package fun.qxfly.service.User;

import com.github.pagehelper.PageInfo;
import fun.qxfly.common.domain.entity.Navigation;
import fun.qxfly.common.domain.entity.User;
import fun.qxfly.common.domain.po.Result;
import fun.qxfly.common.domain.vo.UserVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserInfoService {
    /**
     * 根据uid获取用户
     *
     * @param uid uid
     * @return 用户对象
     */
    User getUserInfo(Integer uid);

    /**
     * 更改用户信息
     *
     * @param user 新的用户对象
     * @return true or false
     */
    boolean updateUserInfo(User user);

    /**
     * 检查用户名是否可行
     *
     * @param user 用户对象
     * @return 可行为空，不可行返回已存在用户
     */
    User checkUsername(User user);

    /**
     * 头像上传
     *
     * @param file 上传的文件
     * @return Result对象
     */
    Result updateAvatar(MultipartFile file, Integer uid);

    /**
     * 获取推荐作者
     *
     * @param currPage 当前页
     * @param pageSize 分页大小
     * @return 推荐作者列表
     */
    PageInfo<UserVO> getSuggestAuthorByPage(Integer currPage, Integer pageSize);

    /**
     * 找回密码
     *
     * @param phone    手机
     * @param password 密码
     * @param code     验证码
     * @return 1：验证码正确。-1：验证码不存在。0：验证码错误。
     */
    Integer resetPassword(String phone, String password, Integer code);

    /**
     * 发送验证码
     *
     * @param user 用户对象
     * @return 成功返回验证码，失败返回-1
     */
    int sendCode(User user);


    /**
     * 检测验证码
     *
     * @param user phone:手机号。role:验证码
     * @return 1：验证码正确。-1：验证码不存在。0：验证码错误。
     */
    int testCode(User user);

    /**
     * 获取用户原手机号
     *
     * @param uid uid
     * @return 手机号
     */
    String getOriginPhone(Integer uid);

    /**
     * 获取用户空间导航栏
     *
     * @param uid uid
     * @return 导航栏列表
     */
    List<Navigation> listUserSpaceNav(Integer uid);
}
