package fun.qxfly.framework.service.impl;

import fun.qxfly.common.domain.entity.User;
import fun.qxfly.framework.mapper.InterceptorMapper;
import fun.qxfly.framework.service.InterceptorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InterceptorServiceImpl implements InterceptorService {
    private final InterceptorMapper interceptorMapper;

    public InterceptorServiceImpl(InterceptorMapper interceptorMapper) {
        this.interceptorMapper = interceptorMapper;
    }

    /**
     * 检查用户是否过期
     *
     * @param user 用户
     * @return
     */
    @Override
    public boolean isExpirationUser(User user) {
        String username = interceptorMapper.isExpirationUser(user);
        if (username != null) {
            log.info("过期");
            interceptorMapper.removeExpirationUser(user);
            return true;
        }
        return false;
    }

    /**
     * 检查用户是否为管理员或审核员
     *
     * @param username 用户名
     * @return true=有后台权限
     */
    @Override
    public boolean isAdmin(String username) {
        Integer role = interceptorMapper.isAdmin(username);
        // role 为 null（用户不存在）或 0（普通用户）均无后台权限
        return role != null && role != 0;
    }
}
