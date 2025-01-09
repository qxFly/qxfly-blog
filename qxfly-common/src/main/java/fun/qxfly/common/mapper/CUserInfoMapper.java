package fun.qxfly.common.mapper;

import fun.qxfly.common.domain.entity.Token;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CUserInfoMapper {
    /**
     * 刷新用户信息
     *
     * @param articleCount
     * @param likeCount
     * @param tagsCount
     * @param id
     */
    @Update("update user_card set Articles = #{articleCount}, Likes = #{likeCount}, Tags = #{tagsCount}, Collection = #{Collection}, Views = #{views} WHERE username = (SELECT username FROM user WHERE id = #{uid})")
    boolean refreshUserInfo(@Param("articleCount") Integer articleCount, @Param("likeCount") Integer likeCount, @Param("Collection") Integer Collection, @Param("views") Integer views, @Param("tagsCount") Integer tagsCount, @Param("uid") int uid);

    /**
     * 获取文章、点赞、收藏、浏览数
     *
     * @return
     */
    Integer getArticleCount(Integer uid);

    Integer getLikeCount(Integer uid);

    Integer getCollectionCount(Integer uid);

    Integer getViewsCount(Integer uid);
}
