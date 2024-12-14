package fun.qxfly.service.Article;

import fun.qxfly.common.domain.entity.Comment;
import fun.qxfly.common.domain.entity.User;
import fun.qxfly.common.domain.po.PageBean;

import java.util.List;

public interface ArticleCommentService {

    /**
     * 根据文章id获取评论
     *
     * @param id
     * @return
     */
    PageBean<Comment> getArticleCommentsByPage(int currPage, int pageSize, String sort, int id);

    /**
     * 发布评论
     *
     * @param comment
     * @return
     */
    boolean releaseComment(Comment comment);

    /**
     * 评论点赞
     * @param comment
     * @param u
     * @return
     */
    Integer likeComment(Comment comment, User u);

    /**
     * 获取用户点赞的评论
     * @param aid
     * @param uid
     * @return
     */
    List<Integer> getUserLikeComment(Integer aid, int uid);
}
