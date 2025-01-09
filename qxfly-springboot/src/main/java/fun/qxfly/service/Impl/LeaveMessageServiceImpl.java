package fun.qxfly.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import fun.qxfly.common.domain.DTO.LeaveMessageDTO;
import fun.qxfly.common.domain.entity.LeaveMessage;
import fun.qxfly.mapper.LeaveMessageMapper;
import fun.qxfly.service.LeaveMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class LeaveMessageServiceImpl implements LeaveMessageService {

    private final LeaveMessageMapper leaveMessageMapper;

    public LeaveMessageServiceImpl(LeaveMessageMapper leaveMessageMapper) {
        this.leaveMessageMapper = leaveMessageMapper;
    }

    /**
     * 分页获取留言列表
     *
     * @return 留言列表
     */
    @Override
    public PageInfo<LeaveMessage> listLeaveMessage(LeaveMessageDTO leaveMessageDTO) {
        PageHelper.startPage(leaveMessageDTO.getCurrPage(), leaveMessageDTO.getPageSize());
        List<LeaveMessage> leaveMessageList = leaveMessageMapper.listLeaveMessage(leaveMessageDTO);
        PageInfo<LeaveMessage> pageInfo = new PageInfo<>(leaveMessageList);
        return pageInfo;
    }

    /**
     * 发送留言
     *
     * @param leaveMessage
     * @return
     */
    @Override
    public boolean sendLeaveMessage(LeaveMessage leaveMessage) {
        return leaveMessageMapper.sendLeaveMessage(leaveMessage);
    }

    /**
     * 删除留言
     *
     * @param id
     * @return
     */
    @Override
    public boolean deleteLeaveMessage(Integer id) {
        return leaveMessageMapper.deleteLeaveMessage(id);
    }
}
