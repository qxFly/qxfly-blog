package fun.qxfly.mapper.Article;

import fun.qxfly.common.domain.entity.Comment;
import fun.qxfly.common.domain.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleCommentMapper {

    /**
     * 根据文章id获取评论
     *
     * @param id
     * @return
     */
    List<Comment> getArticleCommentsByPage(@Param("sort") String sort, @Param("id") int id);

    /**
     * 批量获取子评论
     *
     * @param ids 父评论id列表
     * @return
     */
    List<Comment> getChildCommentsByParentIds(@Param("ids") List<Integer> ids);

    /**
     * 根据评论id获取评论（用于校验归属）
     *
     * @param cid
     * @return
     */
    Comment getCommentById(@Param("cid") Integer cid);

    /**
     * 发布评论
     *
     * @param comment
     * @return
     */
    @Insert("insert into comment(articleId, content, parentCommentId, userId, username, createTime,toUserId,toUsername,verify)values(#{articleId},#{content},#{parentCommentId},#{user.id},#{user.username},#{createTime},#{toUserId},#{toUsername},#{verify})")
    boolean releaseComment(Comment comment);

    /**
     * 添加评论点赞
     *
     * @param comment
     */
    @Update("update comment set likeCount = likeCount + 1 where id = #{id}")
    Integer addCommentLike(Comment comment);

    /**
     * 添加用户评论点赞
     *
     * @param comment
     * @param u
     */
    @Insert("insert into user_like_comment(uid, cid,aid)values(#{u.id},#{comment.id},#{comment.articleId})")
    Integer addUserCommentLike(@Param("u") User u, @Param("comment") Comment comment);

    /**
     * 删除用户评论点赞
     *
     * @param u
     * @param comment
     */
    @Delete("delete from user_like_comment where uid = #{u.id} and cid = #{comment.id}")
    Integer deleteUserCommentLike(@Param("u") User u, @Param("comment") Comment comment);

    /**
     * 获取用户点赞的评论
     *
     * @param aid
     * @param uid
     * @return
     */
    @Select("select cid from user_like_comment where uid = #{uid} and aid = #{aid}")
    List<Integer> getUserLikeComment(@Param("aid") Integer aid, @Param("uid") int uid);

    /**
     * 减少评论点赞数（不小于0）
     *
     * @param comment
     */
    @Update("update comment set likeCount = GREATEST(likeCount - 1, 0) where id = #{id}")
    void reduceCommentLike(Comment comment);

    /**
     * 删除评论及其子评论
     *
     * @param cid
     * @return
     */
    @Delete("delete from comment where id = #{cid} or parentCommentId = #{cid}")
    Integer deleteCommentWithChildren(@Param("cid") Integer cid);

    /**
     * 校验文章是否存在
     *
     * @param id
     * @return
     */
    @Select("select count(*) from article where id = #{id}")
    Integer existsArticle(@Param("id") int id);
}
