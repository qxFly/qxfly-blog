package fun.qxfly.service.User;

import fun.qxfly.common.domain.entity.User;

public interface RegisterService {

    /**
     * 用户注册
     *
     * @param user
     */
    void register(User user);

    /**
     * 检测用户名是否被注册
     *
     * @param user
     * @return
     */
    User checkUsername(String user);

    /**
     * 检测手机是否被注册
     *
     * @param user
     * @return
     */
    User checkPhone(String user);
}
