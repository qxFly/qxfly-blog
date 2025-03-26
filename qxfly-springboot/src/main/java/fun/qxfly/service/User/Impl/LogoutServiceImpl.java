package fun.qxfly.service.User.Impl;

import fun.qxfly.common.domain.entity.Token;
import fun.qxfly.mapper.User.LogoutMapper;
import fun.qxfly.service.User.LogoutService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LogoutServiceImpl implements LogoutService {
    @Autowired
    private LogoutMapper logoutMapper;

    @Override
    public void logout(String username, Integer uid) {
        logoutMapper.addTokenToBlack(username,uid);
    }

    /**
     * 删除token
     *
     * @param uid 用户id
     */
    @Override
    public void deleteToken(Integer uid) {
        logoutMapper.deleteToken(uid);
    }

    /**
     * 获取退出状态信息
     *
     * @param uid 用户id
     */
    @Override
    public String getLogoutStatus(Integer uid) {
        return logoutMapper.getLogoutStatusByToken(uid);
    }

}
