package fun.qxfly.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import fun.qxfly.admin.mapper.UserManageMapper;
import fun.qxfly.admin.service.UserManageService;
import fun.qxfly.common.domain.entity.User;
import fun.qxfly.common.domain.entity.UserSetting;
import fun.qxfly.common.domain.vo.UserSettingVO;
import fun.qxfly.common.enums.FilePaths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class UserManageServiceImpl implements UserManageService {
    private final UserManageMapper userManageMapper;

    public UserManageServiceImpl(UserManageMapper userManageMapper) {
        this.userManageMapper = userManageMapper;
    }

    @Value("${qxfly.file.path.userImg}")
    private String userAvatarDownloadPath;
    @Value("${qxfly.file.path.userBgImg}")
    String bgImgDownloadPath;

    /**
     * 根据条件列出用户
     *
     * @param user
     * @return
     */
    @Override
    public PageInfo<User> listUser(Integer currPage, Integer pageSize, User user) {
        PageHelper.startPage(currPage, pageSize);
        List<User> userList = userManageMapper.listUser(user);
        for (User user1 : userList) {
            if (user1.getAvatar() != null && !user1.getAvatar().isEmpty()) {
                user1.setAvatar(userAvatarDownloadPath + user1.getAvatar());}}
        return new PageInfo<>(userList);
    }

    /**
     * 删除用户头像
     *
     * @param user
     * @return
     */
    @Override
    public boolean deleteUserAvatar(User user) {
        String[] split = user.getAvatar().split("/");
        String s = split[split.length - 1];
        File file = new File(System.getProperty("user.dir") + "/data/qxfly-userAvatar/" + s);
        boolean delete = file.delete();
        if (delete) {
            return userManageMapper.deleteUserAvatar(user);
        } else {
            return false;
        }

    }

    /**
     * 列出用户设置
     *
     * @param currPage 当前页
     * @param pageSize 页大小
     * @param uid      用户id
     * @param username 用户名
     * @return 分页
     */
    @Override
    public PageInfo<UserSettingVO> listUserSetting(Integer currPage, Integer pageSize, Integer uid, String username) {
        PageHelper.startPage(currPage, pageSize);
        List<UserSettingVO> userSettingList = userManageMapper.listUserSetting(uid, username);
        for (UserSettingVO setting : userSettingList) {
            if (setting.getBgImgPath() != null && !setting.getBgImgPath().isEmpty()) {
                setting.setBgImgPath(bgImgDownloadPath + setting.getBgImgPath());
            }
        }
        return new PageInfo<>(userSettingList);
    }

    /**
     * 编辑用户设置信息
     *
     * @param userSetting 用户设置信息
     * @return
     */
    @Override
    public boolean editUserSetting(UserSetting userSetting) {
        String bgImgPath = userSetting.getBgImgPath();
        if (bgImgPath != null && !bgImgPath.isEmpty()) {
            String[] split = bgImgPath.split("userBgImg/");
            userSetting.setBgImgPath(split[1]);
        }
        return userManageMapper.editUserSetting(userSetting);
    }

    /**
     * 根据id删除用户
     *
     * @param id
     * @return
     */
    @Override
    public boolean deleteUserById(Integer id) {
        return userManageMapper.deleteUserById(id);
    }

    /**
     * 更改用户信息
     *
     * @param user
     * @return
     */
    @Override
    public boolean editUserInfo(User user) {
        User u = userManageMapper.getUserById(user.getId());
        if (!user.getUsername().equals(u.getUsername()) || !user.getRole().equals(u.getRole())) {
            userManageMapper.remoUserToken(u.getUsername());
        }
        return userManageMapper.editUserInfo(user);
    }
}
