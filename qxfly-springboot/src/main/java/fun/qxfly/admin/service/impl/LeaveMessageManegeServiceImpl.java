package fun.qxfly.admin.service.impl;

import fun.qxfly.admin.mapper.LeaveMessageManegeMapper;
import fun.qxfly.admin.service.LeaveMessageManegeService;
import fun.qxfly.common.domain.entity.LeaveMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LeaveMessageManegeServiceImpl implements LeaveMessageManegeService {
    private final LeaveMessageManegeMapper leaveMessageManegeMapper;

    public LeaveMessageManegeServiceImpl(LeaveMessageManegeMapper leaveMessageManegeMapper) {
        this.leaveMessageManegeMapper = leaveMessageManegeMapper;
    }
}
