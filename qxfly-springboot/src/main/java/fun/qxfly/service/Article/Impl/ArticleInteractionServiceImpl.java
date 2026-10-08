package fun.qxfly.service.Article.Impl;

import com.alibaba.fastjson2.JSONObject;
import fun.qxfly.common.domain.entity.DailyView;
import fun.qxfly.common.domain.entity.UserLikesAndCollection;
import fun.qxfly.mapper.Article.ArticleMapper;
import fun.qxfly.service.Article.ArticleInteractionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;

@Service
public class ArticleInteractionServiceImpl implements ArticleInteractionService {
    private final ArticleMapper articleMapper;

    public ArticleInteractionServiceImpl(ArticleMapper articleMapper) {
        this.articleMapper = articleMapper;
    }

    /**
     * 文章点赞
     *
     * @param aid 文章id
     * @param uid 用户id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean articleLike(Integer aid, Integer uid) {
        Integer al = articleMapper.getUserArticleLike(aid, uid);
        /*获取用户的点赞json数据*/
        UserLikesAndCollection userLikes = articleMapper.getUserLikes(uid);
        ArrayList<Integer> articles = new ArrayList<>();
        /*如果为空，则创建相关json数据*/
        if (userLikes == null) {
            articles.add(aid);
            UserLikesAndCollection userLikes1 = new UserLikesAndCollection(uid, JSONObject.toJSONString(articles), JSONObject.toJSONString(""));
            articleMapper.addUserLikes(userLikes1);
        } else {
            //否则查询用户是否点赞
            String likeArticles = userLikes.getLikeArticles();
            ArrayList<Integer> arrayList = new ArrayList<>();
            if (likeArticles != null) {
                arrayList = JSONObject.parseObject(likeArticles, ArrayList.class);
                for (Integer item : arrayList) {
                    if (item.equals(aid)) {
                        //已点赞
                        return false;
                    }
                }
                //如果点赞记录超过500条，则删除最早的一条
                if (arrayList.size() > 500) {
                    arrayList.remove(0);
                }
            }
            //未点赞
            arrayList.add(aid);
            userLikes.setLikeArticles(JSONObject.toJSONString(arrayList));
            articleMapper.updateUserLikes(userLikes);
            if (al == null || al == 0) {
                articleMapper.addUserArticleLike(aid, uid);
                articleMapper.articleLike(aid);
            }
        }
        return true;
    }

    /**
     * 取消用户点赞
     *
     * @param aid 文章id
     * @param uid 用户id
     */
    @Override
    public boolean cancelArticleLike(Integer aid, Integer uid) {
        UserLikesAndCollection userLikes = articleMapper.getUserLikes(uid);
        if (userLikes != null && userLikes.getLikeArticles() != null) {
            ArrayList<Integer> arrayList = JSONObject.parseObject(userLikes.getLikeArticles(), ArrayList.class);
            for (Integer item : arrayList) {
                if (item.equals(aid)) {
                    arrayList.remove(aid);
                    userLikes.setLikeArticles(JSONObject.toJSONString(arrayList));
                    articleMapper.updateUserLikes(userLikes);
                    return true;
                }
            }

        }
        return false;
    }

    /**
     * 文章收藏
     *
     * @param aid 文章id
     * @param uid 用户id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean articleCollection(Integer aid, Integer uid) {
        Integer b = articleMapper.userIsCollArt(aid, uid);
        if (b == null) {
            articleMapper.updateUserCollection(aid, uid, new Date());
            articleMapper.addarticleCollectionCount(aid);
        }
        return true;
    }

    /**
     * 取消用户收藏
     *
     * @param aid 文章id
     * @param uid 用户id
     */
    @Override
    public boolean cencelArticleCollection(Integer aid, Integer uid) {
        return articleMapper.deleteUserCollection(aid, uid);
    }

    /**
     * 增加文章访问量
     *
     * @param aid 文章id
     * @param uid 用户id
     * @param UA  用户UA
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addArticleView(Integer aid, Integer uid, String UA) {
        Integer view;
        if (uid != null) {
            view = articleMapper.getUserArticleView(aid, uid, UA);
        } else {
            view = articleMapper.getUAArticleView(aid, UA);
        }
        if (view == null || view == 0) {
            articleMapper.addUserArticleView(aid, uid, UA);
            articleMapper.addArticleTotalViews(aid);
            DailyView dailyView = articleMapper.getDailyViewByArticleId(aid);
            if (dailyView == null) {
                articleMapper.addDailyView(aid);
            } else {
                articleMapper.updateDailyView(aid);
            }
        }
    }

    /**
     * 判断文章是否点赞收藏
     *
     * @param aid 文章id
     * @param uid 用户id
     * @return boolean[点赞，收藏]
     */
    @Override
    public boolean[] isArticleLikeAndCollection(Integer aid, Integer uid) {
        boolean[] result = {false, false};
        UserLikesAndCollection userLikes = articleMapper.getUserLikes(uid);
        if (userLikes == null) {
            return result;
        } else {
            if (userLikes.getLikeArticles() != null) {
                String likeArticles = userLikes.getLikeArticles();
                ArrayList<Integer> a = JSONObject.parseObject(likeArticles, ArrayList.class);
                for (Integer item : a) {
                    if (item.equals(aid)) {
                        result[0] = true;
                        break;
                    }
                }
            }
            if (articleMapper.userIsCollArt(aid, uid) != null) result[1] = true;
        }
        return result;
    }
}
