package fun.qxfly.service;

import com.github.pagehelper.PageInfo;
import fun.qxfly.common.domain.DTO.LeaveMessageDTO;
import fun.qxfly.common.domain.entity.LeaveMessage;

public interface LeaveMessageService {
    /**
     * 分页获取留言列表
     *
     * @return 留言列表
     */
    PageInfo<LeaveMessage> listLeaveMessage(LeaveMessageDTO leaveMessageDTO);

    /**
     * 发送留言
     *
     * @param leaveMessage
     * @return
     */
    boolean sendLeaveMessage(LeaveMessage leaveMessage);

    /**
     * 删除留言
     *
     * @param id
     * @return
     */
    boolean deleteLeaveMessage(Integer id);
}
