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
     * 删除文章评论（含子评论）
     *
     * @param commentId
     * @return
     */
    @Delete("delete from comment where id = #{commentId} or parentCommentId = #{commentId}")
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
     * 批量审核文章评论
     *
     * @param ids    评论id列表
     * @param verify 审核状态
     * @return 更新条数
     */
    @Update("<script>update comment set verify = #{verify} where id in " +
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>#{id}</foreach></script>")
    int batchArticleCommentVerify(@Param("ids") List<Integer> ids, @Param("verify") Integer verify);

    /**
     * 搜索评论
     *
     * @param comment
     * @param createTimeStart
     * @param createTimeEnd
     */
    List<Comment> searchArticleComment(@Param("comment") Comment comment, @Param("createTimeStart") String createTimeStart, @Param("createTimeEnd") String createTimeEnd);
}
