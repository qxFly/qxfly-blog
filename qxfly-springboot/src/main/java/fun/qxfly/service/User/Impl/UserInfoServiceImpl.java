package fun.qxfly.service.User.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import fun.qxfly.admin.service.AdminService;
import fun.qxfly.common.domain.entity.Navigation;
import fun.qxfly.common.domain.entity.User;
import fun.qxfly.common.domain.po.Result;
import fun.qxfly.common.domain.vo.UserVO;
import fun.qxfly.common.enums.FilePaths;
import fun.qxfly.common.utils.AliyunDysmsapi;
import fun.qxfly.common.utils.FileUtils;
import fun.qxfly.mapper.User.UserInfoMapper;
import fun.qxfly.service.User.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class UserInfoServiceImpl implements UserInfoService {

    private final UserInfoMapper userInfoMapper;

    private final AliyunDysmsapi aliyunDysmsapi;

    private final AdminService adminService;

    @Value("${qxfly.file.path.userImg}")
    private String userAvatarPath;

    public UserInfoServiceImpl(UserInfoMapper userInfoMapper, AliyunDysmsapi aliyunDysmsapi, AdminService adminService) {
        this.userInfoMapper = userInfoMapper;
        this.aliyunDysmsapi = aliyunDysmsapi;
        this.adminService = adminService;
    }

    /**
     * 根据uid获取用户
     *
     * @param uid uid
     * @return 用户对象
     */
    @Override
    public User getUserInfo(Integer uid) {
        User user = userInfoMapper.getUserInfo(uid);
        if (user != null && user.getAvatar() != null) {
            String path = userAvatarPath + user.getAvatar();
            path = FileUtils.toUrlSeparator(path);
            user.setAvatar(path);
        }
        return user;
    }

    /**
     * 更改用户信息
     *
     * @param user 新的用户对象
     * @return true or false
     */
    @Override
    public boolean updateUserInfo(User user) {
        return userInfoMapper.updateUserInfo(user);
    }

    /**
     * 检查用户名是否可行
     *
     * @param user 用户对象
     * @return 可行为空，不可行返回已存在用户
     */
    @Override
    public User checkUsername(User user) {
        return userInfoMapper.checkUsername(user);
    }

    /**
     * 头像上传
     *
     * @param file 上传的文件
     * @return Result对象
     */
    @Override
    public Result updateAvatar(MultipartFile file, Integer uid) {
        String path = FilePaths.USER_AVATAR_PATH.getPath();
        User user = userInfoMapper.getUserInfo(uid);
        /* 先删除原来的头像 */
        String ava = user.getAvatar();
        if (ava != null) {
            File avatarDelete = new File(path + File.separator + ava);
            boolean delete = avatarDelete.delete();
        }
        try {
            String fileName = FileUtils.upload(path, file);
            userInfoMapper.updateImg(fileName, user.getId());
            return Result.success(userAvatarPath + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("上传失败");
        }
    }

    /**
     * 获取推荐作者
     *
     * @param currPage 当前页
     * @param pageSize 分页大小
     * @return 推荐作者列表
     */
    @Override
    public PageInfo<UserVO> getSuggestAuthorByPage(Integer currPage, Integer pageSize) {
        PageHelper.startPage(currPage, pageSize);
        List<UserVO> userList = userInfoMapper.getSuggestAuthor();
        for (UserVO userVO : userList) {
            if (userVO.getAvatar() != null)
                userVO.setAvatar(userAvatarPath + userVO.getAvatar());
        }
        return new PageInfo<>(userList);
    }

    /**
     * 发送验证码
     *
     * @param user 用户对象
     * @return 成功返回验证码，失败返回-1
     */
    @Override
    public int sendCode(User user) {
        int code = aliyunDysmsapi.sendCode(user.getPhone());
        return code == -1 ? -1 : userInfoMapper.captcha(user.getPhone(), code, new Date().getTime());
    }

    /**
     * 检测验证码
     *
     * @param user phone:手机号。role:验证码
     * @return 1：验证码正确。-1：验证码不存在。0：验证码错误。
     */
    @Override
    public int testCode(User user) {
        return aliyunDysmsapi.testCode(user.getPhone(), user.getRole());
    }

    /**
     * 获取用户原手机号
     *
     * @param uid uid
     * @return 手机号
     */
    @Override
    public String getOriginPhone(Integer uid) {
        return userInfoMapper.getOriginPhone(uid);
    }

    /**
     * 获取用户空间导航栏
     *
     * @param uid uid
     * @return 导航栏列表
     */
    @Override
    public List<Navigation> listUserSpaceNav(Integer uid) {
        List<Navigation> navigations;
        Integer role = adminService.check(uid);
        if (role != 0) role = 5;
        navigations = userInfoMapper.listUserSpaceNav(role);
        navigations.sort(Comparator.comparingInt(Navigation::getIndex));
        return navigations;
    }

    /**
     * 找回密码
     *
     * @param phone    手机
     * @param password 密码
     * @param code     验证码
     * @return 1：验证码正确。-1：验证码不存在。0：验证码错误。
     */
    @Override
    public Integer resetPassword(String phone, String password, Integer code) {
        int c = aliyunDysmsapi.testCode(phone, code);
        if (c == 0) {
            return 0;
        } else if (c == -1) {
            return -1;
        } else {
            return userInfoMapper.resetPassword(phone, password);
        }
    }
}
