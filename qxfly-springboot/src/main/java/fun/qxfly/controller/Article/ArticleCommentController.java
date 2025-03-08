package fun.qxfly.controller.Article;

import com.github.pagehelper.PageInfo;
import fun.qxfly.common.domain.entity.Comment;
import fun.qxfly.common.domain.entity.User;
import fun.qxfly.common.domain.po.Result;
import fun.qxfly.common.utils.JwtUtils;
import fun.qxfly.service.Article.ArticleCommentService;
import fun.qxfly.service.User.UserInfoService;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@io.swagger.v3.oas.annotations.tags.Tag(name = "文章评论")
@RequestMapping("/article")
public class ArticleCommentController {

    private final ArticleCommentService articleCommentService;
    private final UserInfoService userInfoService;

    public ArticleCommentController(ArticleCommentService articleCommentService, UserInfoService userInfoService) {
        this.articleCommentService = articleCommentService;
        this.userInfoService = userInfoService;
    }

    /**
     * 根据文章id获取评论
     *
     * @param currPage
     * @param pageSize
     * @param sort
     * @param id
     * @return
     */
    @GetMapping("/getArticleComments")
    @Operation(description = "根据文章id获取评论", summary = "根据文章id获取评论")
    public Result getArticleComments(@RequestParam int currPage, @RequestParam int pageSize, @RequestParam(defaultValue = "new") String sort, @RequestParam int id) {
        PageInfo<Comment> pageInfo = articleCommentService.getArticleCommentsByPage(currPage, pageSize, sort, id);
        return Result.success(pageInfo);
    }

    /**
     * 发布评论
     *
     * @param comment
     * @param request
     * @return
     */
    @PostMapping("/releaseComment")
    @Operation(description = "发布评论", summary = "发布评论")
    public Result releaseComment(@RequestBody Comment comment, HttpServletRequest request) {
        String token = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(token);
        Integer uid = (Integer) claims.get("uid");
        User u = userInfoService.getUserInfo(uid);
        User user = new User();
        user.setId(u.getId());
        user.setUsername(u.getUsername());
        comment.setUser(user);
        comment.setCreateTime(new Date());
        boolean b = articleCommentService.releaseComment(comment);
        if (b)
            return Result.success();
        else
            return Result.error("评论审核中，请耐心等待");

    }

    /**
     * 获取用户评论点赞
     *
     * @param request
     * @return
     */
    @GetMapping("/getUserLikeComment")
    @Operation(description = "获取用户评论点赞", summary = "获取用户评论点赞")
    public Result getUserLikeComment(@RequestParam Integer aid, HttpServletRequest request) {
        String token = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(token);
        int uid = (Integer) claims.get("uid");
        List<Integer> likeComment = articleCommentService.getUserLikeComment(aid, uid);
        return Result.success(likeComment);
    }


    /**
     * 评论点赞
     *
     * @param comment
     * @param request
     * @return
     */
    @PostMapping("/likeComment")
    @Operation(description = "评论点赞", summary = "评论点赞")
    public Result likeComment(@RequestBody Comment comment, HttpServletRequest request) {
        String token = request.getHeader("token");
        User u = new User();
        Claims claims = JwtUtils.parseJWT(token);
        u.setId((Integer) claims.get("uid"));
        Integer f = articleCommentService.likeComment(comment, u);
        return Result.success(f);
    }
}
