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
     * 发布评论（校验失败抛出 IllegalArgumentException）
     *
     * @param comment
     * @return true 已发布，false 敏感词命中待审核
     */
    boolean releaseComment(Comment comment);

    /**
     * 评论点赞（校验失败抛出 IllegalArgumentException）
     *
     * @param comment
     * @param u
     * @return 1 点赞成功，0 取消点赞
     */
    Integer likeComment(Comment comment, User u);

    /**
     * 获取用户点赞的评论
     *
     * @param aid
     * @param uid
     * @return
     */
    List<Integer> getUserLikeComment(Integer aid, int uid);

    /**
     * 删除评论及子评论（无权删除抛出 IllegalArgumentException）
     *
     * @param cid
     * @param uid 当前登录用户id
     * @return 删除条数
     */
    Integer deleteComment(Integer cid, Integer uid);
}
