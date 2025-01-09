package fun.qxfly.service.Article;

import com.github.pagehelper.PageInfo;
import fun.qxfly.common.domain.entity.Article;
import fun.qxfly.common.domain.entity.Attachment;
import fun.qxfly.common.domain.entity.Classify;
import fun.qxfly.common.domain.entity.Tag;
import fun.qxfly.common.domain.po.PageBean;
import fun.qxfly.common.domain.po.Result;
import fun.qxfly.common.domain.vo.ArticleVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ArticleService {
    /**
     * 发布文章
     *
     * @param article
     * @return
     */
    Integer releaseArticle(Article article, String image);

    /**
     * 分页获取文章
     *
     * @param currPage
     * @param pageSize
     * @return
     */
    PageInfo<ArticleVO> getArticlesByPage(int currPage, int pageSize, String searchData, String sort, boolean Daily, int authorId, int verify, String classify, String[] tagArr, int pub);

    /**
     * 根据id获取文章
     *
     * @param aid 文章id
     * @param uid 用户id
     */
    ArticleVO getArticleById(Integer aid, Integer uid);

    /**
     * 文章封面上传
     *
     * @param file
     * @return
     */
    Result updateArticleCover(MultipartFile file);

    /**
     * 删除之前的封面
     *
     * @param cover
     * @return
     */
    boolean deletePreviousCover(String cover);

    /**
     * 编辑文章
     *
     * @param article
     * @return
     */
    boolean editArticle(Article article);

    /**
     * 文章内容图片上传
     *
     * @param file
     * @return
     */
    Result uploadArticleImage(MultipartFile file);

    /**
     * 删除文章
     *
     * @param aid
     * @return
     */
    boolean deleteArticleById(Integer aid);


    /**
     * 文章点赞
     *
     * @param articleId
     * @param uid
     * @return
     */
    boolean articleLike(Integer articleId, Integer uid);

    /**
     * 取消用户点赞
     *
     * @param articleId
     * @param uid
     */
    boolean cancelArticleLike(Integer articleId, Integer uid);

    /**
     * 文章收藏
     *
     * @param articleId
     * @param uid
     */
    boolean articleCollection(Integer articleId, Integer uid);

    /**
     * 取消用户收藏
     *
     * @param articleId
     * @param uid
     */
    boolean cencelArticleCollection(Integer articleId, Integer uid);

    /**
     * 判断文章是否点赞
     *
     * @param aid 文章id
     * @param uid 用户id
     */
    boolean[] isArticleLikeAndCollection(Integer aid, Integer uid);

    /**
     * 分页获取收藏文章
     *
     * @param currPage
     * @param pageSize
     * @param searchData
     * @param sort
     * @param uid
     * @return
     */
    PageInfo<ArticleVO> getCollectionArticles(int currPage, int pageSize, String searchData, String sort, int uid);

    /**
     * 编辑完成时删除没有选择的文章图片
     *
     * @param imageList
     */
    boolean deleteArticleImage(String[] imageList);

    /**
     * 添加文章浏览量
     *
     * @param aid 文章id
     * @param uid 用户id
     * @param UA  用户UA
     */
    void addArticleView(Integer aid, Integer uid, String UA);

    /**
     * 分页获取所有分类
     *
     * @return 分类列表
     */
    PageInfo<Classify> listClassifiesByPage(int currPage, int pageSize, Integer id, String name);

    /**
     * 获取所有分类
     *
     * @return 分类列表
     */
    List<Classify> listClassifies();

    /**
     * 分页获取所有标签
     *
     * @param currPage 当前页
     * @param pageSize 每页数量
     * @param id       标签id
     * @param name     标签名称
     * @param uid      标签创建者id
     * @return 分页标签列表
     */
    PageInfo<Tag> listTagsByPage(Integer currPage, Integer pageSize, Integer id, String name, Integer uid);

    /**
     * 获取所有标签
     *
     * @return 标签列表
     */
    List<Tag> listTags();

    /**
     * 上传文章附件
     *
     * @param file 文件
     */
    String uploadAttachment(MultipartFile file);

    /**
     * 删除文章附件
     *
     * @param aid      文章id
     * @param fileName 文件名
     */
    boolean deleteAttachment(Integer aid, String fileName);

    /**
     * 保存文章附件
     *
     * @param aid            文章id
     * @param uid            用户id
     * @param attachmentList 附件列表
     */
    Integer saveAttachment(Integer aid, Integer uid, List<Attachment> attachmentList);

    /**
     * 获取文章附件
     *
     * @param aid 文章id
     * @return 附件列表
     */
    List<Attachment> getArticleAttachment(Integer aid);

    /**
     * 批量删除文章
     *
     * @param split1 文章id列表
     * @return boolean
     */
    boolean batchDeleteArticle(String[] split1);
}
