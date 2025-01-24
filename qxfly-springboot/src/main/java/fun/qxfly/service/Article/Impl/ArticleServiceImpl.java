package fun.qxfly.service.Article.Impl;

import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import fun.qxfly.common.domain.entity.*;
import fun.qxfly.common.domain.po.Result;
import fun.qxfly.common.domain.vo.ArticleVO;
import fun.qxfly.common.enums.FilePaths;
import fun.qxfly.common.utils.FileUtils;
import fun.qxfly.mapper.Article.ArticleMapper;
import fun.qxfly.service.Article.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService {
    @Value("${qxfly.file.articleCover.download.path}")
    private String articleCoverDownloadPath;
    @Value("${qxfly.file.articleImage.download.path}")
    private String articleImageDownloadPath;
    @Value("${qxfly.file.articleAttachment.download.path}")
    private String articleAttachmentDownloadPath;
    private final ArticleMapper articleMapper;

    public ArticleServiceImpl(ArticleMapper articleMapper) {
        this.articleMapper = articleMapper;
    }

    /**
     * 发布文章
     *
     * @param article
     * @return
     */
    @Override
    public Integer releaseArticle(Article article, String image) {
        /*封面以文件名方式保存至数据库*/
        String[] split1 = article.getCover().split("/");
        article.setCover(split1[split1.length - 1]);
        articleMapper.releaseArticle(article);
        /*保存文章中的图片*/
        if (image != null && !image.equals("")) {
            String[] imageArr = image.split(",");
            String[] imageUrl = new String[imageArr.length];
            for (int i = 0; i < imageArr.length; i++) {
                String[] split = imageArr[i].split("/");
                String imageName = split[split.length - 1];
                /*如果文章公开图片，则单独保存*/
                if (article.getPub() % 10 == 1)
                    articleMapper.savePubImage(article.getId(), imageName, new Date());
                else
                    articleMapper.removePubImage(article.getId());
                imageUrl[i] = imageName;
            }
            articleMapper.saveArticleImage(article.getId(), JSONObject.toJSONString(imageUrl));
        }
        return article.getId();
    }

    /**
     * 编辑文章
     *
     * @param article
     * @return
     */
    @Override
    public boolean editArticle(Article article) {
        /* 如果更换封面则删除之前的封面 */
        Article article1 = articleMapper.getArticleById(article.getId());
        if (!article.getCover().equals(article1.getCover())) {
            if (!deletePreviousCover(article1.getCover())) {
                return false;
            }
            ;
        }
        /*以文件名方式保存至数据库*/
        String[] split1 = article.getCover().split("/");
        article.setCover(split1[split1.length - 1]);
        /*保存文章中的图片*/
        String[] imageArr = article.getAuthor().split(",");
        String[] imageUrl = new String[imageArr.length];
        for (int i = 0; i < imageArr.length; i++) {
            String[] split = imageArr[i].split("/");
            String imageName = split[split.length - 1];
            /*如果文章公开图片，则单独保存*/
            if (article.getPub() % 10 == 1) {
                if (!imageName.equals("")) {
                    Integer c = articleMapper.checkSamePubImage(article.getId(), imageName);
                    if (c == 0)
                        articleMapper.savePubImage(article.getId(), imageName, new Date());
                }
            } else {
                articleMapper.removePubImage(article.getId());
            }
            imageUrl[i] = imageName;
        }
        articleMapper.updateArticleImage(article.getId(), JSONObject.toJSONString(imageUrl));
        return articleMapper.editArticle(article);
    }

    /**
     * 分页获取文章
     * @param currPage 当前页
     * @param pageSize 每页大小
     * @param searchData 搜索条件
     * @param sort 排序条件
     * @param daily 是否为每日推荐
     * @param authorId 作者id
     * @param verify 审核状态
     * @param classify 分类
     * @param tagArr 标签
     * @param pub 公开状态
     * @return 分页文章
     */
    @Override
    public PageInfo<ArticleVO> getArticlesByPage(int currPage, int pageSize, String searchData, String sort, boolean daily, int authorId, int verify, String classify, String[] tagArr, int pub) {
        List<ArticleVO> articleList;
        if (!daily) {
            PageHelper.startPage(currPage, pageSize);
            articleList = articleMapper.getArticlesByPage(searchData, sort, authorId, verify, classify, tagArr, pub);
        } else {
            PageHelper.startPage(currPage, pageSize);
            articleList = articleMapper.getDailyArticlesByPage();
        }
        for (ArticleVO article : articleList) {
            article.setCover(articleCoverDownloadPath + article.getCover());
        }
        return new PageInfo<>(articleList);
    }

    /**
     * 分页获取收藏文章
     *
     * @param currPage 当前页
     * @param pageSize 每页大小
     * @param searchData 搜索条件
     * @param sort 排序条件
     * @param uid 用户id
     * @return 分页文章
     */
    @Override
    public PageInfo<ArticleVO> getCollectionArticles(int currPage, int pageSize, String searchData, String sort, int uid) {
        PageHelper.startPage(currPage, pageSize);
        List<ArticleVO> articleList = articleMapper.getUserCollection(searchData, sort, uid);
        for (ArticleVO article : articleList) {
            article.setCover(articleCoverDownloadPath + article.getCover());
        }
        return new PageInfo<>(articleList);
    }

    /**
     * 编辑完成时删除没有选择的文章图片
     *
     * @param imageList 要删除的图片列表
     * @return 是否成功
     */
    @Override
    public boolean deleteArticleImage(String[] imageList) {
        String path = System.getProperty("user.dir") + "/data/qxfly-articleImage/";
        for (String item : imageList) {
            File file = new File(path + item);
            if (file.exists()) {
                articleMapper.removePubImageByImageName(item);
                if (!file.delete()) return false;
            }
        }
        return true;
    }

    /**
     * 根据id获取文章
     *
     * @param aid 文章id
     * @param uid 用户id
     * @return
     */
    @Override
    public ArticleVO getArticleById(Integer aid, Integer uid) {
        Article article = articleMapper.getArticleById(aid);
        article.setCover(articleCoverDownloadPath + article.getCover());
        ArticleVO articleVO = new ArticleVO();
        BeanUtils.copyProperties(article, articleVO);
        if (uid != null) {
            boolean[] articleLikeAndCollection = isArticleLikeAndCollection(aid, uid);
            articleVO.setIsLike(articleLikeAndCollection[0]);
            articleVO.setIsCollection(articleLikeAndCollection[1]);
        }
        return articleVO;
    }

    /**
     * 文章封面上传
     *
     * @param file
     * @return
     */
    @Override
    public Result updateArticleCover(MultipartFile file) {
        //todo 文章封面上传 返回文件名字，而非地址（暂定）
        String path = FilePaths.ARTICLE_COVER_PATH.getPath();
        try {
            String fileName = FileUtils.upload(path, file);
            return Result.success(articleCoverDownloadPath + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("上传失败");
        }
    }

    /**
     * 文章内容图片上传
     *
     * @param file
     * @return
     */
    @Override
    public Result uploadArticleImage(MultipartFile file) {
        String path = FilePaths.ARTICLE_IMAGE_PATH.getPath();
        try {
            String fileName = FileUtils.upload(path, file);
            String[] fileOriginName = file.getOriginalFilename().split("\\.");
            return Result.success(fileOriginName[0], articleImageDownloadPath + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("上传失败");
        }
    }

    /**
     * 删除文章
     *
     * @param aid
     * @return
     */
    @Override
    public boolean deleteArticleById(Integer aid) {
        Article articleById = articleMapper.getArticleById(aid);
        /* 删除封面 */
        String s = articleById.getCover().split("/")[articleById.getCover().split("/").length - 1];
        File cover = new File(System.getProperty("user.dir") + "/data/qxfly-articleCover/" + s);
        if (cover.exists()) cover.delete();
        /* 删除内容图片 */
        String images = articleMapper.getArticleImage(articleById);
        if (images != null) {
            ArrayList<String> arrayList = JSONObject.parseObject(images, ArrayList.class);
            String path = System.getProperty("user.dir") + "/data/qxfly-articleImage/";
            for (String image : arrayList) {
                File file = new File(path + image);
                if (file.exists()) {
                    file.delete();
                }
            }
        }
        /* 删除附件 */
        List<Attachment> attachmentList = articleMapper.getArticleAttachmentByAid(articleById.getId());
        for (Attachment attachment : attachmentList) {
            File file = new File(System.getProperty("user.dir") + "/data/qxfly-articleAttachment/" + attachment.getFileName());
            if (file.exists()) {
                file.delete();
            }
        }
        articleMapper.deleteAllArticleAttachmentByAid(articleById.getId());
        return articleMapper.deleteArticleById(articleById);
    }

    /**
     * 删除之前的封面
     *
     * @param coverUrl
     * @return
     */
    @Override
    public boolean deletePreviousCover(String coverUrl) {
        String[] split = coverUrl.split("/");
        String coverName = split[split.length - 1];
        String path = System.getProperty("user.dir") + "/data/qxfly-articleCover/";
        File cover = new File(path + coverName);
        if (cover.exists()) {
            return cover.delete();
        } else {
            return true;
        }
    }

    /**
     * 分页获取所有分类
     *
     * @return 分页分类列表
     */
    @Override
    public PageInfo<Classify> listClassifiesByPage(int currPage, int pageSize, Integer id, String name) {
        PageHelper.startPage(currPage, pageSize);
        List<Classify> classifyList = articleMapper.listClassifiesByPage(id, name);
        return new PageInfo<>(classifyList);
    }

    /**
     * 获取所有分类
     *
     * @return 分类列表
     */
    @Override
    public List<Classify> listClassifies() {
        return articleMapper.listClassifies();
    }

    /**
     * 分页获取所有标签
     *
     * @param currPage 当前页
     * @param pageSize 每页数量
     * @param id       标签id
     * @param name     标签名称
     * @param uid      标签创建者id
     * @return 分页标签列表
     */
    @Override
    public PageInfo<Tag> listTagsByPage(Integer currPage, Integer pageSize, Integer id, String name, Integer uid) {
        PageHelper.startPage(currPage, pageSize);
        List<Tag> tagList = articleMapper.listTagsByPage(id, name, uid);
        return new PageInfo<>(tagList);
    }

    /**
     * 获取所有标签
     *
     * @return 标签列表
     */
    @Override
    public List<Tag> listTags() {
        return articleMapper.listTags();
    }

    /**
     * 文章点赞
     *
     * @param articleId
     * @return
     */
    @Override
    public boolean articleLike(Integer articleId, Integer uid) {
        Integer al = articleMapper.getUserArticleLike(articleId, uid);

        /*获取用户的点赞json数据*/
        UserLikesAndCollection userLikes = articleMapper.getUserLikes(uid);
        ArrayList<Integer> articles = new ArrayList<>();
        /*如果为空，则创建相关json数据*/
        if (userLikes == null) {
            articles.add(articleId);
            UserLikesAndCollection userLikes1 = new UserLikesAndCollection(uid, JSONObject.toJSONString(articles), JSONObject.toJSONString(""));
            articleMapper.addUserLikes(userLikes1);
        } else {
            //否则查询用户是否点赞
            String likeArticles = userLikes.getLikeArticles();
            ArrayList<Integer> arrayList = new ArrayList<>();
            if (likeArticles != null) {
                arrayList = JSONObject.parseObject(likeArticles, ArrayList.class);

                for (Object item : arrayList) {
                    if (item.equals(articleId)) {
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
            arrayList.add(articleId);
            userLikes.setLikeArticles(JSONObject.toJSONString(arrayList));
            articleMapper.updateUserLikes(userLikes);
            if (al == null || al == 0) {
                articleMapper.addUserArticleLike(articleId, uid);
                articleMapper.articleLike(articleId);
            }

        }
        return true;
    }

    /**
     * 取消用户点赞
     *
     * @param articleId
     * @param uid
     * @return
     */
    @Override
    public boolean cancelArticleLike(Integer articleId, Integer uid) {
        UserLikesAndCollection userLikes = articleMapper.getUserLikes(uid);
        if (userLikes != null && userLikes.getLikeArticles() != null) {
            ArrayList<Integer> arrayList = JSONObject.parseObject(userLikes.getLikeArticles(), ArrayList.class);
            for (Integer item : arrayList) {
                if (item.equals(articleId)) {
                    arrayList.remove(articleId);
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
     * @param articleId
     * @param uid
     */
    @Override
    public boolean articleCollection(Integer articleId, Integer uid) {
        Integer b = articleMapper.userIsCollArt(articleId, uid);
        if (b == null) {
            articleMapper.updateUserCollection(articleId, uid, new Date());
            articleMapper.addarticleCollectionCount(articleId);
        }
        return true;
    }

    /**
     * 取消用户收藏
     *
     * @param articleId
     * @param uid
     * @return
     */
    @Override
    public boolean cencelArticleCollection(Integer articleId, Integer uid) {
        return articleMapper.deleteUserCollection(articleId, uid);
    }

    /**
     * 增加文章访问量
     *
     * @param aid 文章id
     * @param uid 用户id
     * @param UA  用户UA
     */
    @Override
    public void addArticleView(Integer aid, Integer uid, String UA) {

        Integer view = articleMapper.getUserArticleView(aid, uid, UA);
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
                for (Object item : a) {
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

    /**
     * 上传文章附件
     *
     * @param file 文件
     * @return 文件名
     */
    @Override
    public String uploadAttachment(MultipartFile file) {
        String path = FilePaths.ARTICLE_ATTACHMENT_PATH.getPath();
        String fileName;
        try {
            fileName = FileUtils.upload(path, file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return fileName;
//        File filePath = new File(path);
//        if (!filePath.exists()) {
//            filePath.mkdirs();
//        }
//        if (file != null) {
//            /* 把文件从临时文件转存到指定目录 */
//            String uuid = UUID.randomUUID().toString();
//            String[] split = file.getOriginalFilename().split("\\.");
//            String suffix = split[split.length - 1];
//            String fileName = uuid + "." + suffix;
//            try (FileOutputStream fileOutputStream = new FileOutputStream(path + fileName)) {
//                fileOutputStream.write(file.getBytes());
//            } catch (Exception e) {
//                e.printStackTrace();
//                return null;
//            }
//            return fileName;
//        } else {
//            return null;
//        }
    }

    /**
     * 删除文章附件
     *
     * @param aid      文章id
     * @param fileName 文件名
     * @return boolean
     */
    @Override
    public boolean deleteAttachment(Integer aid, String fileName) {
        File file = new File(FilePaths.ARTICLE_ATTACHMENT_PATH.getPath() + fileName);
        if (aid != null && aid != 0) {
            articleMapper.deleteAttachment(aid, fileName);
        }

        return file.exists() && file.delete();
    }

    /**
     * 保存文章附件
     *
     * @param aid            文章id
     * @param uid            用户id
     * @param attachmentList 附件列表
     * @return null
     */
    @Override
    public Integer saveAttachment(Integer aid, Integer uid, List<Attachment> attachmentList) {
        for (Attachment attachment : attachmentList) {
            articleMapper.saveAttachment(aid, uid, attachment);
        }
        return null;
    }

    /**
     * 获取文章附件
     *
     * @param aid 文章id
     * @return 附件列表
     */
    @Override
    public List<Attachment> getArticleAttachment(Integer aid) {
        List<Attachment> articleAttachment = articleMapper.getArticleAttachmentByAid(aid);
        for (Attachment attachment : articleAttachment) {
            attachment.setDownloadUrl(articleAttachmentDownloadPath + attachment.getFileName());
        }
        return articleAttachment;
    }

    /**
     * 批量删除文章
     *
     * @param aidList 文章id列表
     * @return boolean
     */
    @Override
    public boolean batchDeleteArticle(String[] aidList) {
        for (String ar : aidList)
            if (!deleteArticleById(Integer.parseInt(ar)))
                return false;
        return true;
    }
}
