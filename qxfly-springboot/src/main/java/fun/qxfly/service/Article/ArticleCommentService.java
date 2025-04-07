package fun.qxfly.service.Article;

import com.github.pagehelper.PageInfo;
import fun.qxfly.common.domain.entity.Comment;
import fun.qxfly.common.domain.entity.User;

import java.util.List;

public interface ArticleCommentService {

    /**
     * 根据文章id获取评论
     *
     * @param id
     * @return
     */
    PageInfo<Comment> getArticleCommentsByPage(int currPage, int pageSize, String sort, int id);

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

    /**
     * 删除评论
     *
     * @param cid
     * @return
     */
    Integer deleteComment(Integer cid);
}
