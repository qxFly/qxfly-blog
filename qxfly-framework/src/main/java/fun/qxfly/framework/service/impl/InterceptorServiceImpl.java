package fun.qxfly.framework.service.impl;

import fun.qxfly.framework.mapper.InterceptorMapper;
import fun.qxfly.framework.service.InterceptorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InterceptorServiceImpl implements InterceptorService {
    final InterceptorMapper interceptorMapper;

    public InterceptorServiceImpl(InterceptorMapper interceptorMapper) {
        this.interceptorMapper = interceptorMapper;
    }

    /**
     * 获取退出状态信息
     *
     * @param token
     */
    @Override
    public String getLogoutStatus(String token) {
        return interceptorMapper.getLogoutStatusByToken(token);
    }

    /**
     * 判断用户是否为管理员
     *
     * @param username
     * @return
     */
    @Override
    public boolean isAdmin(String username) {
       return interceptorMapper.isAdmin(username) != 0;
    }
}
