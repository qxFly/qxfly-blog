package fun.qxfly.service.User;

import fun.qxfly.common.domain.entity.Token;

public interface LogoutService {
    /**
     * 退出登入
     *
     * @param token
     */
    void logout(Token token);

    /**
     * 删除token
     *
     * @param jwt
     */
    void deleteToken(Token jwt);

    /**
     * 获取退出状态信息
     *
     * @param token
     */
    String getLogoutStatus(String token);
}
