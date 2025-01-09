package fun.qxfly.controller.Article;

import com.alibaba.fastjson2.JSONArray;
import com.github.pagehelper.PageInfo;
import fun.qxfly.common.domain.entity.*;
import fun.qxfly.common.domain.po.PageBean;
import fun.qxfly.common.domain.po.Result;
import fun.qxfly.common.domain.vo.ArticleVO;
import fun.qxfly.common.utils.JwtUtils;
import fun.qxfly.service.Article.ArticleService;
import fun.qxfly.service.User.UserInfoService;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@io.swagger.v3.oas.annotations.tags.Tag(name = "文章")
@RequestMapping("/article")
public class ArticleController {

    private final ArticleService articleService;
    private final UserInfoService userInfoService;

    public ArticleController(ArticleService articleService, UserInfoService userInfoService) {
        this.articleService = articleService;
        this.userInfoService = userInfoService;
    }

    /**
     * 发布文章
     *
     * @param article
     * @return
     */
    @PostMapping("/releaseArticle")
    @Operation(description = "发布文章", summary = "发布文章")
    public Result releaseArticle(@RequestBody Article article, HttpServletRequest request) {
        article.setCreateTime(new Date());
        article.setUpdateTime(new Date());
        String token = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(token);
        if (claims == null)
            return Result.error("发布失败");
        Integer uid = (Integer) claims.get("uid");
        User u = userInfoService.getUserInfo(uid);
        if (u == null) {
            return Result.noLoginError();
        }
        String image = article.getAuthor();
        article.setAuthorId(u.getId());
        article.setAuthor(u.getUsername());
        Integer aid = articleService.releaseArticle(article, image);
        return Result.success(aid);
    }

    /**
     * 编辑文章
     *
     * @param article
     * @return
     */
    @PostMapping("/editArticle")
    @Operation(description = "编辑文章", summary = "编辑文章")
    public Result editArticle(@RequestBody Article article) {
        article.setUpdateTime(new Date());
        article.setVerify(1);
        boolean f = articleService.editArticle(article);
        if (f) return Result.success();
        else return Result.error("编辑失败");
    }

    /**
     * 删除文章
     *
     * @param article
     * @return
     */
    @PostMapping("/deleteArticle")
    @Operation(description = "删除文章", summary = "删除文章")
    public Result deleteArticle(@RequestBody Article article) {
        boolean f = articleService.deleteArticleById(article.getId());
        if (f) return Result.success();
        else return Result.error("删除失败");
    }

    /**
     * 批量删除文章
     *
     * @param article
     * @return
     */
    @PostMapping("/batchDeleteArticle")
    @Operation(description = "批量删除文章", summary = "批量删除文章")
    public Result batchDeleteArticle(@RequestBody Article article) {
        String[] split1 = article.getContent().split(",");
        boolean f = articleService.batchDeleteArticle(split1);
        if (f) return Result.success();
        else return Result.error("部分删除失败");
    }

    /**
     * 文章封面上传
     *
     * @param file
     * @return
     */
    @Operation(description = "封面上传", summary = "封面上传")
    @PostMapping("/updateArticleCover")
    public Result updateArticleCover(MultipartFile file) {
        return articleService.updateArticleCover(file);
    }

    /**
     * 文章内容图片上传
     *
     * @param file
     * @return
     */
    @Operation(description = "文章内容图片上传", summary = "文章内容图片上传")
    @PostMapping("/uploadArticleImage")
    public Result uploadArticleImage(MultipartFile file) {
        return articleService.uploadArticleImage(file);
    }

