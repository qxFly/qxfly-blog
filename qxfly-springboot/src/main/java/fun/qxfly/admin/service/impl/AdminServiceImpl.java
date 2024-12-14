package fun.qxfly.admin.service.impl;

import fun.qxfly.admin.service.AdminService;
import fun.qxfly.admin.mapper.AdminMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminMapper adminMapper;

    /**
     * 判断用户是否为管理员
     *
     * @param username
     * @return
     */
    @Override
    public Integer check(String username) {
        return adminMapper.check(username);
    }



}
