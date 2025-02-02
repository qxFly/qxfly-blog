package fun.qxfly.mapper.Article;

import fun.qxfly.common.domain.entity.*;
import fun.qxfly.common.domain.vo.ArticleVO;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface ArticleMapper {
    /**
     * 发布文章
     *
     * @param article
     * @return
     */
    Integer releaseArticle(Article article);

    /**
     * 获取文章数
     *
     * @return
     */
    int getArticleCount(@Param("searchData") String searchData, @Param("authorId") int authorId, @Param("verify") int verify, @Param("classify") String classify, @Param("tagArr") String[] tagArr, @Param("pub") int pub);

    /**
     * 分页获取文章
     *
     * @return
     */
    List<ArticleVO> getArticlesByPage( @Param("searchData") String searchData, @Param("sort") String sort, @Param("authorId") int authorId, @Param("verify") int verify, @Param("classify") String classify, @Param("tagArr") String[] tagArr, @Param("pub") int pub);

    /**
     * 根据id获取文章
     *
     * @param id
     * @return
     */
    @Select("select * from article where id = #{id}")
    Article getArticleById(int id);

    /**
     * 编辑文章
     *
     * @param article
     * @return
     */
    @Update("update article set title = #{title}, content = #{content}, preview = #{preview}, cover = #{cover}, updateTime = #{updateTime}, tag = #{tag}, verify = #{verify}, classify = #{classify},pub = #{pub} where id = #{id}")
    boolean editArticle(Article article);

    /**
     * 删除文章
     *
     * @param article
     * @return
     */
    @Delete("delete from article where id = #{id}")
    boolean deleteArticleById(Article article);

    /**
     * 增加文章总访问量
     *
     * @param aid
     */
    @Update("update article set views = views + 1 where id = #{aid}")
    boolean addArticleTotalViews(int aid);

    /**
     * 获取文章浏览量
     *
     * @param aid
     * @return
     */
    @Select("select * from daily_view where aid = #{aid}")
    DailyView getDailyViewByArticleId(int aid);

    /**
     * 向表中插入文章日浏览量
     *
     * @param aid
     */
    @Insert("insert into daily_view(aid) VALUES (#{aid})")
    void addDailyView(int aid);

    /**
     * 增加文章日浏览量
     *
     * @param aid
     */
    @Update("update daily_view set daily_views = daily_views + 1, Weekly_views = Weekly_views + 1, Monthly_views = Monthly_views + 1 where aid = #{aid}")
    void updateDailyView(int aid);

    /**
     * 文章点赞
     *
     * @param aid
     * @return
     */
    @Update("update article set likes = likes + 1 where id = #{aid};")
    boolean articleLike(Integer aid);

    /**
     * 文章收藏
     *
     * @param aid
     * @return
     */
    @Update("update article set collection = article.collection + 1 where id = #{aid};")
    boolean addarticleCollectionCount(Integer aid);

    /**
     * 获取用户点赞收藏的文章
     *
     * @param uid
     * @return
     */
    @Select("select * from user_like_article where uid = #{uid}")
    UserLikesAndCollection getUserLikes(Integer uid);

    /**
     * 添加用户点赞的文章
     *
     * @param userLikes
     */
    @Insert("insert into user_like_article(uid, likeArticles)values(#{uid},#{likeArticles})")
    void addUserLikes(UserLikesAndCollection userLikes);

    /**
     * 更新用户点赞的文章
     *
     * @param userLikes
     */
    @Update("update user_like_article set likeArticles = #{likeArticles} where uid = #{uid}")
    void updateUserLikes(UserLikesAndCollection userLikes);

    /**
     * 获取每日热门文章数
     *
     * @return
     */
    @Select("select count(*) from daily_view where daily_views != 0")
    int getDailyArticleCount();

    /**
     * 获取每日热门文章
     *
     * @return
     */
    @Select("select * from article a, daily_view dv where dv.aid = a.id and a.verify = 3 and dv.daily_views != 0 order by dv.daily_views desc")
    List<ArticleVO> getDailyArticlesByPage();

    /**
     * 分页获取收藏文章
     *
     * @param searchData
     * @param sort
     * @param uid
     * @return
     */
    List<ArticleVO> getUserCollection(@Param("searchData") String searchData, @Param("sort") String sort, @Param("uid") int uid);

    /**
     * 收藏文章
     *
     * @param aid
     * @param uid
     * @param date
     */
    @Insert("insert into user_collection(uid, aid, collectionTime)values(#{uid},#{aid},#{date}) ")
    void updateUserCollection(@Param("aid") Integer aid, @Param("uid") Integer uid, @Param("date") Date date);

    /**
     * 取消文章收藏
     *
     * @param aid
     * @param uid
     */
    @Delete("delete from user_collection where uid = #{uid} and aid = #{aid}")
    boolean deleteUserCollection(@Param("aid") Integer aid, @Param("uid") Integer uid);

    /**
     * 判断用户是否收藏文章
     *
     * @param aid
     * @param uid
     * @return
     */
    @Select("select * from user_collection where aid = #{aid} and uid = #{uid}")
    Integer userIsCollArt(@Param("aid") Integer aid, @Param("uid") Integer uid);

    /**
     * 获取用户今日是否浏览该文章
     *
     * @param aid
     * @param uid
     * @return
     */
    @Select("select count(*) from user_article_daily_view where (aid = #{aid} and uid = #{uid}) or (aid = #{aid} and UserAgent = #{UA})")
    Integer getUserArticleView(@Param("aid") Integer aid, @Param("uid") Integer uid, @Param("UA") String UA);

    /**
     * 添加用户每日浏览记录
     *
     * @param aid
     * @param uid
     */
    @Insert("insert into user_article_daily_view(aid, uid, UserAgent)values(#{aid},#{uid},#{UA}) ")
    void addUserArticleView(@Param("aid") Integer aid, @Param("uid") Integer uid, @Param("UA") String UA);

    /**
     * 获取用户对文章的点赞状态
     *
     * @param aid
     * @param uid
     * @return
     */
    @Select("select * from user_article_daily_like where uid = #{uid} and aid = #{aid}")
    Integer getUserArticleLike(@Param("aid") Integer aid, @Param("uid") Integer uid);

    /**
     * 添加用户每日点赞记录
     *
     * @param aid
     * @param uid
     */
    @Insert("insert into user_article_daily_like(uid, aid)values(#{uid},#{aid})")
    void addUserArticleLike(@Param("aid") Integer aid, @Param("uid") Integer uid);

    /**
     * 分页获取所有分类
     *
     * @return
     */
    List<Classify> listClassifiesByPage(@Param("id") Integer id, @Param("name") String name);

    /**
     * 获取所有分类
     *
     * @return
     */
    @Select("select * from classify")
    List<Classify> listClassifies();

    /**
     * 分页获取所有标签
     *
     * @param id
     * @param name
     * @param uid
     * @return
     */
    List<Tag> listTagsByPage(@Param("id") Integer id, @Param("name") String name, @Param("uid") Integer uid);

    /**
     * 获取所有标签
     *
     * @return
     */
    @Select("select * from tag")
    List<Tag> listTags();

    /**
     * 获取用户今日是否点赞
     *
     * @param user
     * @param comment
     * @return
     */
    @Select("select count(*) from user_comment_daily_like where uid = #{user.id} and cid = #{comment.id}")
    Integer getUserCommentDailyLike(@Param("user") User user, @Param("comment") Comment comment);

    /**
     * 添加用户今日点赞
     *
     * @param user
     * @param comment
     */
    @Insert("insert into user_comment_daily_like(uid, cid)values(#{user.id},#{comment.id})")
    Integer addUserCommentDailyLike(@Param("user") User user, @Param("comment") Comment comment);

    /**
     * 保存文章图片
     *
     * @param aid
     * @param image
     * @return
     */
    @Insert("insert into article_image(aid, image)values(#{aid}, #{image})")
    boolean saveArticleImage(@Param("aid") Integer aid, @Param("image") String image);

    /**
     * 更新文章图片
     *
     * @param aid
     * @param image
     */
    @Update("update article_image set image = #{image} where aid = #{aid}")
    void updateArticleImage(@Param("aid") Integer aid, @Param("image") String image);

    /**
     * 获取文章图片
     *
     * @param article
     * @return
     */
    @Select("select image from article_image where aid = #{id}")
    String getArticleImage(Article article);

    /**
     * 保存文章附件
     *
     * @param aid
     * @param uid
     * @param attachment
     * @return
     */
    @Insert("insert into article_attachment(aid, uid, file_name, file_origin_name)values(#{aid}, #{uid}, #{attachment.fileName}, #{attachment.fileOriginName})")
    Integer saveAttachment(@Param("aid") Integer aid, @Param("uid") Integer uid, @Param("attachment") Attachment attachment);

    /**
     * 获取文章附件
     *
     * @param id
     * @return
     */
    @Select("select * from article_attachment where aid = #{id}")
    List<Attachment> getArticleAttachmentByAid(@Param("id") Integer id);

    /**
     * 删除文章附件
     *
     * @param aid
     * @param fileName
     */
    @Delete("delete from article_attachment where aid = #{aid} and file_name = #{fileName}")
    void deleteAttachment(@Param("aid") Integer aid, @Param("fileName") String fileName);

    /**
     * 删除文章所有附件
     *
     * @param id
     */
    @Delete("delete from article_attachment where aid = #{id}")
    void deleteAllArticleAttachmentByAid(Integer id);

    /**
     * 如果文章公开图片，则单独保存
     *
     * @param id
     * @param imageName
     */
    @Insert("insert into image(aid, name, createTime)values(#{id}, #{imageName}, #{date})")
    void savePubImage(@Param("id") Integer id, @Param("imageName") String imageName, @Param("date") Date date);

    /**
     * 如果文章不公开图片 删除文章公开图片
     *
     * @param id
     */
    @Delete("delete from image where aid = #{id}")
    void removePubImage(Integer id);

    /**
     * 如果文章不公开图片 删除文章公开图片,根据图片名称差异删除
     *
     * @param item
     */
    @Delete("delete from image where name = #{item}")
    void removePubImageByImageName( String item);

    /**
     * 检查文章公开图片是否重复
     *
     * @param id
     * @param imageName
     * @return
     */
    @Select("select count(*) from image where aid = #{id} and name = #{imageName}")
    Integer checkSamePubImage(@Param("id") Integer id, @Param("imageName") String imageName);
}
