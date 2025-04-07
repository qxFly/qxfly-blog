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
}
