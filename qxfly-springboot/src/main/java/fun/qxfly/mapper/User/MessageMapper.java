package fun.qxfly.mapper.User;

import fun.qxfly.common.domain.entity.Message;
import fun.qxfly.common.domain.vo.MessageNavVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MessageMapper {
    /**
     * 发送消息
     *
     * @param message
     * @return
     */
    @Insert("insert into message (toUid, fromUid, content, sendTime, msgId) values(#{toUid}, #{fromUid}, #{content}, #{sendTime}, #{msgId})")
    boolean sendMessage(Message message);

    /**
     * 读取消息
     *
     * @param message
     * @return
     */
    @Update("update message set status = 1 where msgId = #{msgId} and toUid = #{toUid}")
    boolean readMessage(Message message);

    /**
     * 获取指定用户发送的消息
     *
     * @param msgId
     * @param size
     * @return
     */
    @Select("select * from message where msgId = #{msgId} order by sendTime limit #{size}")
    List<Message> listMessage(@Param("msgId") String msgId, @Param("size") Integer size);

    /**
     * 获取给当前用户发送的消息的用户
     *
     * @param uid
     * @param size
     * @return
     */
    @Select("select id, username, avatar from user where (id in (select distinct toUid from message where fromUid = #{uid} order by sendTime desc)) or (id in (select distinct fromUid from message where toUid = #{uid} order by sendTime desc)) limit #{size}")
    List<MessageNavVO> listMessageUserNav(@Param("uid") Integer uid, @Param("size") Integer size);

    /**
     * 获取对话id
     *
     * @param uid
     * @param fromUid
     * @return
     */
    @Select("select distinct msgId from message where (fromUid = #{uid} and toUid = #{fromUid} )or (toUid = #{uid} and fromUid = #{fromUid})")
    String getMsgId(@Param("uid") Integer uid, @Param("fromUid") Integer fromUid);

    /**
     * 初始化消息对话
     *
     * @param uid
     * @return
     */
    @Select("select id, username, avatar from user where id = #{uid}")
    MessageNavVO initUserMessage(Integer uid);

    /**
     * 根据对话id获取未读消息数量
     *
     * @param msgId
     * @return
     */
    @Select("select count(*) from message where msgId = #{msgId} and toUid = #{uid} and status = 0")
    Integer getNoReadCountByMsgId(@Param("uid") Integer uid, @Param("msgId") String msgId);

    /**
     * 根据用户id查看是否有未读消息
     *
     * @param uid
     * @return
     */
    @Select("select count(*) from message where toUid = #{uid} and status = 0")
    Integer getNoReadCountByUid(Integer uid);
}
