package fun.qxfly.service.Article;

public interface ArticleInteractionService {
    /**
     * 文章点赞
     *
     * @param articleId
     * @param uid
     * @return
     */
    boolean articleLike(Integer articleId, Integer uid);

    /**
     * 取消用户点赞
     *
     * @param articleId
     * @param uid
     */
    boolean cancelArticleLike(Integer articleId, Integer uid);

    /**
     * 文章收藏
     *
     * @param articleId
     * @param uid
     */
    boolean articleCollection(Integer articleId, Integer uid);

    /**
     * 取消用户收藏
     *
     * @param articleId
     * @param uid
     */
    boolean cencelArticleCollection(Integer articleId, Integer uid);

    /**
     * 判断文章是否点赞收藏
     *
     * @param aid 文章id
     * @param uid 用户id
     */
    boolean[] isArticleLikeAndCollection(Integer aid, Integer uid);

    /**
     * 添加文章浏览量
     *
     * @param aid 文章id
     * @param uid 用户id
     * @param UA  用户UA
     */
    void addArticleView(Integer aid, Integer uid, String UA);
}
