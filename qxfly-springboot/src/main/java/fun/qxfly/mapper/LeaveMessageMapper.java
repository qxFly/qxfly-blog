package fun.qxfly.mapper;

import fun.qxfly.common.domain.DTO.LeaveMessageDTO;
import fun.qxfly.common.domain.entity.LeaveMessage;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LeaveMessageMapper {

    /**
     * 分页获取留言列表
     *
     * @return 留言列表
     */
    List<LeaveMessage> listLeaveMessage(LeaveMessageDTO leaveMessageDTO);

    /**
     * 发送留言
     *
     * @param leaveMessage
     * @return
     */
    @Insert("insert into leave_message (uid, username, content, date) values (#{uid},#{username}, #{content}, now())")
    boolean sendLeaveMessage(LeaveMessage leaveMessage);

    /**
     * 删除留言
     *
     * @param id
     * @return
     */
    @Delete("delete from leave_message where id = #{id}")
    boolean deleteLeaveMessage(Integer id);
}
