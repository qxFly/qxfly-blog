package fun.qxfly.service.User;

import fun.qxfly.common.domain.entity.Token;
import fun.qxfly.common.domain.entity.User;

public interface LoginService {
    /**
     * 登陆校验
     *
     * @param user
     * @return
     */
    User login(User user);

    /**
     * 查询用户是否有token
     *
     * @param user
     * @return
     */
    Token getTokenByUser(User user);

    /**
     * 设置用户的token
     *
     * @param username
     * @param newToken
     */
    void setToken(String username, String newToken, long createDate);

    /**
     * 删除用户token
     *
     * @param token
     */
    void deleteToken(Token token);

    /**
     * 更新用户token
     *
     * @param username
     * @param newToken
     */
    void updateToken(String username, String newToken, long nowDate);

    /**
     * 获取token创建时间
     *
     * @param token
     * @return
     */
    Long getTokenCreateTime(Token token);

}
