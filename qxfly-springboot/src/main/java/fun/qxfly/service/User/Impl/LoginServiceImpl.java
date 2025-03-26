package fun.qxfly.service.User.Impl;

import fun.qxfly.common.domain.po.Result;
import fun.qxfly.common.enums.ExceptionEnum;
import fun.qxfly.common.exception.excep.UserException;
import fun.qxfly.common.service.RSAService;
import fun.qxfly.common.utils.RSAEncrypt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fun.qxfly.common.domain.entity.Token;
import fun.qxfly.common.domain.entity.User;
import fun.qxfly.mapper.User.LoginMapper;
import fun.qxfly.service.User.LoginService;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {
    private final LoginMapper loginMapper;
    private final RSAService rsaService;
    public LoginServiceImpl(LoginMapper loginMapper, RSAService rsaService) {
        this.loginMapper = loginMapper;
        this.rsaService = rsaService;
    }

    /**
     * 登陆校验
     *
     * @param user
     * @return
     */
    @Override
    public User login(User user) {
        /*判断是否为手机号*/
        boolean isPhone = user.getUsername().matches("^[0-9]*$");
        if (isPhone) {
            user.setPhone(user.getUsername());
        }
        String encodePassword = user.getPassword();
        String decodePassword;
        String privateKey = rsaService.getPrivateKey(user.getSalt());
        try {
            decodePassword = RSAEncrypt.decrypt(encodePassword, privateKey);
        } catch (Exception e) {
            throw new UserException(ExceptionEnum.USER_LOGIN_EXPIRED);
        }
        user.setPassword(decodePassword);
        return loginMapper.login(user);
    }

    /**
     * 查询用户是否有token
     *
     * @param user
     * @return
     */
    @Override
    public Token getTokenByUser(User user) {
        return loginMapper.getTokenByUser(user);
    }

    /**
     * 设置用户的token
     *
     * @param username
     * @param newToken
     */
    @Override
    public void setToken(String username, String newToken, long createDate) {
        loginMapper.setTokenByUser(username, newToken, createDate);
    }

    /**
     * 删除用户token
     *
     * @param token
     */
    @Override
    public void deleteToken(Token token) {
        loginMapper.deleteToken(token);
    }

    /**
     * 更新用户token
     *
     * @param username
     * @param newJwt
     */
    @Override
    public void updateToken(String username, String newJwt, long nowDate) {
        loginMapper.updateJwt(username, newJwt, nowDate);
    }

    /**
     * 获取token创建时间
     *
     * @param jwt
     * @return
     */
    @Override
    public Long getTokenCreateTime(Token jwt) {
        return loginMapper.getJwtCreateTime(jwt);
    }

}
