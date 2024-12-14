package fun.qxfly.service.User;

import com.github.pagehelper.PageInfo;
import fun.qxfly.common.domain.po.Result;
import org.springframework.web.multipart.MultipartFile;
import fun.qxfly.common.domain.entity.Token;
import fun.qxfly.common.domain.entity.User;
import fun.qxfly.common.domain.vo.UserVO;

public interface UserInfoService {
    /**
     * 根据Token获取用户
     *
     * @param uid
     * @return
     */
    User getUserInfo(Integer uid);

    /**
     * 更改用户信息
     *
     * @param user
     * @return
     */
    boolean updateUserInfo(User user);

    /**
     * 检查用户名是否可行
     *
     * @param user
     * @return
     */
    User checkUsername(User user);

    /**
     * 头像上传
     *
     * @param file
     * @return
     */
    Result updateAvatar(MultipartFile file, Integer uid);

//    /**
//     * 刷新用户信息
//     *
//     * @param token
//     * @return
//     */
//    boolean refreshUserInfoTask(Token token);


    /**
     * 获取推荐作者
     * @param currPage
     * @param pageSize
     * @return
     */
    PageInfo<UserVO> getSuggestAuthorByPage(Integer currPage, Integer pageSize);

    /**
     * 找回密码
     * @param phone
     * @param password
     * @param code
     * @return
     */
    Integer resetPassword(String phone, String password, Integer code);

    /**
     * 发送验证码
     * @param user
     * @return
     */
    int sendCode(User user);


    /**
     *  检测验证码
     * @param user
     * @return
     */
    int testCode(User user);

    /**
     * 获取原手机号
     * @param uid
     * @return
     */
    String getOriginPhone(Integer uid);
}