    /**
     * 分页获取文章
     *
     * @param currPage   当前页
     * @param pageSize   页大小
     * @param authorId   用户id
     * @param searchData 搜索内容
     * @param sort       排序方式
     * @param classify   分类
     * @param tag        标签
     * @param daily      是为每日推荐，否则热度推荐
     * @param verify     是否审核通过
     * @param request
     * @return
     */
    @GetMapping("/listArticles")
    @Operation(description = "分页获取文章", summary = "分页获取文章")
    public Result getArticles(@RequestParam int currPage, @RequestParam int pageSize,
                              @RequestParam(defaultValue = "0") int authorId,
                              @RequestParam(defaultValue = "") String searchData,
                              @RequestParam(defaultValue = "new") String sort,
                              @RequestParam(defaultValue = "") String classify,
                              @RequestParam(defaultValue = "") String tag,
                              @RequestParam(defaultValue = "false") boolean daily,
                              @RequestParam(defaultValue = "3") int verify,
                              HttpServletRequest request) {
        String token = request.getHeader("token");
        int pub = 12;//个位：图片是否公开到图片页；十位：文章是否公开。（1、公开、2、私密、3、好友）
        int userId = -1;
        Claims claims = JwtUtils.parseJWT(token);
        if (claims != null) {
            userId = (int) claims.get("uid");
        } else {
            verify = 3;
        }
        if (authorId != userId) {
            verify = 3;
        } else {
            pub = 0;
        }
        String[] tagArr;
        if (tag.isEmpty()) {
            tagArr = null;
        } else {
            tagArr = tag.split(",");
        }
        PageInfo<ArticleVO> pageInfo = articleService.getArticlesByPage(currPage, pageSize, searchData, sort, daily, authorId, verify, classify, tagArr, pub);
        return Result.success(pageInfo);
    }

    /**
     * 分页获取收藏文章
     *
     * @param currPage
     * @param pageSize
     * @param uid
     * @param searchData
     * @param sort
     * @return
     */
    @GetMapping("/getCollectionArticles")
    @Operation(description = "分页获取收藏文章", summary = "分页获取收藏文章")
    public Result getCollectionArticles(@RequestParam int currPage, @RequestParam int pageSize,
                                        @RequestParam int uid, @RequestParam(defaultValue = "") String searchData,
                                        @RequestParam(defaultValue = "newC") String sort) {
        PageInfo<ArticleVO> pageBean = articleService.getCollectionArticles(currPage, pageSize, searchData, sort, uid);
        return Result.success(pageBean);
    }

    /**
     * 根据id获取文章
     *
     * @param aid
     * @return
     */
    @GetMapping("/getArticleById")
    @Operation(description = "根据id获取文章", summary = "根据id获取文章")
    public Result getArticleById(@RequestParam int aid, HttpServletRequest request) {
        String token = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(token);
        Integer uid = null;
        if (claims != null) {
            uid = (Integer) claims.get("uid");
        }
        ArticleVO articleVO = articleService.getArticleById(aid, uid);
        return Result.success(articleVO);
    }

    /**
     * 检测用户章是否可编辑文章
     *
     * @return 包含uid、和用户名的用户对象
     */
    @PostMapping("/checkA")
    @Operation(description = "检测用户章是否可编辑文章，返回包含uid、和用户名的用户对象", summary = "检测用户章是否可编辑文章")
    public Result checkArticle(HttpServletRequest request) {
        Claims claims = JwtUtils.parseJWT(request.getHeader("token"));
        if (claims == null) return Result.error("");
        User user = new User();
        user.setId((Integer) claims.get("uid"));
        user.setUsername((String) claims.get("username"));
        return Result.success(user);
    }

    /**
     * 文章浏览量
     *
     * @param dailyView
     * @return
     */
    @PostMapping("/addArticleView")
    @Operation(description = "文章浏览量", summary = "文章浏览量")
    public Result addArticleView(@RequestBody DailyView dailyView, HttpServletRequest request) {
        String token = request.getHeader("token");
        String UA = request.getHeader("User-Agent");
        Claims claims = JwtUtils.parseJWT(token);
        if (claims == null) return Result.error("");
        Integer uid = (Integer) claims.get("uid");
        articleService.addArticleView(dailyView.getArticleId(), uid, UA);
        return Result.success();
    }

    /**
     * 文章点赞和取消
     *
     * @param dailyView
     * @return
     */
    @PostMapping("/articleLike")
    @Operation(description = "文章点赞", summary = "文章点赞")
    public Result articleLike(@RequestBody DailyView dailyView, HttpServletRequest request) {
        /*从token中获取用户id*/
        String token = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(token);
        if (claims == null) return Result.error("操作失败");
        Integer uid = (Integer) claims.get("uid");
        if (dailyView.getViews().equals(0))
            articleService.articleLike(dailyView.getArticleId(), uid);
        else
            articleService.cancelArticleLike(dailyView.getArticleId(), uid);
        return Result.success();
    }

    /**
     * 文章收藏和取消
     *
     * @param Collection
     * @return
     */
    @PostMapping("/articleCollection")
    @Operation(description = "文章收藏", summary = "文章收藏")
    public Result articleCollection(@RequestBody DailyView Collection, HttpServletRequest request) {
        /*从token中获取用户id*/
        String token = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(token);
        if (claims == null) return Result.operationError();
        Integer uid = (Integer) claims.get("uid");
        /* 0为没有收藏，否则取消收藏*/
        if (Collection.getViews().equals(0))
            articleService.articleCollection(Collection.getArticleId(), uid);
        else
            articleService.cencelArticleCollection(Collection.getArticleId(), uid);
        return Result.success();

    }

