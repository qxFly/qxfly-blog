package fun.qxfly.service.User.Impl;

import fun.qxfly.common.domain.entity.Message;
import fun.qxfly.common.domain.vo.MessageNavVO;
import fun.qxfly.mapper.User.MessageMapper;
import fun.qxfly.service.User.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class MessageServiceImpl implements MessageService {

    @Value("${qxfly.file.path.userImg}")
    private String userAvatarPath;

    private final MessageMapper messageMapper;

    public MessageServiceImpl(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    /**
     * 发送消息
     *
     * @param message 消息对象
     * @return 成功返回msgId, 失败返回null
     */
    @Override
    public String sendMessage(Message message) {
        String msgId;
        if (message.getMsgId() == null || message.getMsgId().isEmpty()) {
            msgId = messageMapper.getMsgId(message.getToUid(), message.getFromUid());
            if (msgId == null || msgId.isEmpty()) {
                msgId = UUID.randomUUID().toString();
            }
            message.setMsgId(msgId);
        } else {
            msgId = message.getMsgId();
        }
        message.setSendTime(new Date());
        boolean b = messageMapper.sendMessage(message);
        if (b) return msgId;
        else return null;
    }

    /**
     * 读取消息
     *
     * @param message
     * @return
     */
    @Override
    public boolean readMessage(Message message) {
        return messageMapper.readMessage(message);
    }

    /**
     * 获取指定用户发送的消息
     *
     * @param msgId
     * @param size
     * @return
     */
    @Override
    public List<Message> listMessage(String msgId, Integer size) {
        return messageMapper.listMessage(msgId, size);
    }

    /**
     * 获取给当前用户发送的消息的用户
     *
     * @param uid
     * @param size
     * @return
     */
    @Override
    public List<MessageNavVO> listMessageUserNav(Integer uid, Integer size) {
        List<MessageNavVO> userList = messageMapper.listMessageUserNav(uid, size);
        for (MessageNavVO user : userList) {
            if (user.getAvatar() != null)
                user.setAvatar(userAvatarPath + user.getAvatar());
            String msgId = messageMapper.getMsgId(uid, user.getId());
            Integer c = messageMapper.getNoReadCountByMsgId(uid, msgId);
            user.setNoReadCount(c);
            user.setMsgId(msgId);
        }
        return userList;
    }

    /**
     * 初始化消息对话
     *
     * @param uid
     * @return
     */
    @Override
    public MessageNavVO initUserMessage(Integer uid) {
        MessageNavVO message = messageMapper.initUserMessage(uid);
        if (message.getAvatar() != null)
            message.setAvatar(userAvatarPath + message.getAvatar());
        return message;
    }

    @Override
    public Integer getNoReadMessageCount(Integer uid) {
        return messageMapper.getNoReadCountByUid(uid);
    }
}
