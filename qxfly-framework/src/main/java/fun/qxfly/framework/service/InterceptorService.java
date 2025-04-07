package fun.qxfly.framework.service;

import fun.qxfly.common.domain.entity.User;

public interface InterceptorService {
    /**
     * 检查用户是否过期
     *
     * @param user 用户
     * @return
     */
    boolean isExpirationUser(User user);
}
