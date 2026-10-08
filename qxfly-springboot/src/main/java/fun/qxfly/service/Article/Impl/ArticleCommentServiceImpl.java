package fun.qxfly.service.Article.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import fun.qxfly.common.domain.entity.Comment;
import fun.qxfly.common.domain.entity.Message;
import fun.qxfly.common.domain.entity.User;
import fun.qxfly.controller.Message.WebSocketServer;
import fun.qxfly.mapper.Article.ArticleCommentMapper;
import fun.qxfly.service.Article.ArticleCommentService;
import fun.qxfly.service.User.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ArticleCommentServiceImpl implements ArticleCommentService {
    @Value("${qxfly.file.path.userImg}")
    private String userAvatarPath;

    /**
     * 敏感词预编译，子串匹配
     */
    private static final Pattern SENSITIVE_WORDS = Pattern.compile("sb|王八蛋|卖淫|嫖娼|赌博|吸食|毒品|装逼|草泥马|特么的|撕逼|玛勒戈壁|爆菊|JB|呆逼|本屌|齐B短裙|法克鱿|丢你老母|吉跋猫|妈蛋|逗比|我靠|碧莲|碧池|然并卵|日了狗|屁民|吃翔|你老母|达菲鸡|装13|逼格|蛋疼|傻逼|绿茶婊|你妈的|表砸|屌爆了|买了个表|淫家|你妹|浮尸国|滚粗");

    /**
     * 系统消息账号
     */
    private static final int SYSTEM_UID = 4;

    private final ArticleCommentMapper articleCommentMapper;
    private final MessageService messageService;

    public ArticleCommentServiceImpl(ArticleCommentMapper articleCommentMapper, MessageService messageService) {
        this.articleCommentMapper = articleCommentMapper;
        this.messageService = messageService;
    }

    /**
     * 评论点赞
     *
     * @param comment 评论
     * @param user    用户
     * @return 1 为点赞，0 为取消点赞
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer likeComment(Comment comment, User user) {
        if (comment == null || comment.getId() == null || comment.getArticleId() == null || comment.getArticleId() <= 0) {
            throw new IllegalArgumentException("参数错误");
        }
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("请先登录");
        }
        Comment db = articleCommentMapper.getCommentById(comment.getId());
        if (db == null) {
            throw new IllegalArgumentException("评论不存在或已删除");
        }
        comment.setArticleId(db.getArticleId());

        Integer removed = articleCommentMapper.deleteUserCommentLike(user, comment);
        if (removed != null && removed > 0) {
            // 已点赞过，本次为取消
            articleCommentMapper.reduceCommentLike(comment);
            return 0;
        }
        try {
            articleCommentMapper.addUserCommentLike(user, comment);
        } catch (DuplicateKeyException e) {
            // 并发下已点赞，保持已点赞状态
            return 1;
        }
        articleCommentMapper.addCommentLike(comment);
        return 1;
    }

    /**
     * 获取用户点赞的评论
     *
     * @param aid
     * @param uid
     * @return
     */
    @Override
    public List<Integer> getUserLikeComment(Integer aid, int uid) {
        return articleCommentMapper.getUserLikeComment(aid, uid);
    }

    /**
     * 删除评论及子评论
     *
     * @param cid
     * @param uid 当前登录用户id
     * @return 删除条数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteComment(Integer cid, Integer uid) {
        Comment db = articleCommentMapper.getCommentById(cid);
        if (db == null) {
            return 0;
        }
        if (db.getUser() == null || !db.getUser().getId().equals(uid)) {
            throw new IllegalArgumentException("无权删除该评论");
        }
        return articleCommentMapper.deleteCommentWithChildren(cid);
    }

    /**
     * 根据文章id获取评论
     *
     * @param id
     * @return
     */
    @Override
    public PageInfo<Comment> getArticleCommentsByPage(int currPage, int pageSize, String sort, int id) {
        PageHelper.startPage(currPage, pageSize);
        List<Comment> comments = articleCommentMapper.getArticleCommentsByPage(sort, id);
        if (comments != null && !comments.isEmpty()) {
            List<Integer> rootIds = comments.stream().map(Comment::getId).collect(Collectors.toList());
            List<Comment> children = articleCommentMapper.getChildCommentsByParentIds(rootIds);
            Map<Integer, List<Comment>> childMap = children.stream()
                    .collect(Collectors.groupingBy(Comment::getParentCommentId));
            for (Comment comment : comments) {
                /*设置子评论（批量查询，无子评论时置空列表）*/
                List<Comment> child = childMap.getOrDefault(comment.getId(), new ArrayList<>());
                for (Comment childComment : child) {
                    setAvatar(childComment);
                }
                comment.setChildComment(child);
                setAvatar(comment);
            }
        }
        return new PageInfo<>(comments);
    }

    /**
     * 发布评论
     *
     * @param comment
     * @return true 已发布，false 敏感词命中待审核
     */
    @Override
    public boolean releaseComment(Comment comment) {
        /*参数校验*/
        if (comment.getContent() == null || comment.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("评论内容不能为空");
        }
        String content = comment.getContent().trim();
        if (content.length() > 255) {
            throw new IllegalArgumentException("评论内容不能超过255字");
        }
        comment.setContent(content);
        if (comment.getArticleId() == null || comment.getArticleId() <= 0) {
            throw new IllegalArgumentException("参数错误");
        }
        if (comment.getUser() == null || comment.getUser().getId() == null) {
            throw new IllegalArgumentException("请先登录");
        }
        if (articleCommentMapper.existsArticle(comment.getArticleId()) == 0) {
            throw new IllegalArgumentException("文章不存在或已删除");
        }

        int parent = comment.getParentCommentId() == null ? 0 : comment.getParentCommentId();
        comment.setParentCommentId(parent);
        if (parent > 0) {
            /*回复：校验父评论存在且属于同一篇文章，并补充被回复人信息*/
            Comment p = articleCommentMapper.getCommentById(parent);
            if (p == null) {
                throw new IllegalArgumentException("回复的评论不存在或已删除");
            }
            if (!p.getArticleId().equals(comment.getArticleId())) {
                throw new IllegalArgumentException("参数错误");
            }
            if (comment.getToUserId() == null || comment.getToUserId() <= 0) {
                if (p.getUser() != null) {
                    comment.setToUserId(p.getUser().getId());
                    comment.setToUsername(p.getUser().getUsername());
                }
            }
            if (comment.getToUserId() == null || comment.getToUserId() <= 0) {
                throw new IllegalArgumentException("参数错误");
            }
        } else {
            comment.setToUserId(null);
            comment.setToUsername(null);
        }

        /*敏感词命中转待审核，其余直接发布*/
        boolean pass = !SENSITIVE_WORDS.matcher(content).find();
        comment.setVerify(pass ? 3 : 1);
        articleCommentMapper.releaseComment(comment);

        /*发布成功后给被回复人发送站内通知并实时推送*/
        if (pass && parent > 0) {
            notifyReply(comment);
        }
        return pass;
    }

    /**
     * 回复通知（系统账号发送，失败不影响评论发布）
     *
     * @param comment 已发布的评论
     */
    private void notifyReply(Comment comment) {
        try {
            Integer toUid = comment.getToUserId();
            if (toUid == null || toUid.equals(comment.getUser().getId())) {
                // 不给自己发通知
                return;
            }
            String preview = comment.getContent().replace('\n', ' ').replace('\r', ' ');
            if (preview.length() > 50) {
                preview = preview.substring(0, 50) + "…";
            }
            Message message = new Message();
            message.setContent("用户 " + comment.getUser().getUsername() + " 回复了你的评论：" + preview);
            message.setToUid(toUid);
            message.setFromUid(SYSTEM_UID);
            // msgId、sendTime 由 sendMessage 内部生成
            messageService.sendMessage(message);
            WebSocketServer.pushMessage(message);
        } catch (Exception e) {
            log.warn("回复通知发送失败 commentId={}", comment.getId(), e);
        }
    }

    /**
     * 补全头像路径
     *
     * @param comment 评论
     */
    private void setAvatar(Comment comment) {
        User user = comment.getUser();
        if (user != null && user.getAvatar() != null) {
            user.setAvatar(userAvatarPath + user.getAvatar());
        }
    }
}
