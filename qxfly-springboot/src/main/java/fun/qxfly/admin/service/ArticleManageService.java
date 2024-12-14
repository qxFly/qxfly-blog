package fun.qxfly.admin.service;

import com.github.pagehelper.PageInfo;
import fun.qxfly.common.domain.entity.Article;

public interface ArticleManageService {
    /**
     * 文章审核
     *
     * @param article
     * @param reason
     * @return
     */
    boolean articleVerify(Article article,String reason);

    /**
     * 搜索文章
     * @param currPage
     * @param pageSize
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
    PageInfo<Article> searchArticle(Integer currPage, Integer pageSize, Integer articleId, String title, String author, Integer authorId, String tag, String classify, String createTimeStart, String createTimeEnd, String updateTimeStart, String updateTimeEnd, Integer verify);

    /**
     * 文章
     * @param article
     * @return
     */
    boolean deleteArticle(Article article);
}
