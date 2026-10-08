package fun.qxfly.service.Article;

import com.github.pagehelper.PageInfo;
import fun.qxfly.common.domain.entity.Article;
import fun.qxfly.common.domain.entity.Classify;
import fun.qxfly.common.domain.entity.Tag;
import fun.qxfly.common.domain.po.Result;
import fun.qxfly.common.domain.vo.ArticleVO;
import org.springframework.web.multipart.MultipartFile;

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
    String updateArticleCover(MultipartFile file);

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
     * 批量删除文章
     *
     * @param split1 文章id列表
     * @return boolean
     */
    boolean batchDeleteArticle(String[] split1);
}