    /**
     * 编辑完成时删除没有选择的文章图片
     *
     * @param imageList
     * @return
     */
    @GetMapping("/deleteArticleImage")
    @Operation(description = "编辑完成时删除没有选择的文章图片", summary = "删除文章图片")
    public Result deleteArticleImage(@RequestParam String imageList) {
        String[] split = imageList.split(",");
        articleService.deleteArticleImage(split);
        return Result.success();
    }

    /**
     * 获取文章分类
     *
     * @return
     */
    @GetMapping("/getClassifies")
    @Operation(description = "获取文章分类", summary = "获取文章分类")
    public Result getClassifies(@RequestParam(defaultValue = "0") Integer currPage,
                                @RequestParam(defaultValue = "0") Integer pageSize,
                                @RequestParam(defaultValue = "0") Integer id,
                                @RequestParam(defaultValue = "") String name) {
        if (id == 0) id = null;
        if (currPage != 0) {
            PageInfo<Classify> classifyList = articleService.listClassifiesByPage(currPage, pageSize, id, name);
            return Result.success(classifyList);
        } else {
            List<Classify> classifies = articleService.listClassifies();
            return Result.success(classifies);
        }
    }

    /**
     * 获取标签
     *
     * @return
     */
    @GetMapping("/getTags")
    @Operation(description = "获取标签", summary = "获取标签")
    public Result getTags(@RequestParam(defaultValue = "0") Integer currPage,
                          @RequestParam(defaultValue = "0") Integer pageSize,
                          @RequestParam(defaultValue = "0") Integer id,
                          @RequestParam(defaultValue = "0") Integer uid,
                          @RequestParam(defaultValue = "") String name) {
        if (id == 0) id = null;
        if (uid == 0) uid = null;
        if (currPage != 0) {
            PageInfo<Tag> tagsPageInfo = articleService.listTagsByPage(currPage, pageSize, id, name, uid);
            return Result.success(tagsPageInfo);
        } else {
            List<Tag> tagList = articleService.listTags();
            return Result.success(tagList);
        }
    }

    /**
     * 上传文章附件
     *  todo 文件，文章附件上传
     * @param file
     * @return
     */
    @Operation(description = "上传文章附件", summary = "上传文章附件")
    @PostMapping("/uploadAttachment")
    public Result upload(MultipartFile file) {
        String fileName = articleService.uploadAttachment(file);
        if (fileName != null) {
            return Result.success(fileName);
        } else {
            return Result.error("上传失败");
        }
    }

    /**
     * 删除文章附件
     * todo 文件，文章附件删除
     * @param fileName
     * @return
     */
    @Operation(description = "删除文章附件", summary = "删除文章附件")
    @GetMapping("/deleteAttachment")
    public Result deleteAttachment(@RequestParam Integer aid, @RequestParam String fileName) {
        boolean f = articleService.deleteAttachment(aid, fileName);
        if (fileName != null) {
            return Result.success(fileName);
        } else {
            return Result.error("删除失败");
        }
    }

    /**
     * 获取文章附件
     *
     * @param id
     * @return
     */
    @Operation(description = "根据id获取文章附件", summary = "获取文章附件")
    @GetMapping("/getArticleAttachment")
    public Result getArticleAttachment(@RequestParam Integer id) {
        List<Attachment> attachmentList = articleService.getArticleAttachment(id);
        return Result.success(attachmentList);
    }

    /**
     * 保存文章附件
     *
     * @param
     * @return
     */
    @Operation(description = "保存文章附件", summary = "保存文章附件")
    @PostMapping("/saveAttachment")
    public Result saveAttachment(@RequestBody HashMap<String, Object> map, HttpServletRequest request) {
        List<Attachment> attachmentList = JSONArray.parseArray((String) map.get("attachment")).toJavaList(Attachment.class);
        Integer aid = (Integer) map.get("aid");
        String token = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(token);
        if (claims == null) return Result.error("保存失败");
        Integer uid = (Integer) claims.get("uid");
        Integer f = articleService.saveAttachment(aid, uid, attachmentList);
        return Result.success();
    }
}
