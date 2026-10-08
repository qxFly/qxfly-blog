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

    /**
     * 检查用户是否为管理员或审核员（角色不为普通用户）
     *
     * @param username 用户名
     * @return true=有后台权限
     */
    boolean isAdmin(String username);
}
