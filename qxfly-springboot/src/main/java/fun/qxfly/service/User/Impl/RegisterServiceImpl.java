package fun.qxfly.service.User.Impl;

import fun.qxfly.common.domain.entity.User;
import fun.qxfly.mapper.User.RegisterMapper;
import fun.qxfly.service.User.RegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private RegisterMapper registerMapper;

    /**
     * 用户注册
     *
     * @param user
     */
    @Override
    public void register(User user) {
        registerMapper.register(user);
        registerMapper.createUserInfo(user);
    }

    /**
     * 检测用户名是否被注册
     *
     * @param user
     * @return
     */
    @Override
    public User checkUsername(String user) {
        return registerMapper.findUserByUsername(user);
    }

    /**
     * 检测手机是否被注册
     *
     * @param user
     * @return
     */
    @Override
    public User checkPhone(String user) {
        return registerMapper.findUserByPhone(user);
    }
}
