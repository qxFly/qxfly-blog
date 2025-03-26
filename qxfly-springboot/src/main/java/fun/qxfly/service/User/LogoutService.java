package fun.qxfly.service.User;

import fun.qxfly.common.domain.entity.Token;

public interface LogoutService {
    /**
     * 退出登入
     * @param username 用户名
     * @param uid 用户id
     */
    void logout(String username, Integer uid);

    /**
     * 删除token
     *
     * @param uid 用户id
     */
    void deleteToken(Integer uid);

    /**
     * 获取退出状态信息
     *
     * @param uid 用户id
     */
    String getLogoutStatus(Integer uid);
}
