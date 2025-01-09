package fun.qxfly.service.User;

import fun.qxfly.common.domain.entity.Token;

public interface LogoutService {
    /**
     * 退出登入
     *
     * @param token token
     */
    void logout(Token token);

    /**
     * 删除token
     *
     * @param token token
     */
    void deleteToken(String token);

    /**
     * 获取退出状态信息
     *
     * @param token token
     */
    String getLogoutStatus(String token);
}
