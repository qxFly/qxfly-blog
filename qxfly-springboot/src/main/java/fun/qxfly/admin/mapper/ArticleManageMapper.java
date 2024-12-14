package fun.qxfly.admin.mapper;

import fun.qxfly.common.domain.entity.Article;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleManageMapper {
    /**
     * 查询所有文章
     *
     * @return
     */
    List<Article> listArticle(int verify);

    /**
     * 文章审核
     *
     * @param article
     * @return
     */
    @Update("update article set verify = #{verify} ,tag = #{tag} ,classify = #{classify} where id = #{id}")
    boolean articleVerify(Article article);

    /**
     * 搜索文章
     *
     * @param articleId
     * @param title
     * @param author
     * @param authorId
     * @param tag
     * @param createTimeStart
     * @param createTimeEnd
     * @param updateTimeStart
     * @param updateTimeEnd
     * @param verify
     * @return
     */
    List<Article> searchArticle(@Param("articleId") Integer articleId,
                                @Param("title") String title,
                                @Param("author") String author,
                                @Param("authorId") Integer authorId,
                                @Param("tag") String tag,
                                @Param("classify") String classify,
                                @Param("createTimeStart") String createTimeStart,
                                @Param("createTimeEnd") String createTimeEnd,
                                @Param("updateTimeStart") String updateTimeStart,
                                @Param("updateTimeEnd") String updateTimeEnd,
                                @Param("verify") Integer verify);

    /**
     * 删除文章
     *
     * @param articleId
     * @return
     */
    @Delete("delete from article where id = #{articleId}")
    boolean deleteArticle(int articleId);

    /**
     * 添加未通过文章的原因
     *
     * @param article
     * @param reason
     */
    @Insert("insert into not_passed_article (aid, reason) values (#{article.id}, #{reason})")
    void addNoPassedArticle(@Param("article") Article article, @Param("reason") String reason);

    /**
     * 删除未通过文章的原因
     *
     * @param article
     */
    @Delete("delete from not_passed_article where aid = #{id}")
    void removeNoPassedArticle(Article article);
}
