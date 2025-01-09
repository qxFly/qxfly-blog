package fun.qxfly.admin.service.impl;

import fun.qxfly.admin.mapper.AdminMapper;
import fun.qxfly.admin.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {
    private final AdminMapper adminMapper;

    public AdminServiceImpl(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }

    /**
     * 判断用户是否为管理员
     *
     * @param uid
     * @return
     */
    @Override
    public Integer check(Integer uid) {
        return adminMapper.check(uid);
    }


}
