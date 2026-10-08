package fun.qxfly.admin.service;

import com.github.pagehelper.PageInfo;
import fun.qxfly.common.domain.entity.Comment;

public interface ArticleCommentManageService {
    /**
     * 删除文章评论
     *
     * @param commentId
     * @return
     */
    boolean deleteArticleComment(Integer commentId);

    /**
     * 文章评论审核
     *
     * @param commentId
     * @param verify
     * @return
     */
    boolean articleCommentVerify(Integer commentId, Integer verify);

    /**
     * 批量审核文章评论
     *
     * @param ids    评论id列表
     * @param verify 审核状态
     * @return
     */
    boolean batchArticleCommentVerify(java.util.List<Integer> ids, Integer verify);

    /**
     * 搜索评论
     * @param comment
     * @param createTimeStart
     * @param createTimeEnd
     * @return
     */
    PageInfo<Comment> searchArticleComment(Integer currPage, Integer pageSize, Comment comment, String createTimeStart, String createTimeEnd);
}
