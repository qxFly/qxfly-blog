package fun.qxfly.admin.service;

import com.github.pagehelper.PageInfo;
import fun.qxfly.common.domain.entity.User;
import fun.qxfly.common.domain.entity.UserSetting;
import fun.qxfly.common.domain.vo.UserSettingVO;

public interface UserManageService {
    /**
     * 根据id删除用户
     *
     * @param id
     * @return
     */
    boolean deleteUserById(Integer id);

    /**
     * 编辑用户信息
     *
     * @param user
     * @return
     */
    boolean editUserInfo(User user);

    /**
     * 根据条件列出用户
     *
     * @param user
     * @return
     */
    PageInfo<User> listUser(Integer currPage, Integer pageSize, User user);

    /**
     * 删除用户头像
     *
     * @param user
     * @return
     */
    boolean deleteUserAvatar(User user);

    /**
     * 列出用户设置
     *
     * @param currPage 当前页
     * @param pageSize 页大小
     * @param uid      用户id
     * @param username 用户名
     * @return 分页
     */
    PageInfo<UserSettingVO> listUserSetting(Integer currPage, Integer pageSize, Integer uid, String username);

    /**
     * 编辑用户设置信息
     *
     * @param userSetting 用户设置信息
     * @return
     */
    boolean editUserSetting(UserSetting userSetting);

    /**
     * 删除用户背景
     *
     * @param uid    用户id
     * @param bgPath 背景路径
     */
    boolean deleteUserBackground(Integer uid, String bgPath);
}
