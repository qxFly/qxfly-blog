package fun.qxfly.admin.mapper;

import fun.qxfly.common.domain.entity.Comment;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ArticleCommentManageMapper {
    /**
     * 删除文章评论
     *
     * @param commentId
     * @return
     */
    @Delete("delete from comment where id = #{commentId}")
    boolean deleteArticleComment(Integer commentId);

    /**
     * 文章评论审核
     *
     * @param commentId
     * @param verify
     * @return
     */
    @Update("update comment set verify = #{verify} where id = #{commentId}")
    boolean articleCommentVerify(Integer commentId, Integer verify);

    /**
     * 搜索评论
     *
     * @param comment
     * @param createTimeStart
     * @param createTimeEnd
     */
    List<Comment> searchArticleComment(@Param("comment") Comment comment, @Param("createTimeStart") String createTimeStart, @Param("createTimeEnd") String createTimeEnd);
}
