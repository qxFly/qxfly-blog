package fun.qxfly.mapper.User;

import fun.qxfly.common.domain.entity.Navigation;
import fun.qxfly.common.domain.entity.User;
import fun.qxfly.common.domain.vo.UserVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserInfoMapper {

    /**
     * 根据uid获取用户
     *
     * @param uid uid
     */
    User getUserInfo(Integer uid);

    /**
     * 更改用户信息
     *
     * @param user 用户对象
     */
    boolean updateUserInfo(User user);

    /**
     * 检查用户名是否可行
     *
     * @param user 用户对象
     */
    @Select("select * from user where username = #{username}")
    User checkUsername(User user);

    /**
     * 更新用户头像
     *
     * @param path 头像路径
     * @param uid  用户id
     */
    @Update("update user set avatar = #{path} where id = #{uid}")
    void updateImg(@Param("path") String path, @Param("uid") int uid);

    /**
     * 获取推荐作者
     */
    @Select("SELECT u.* FROM user_card uc, user u WHERE uc.Views >= ( SELECT floor( RAND() * ( SELECT avg(Views) FROM user_card ) ) ) and uc.Views != 0 and u.id = uc.id ORDER BY uc.Views DESC")
    List<UserVO> getSuggestAuthor();

    /**
     * 找回密码
     *
     * @param phone 手机号
     * @param password 密码
     */
    @Update("update user set password = #{password} where phone = #{phone}")
    Integer resetPassword(@Param("phone") String phone, @Param("password") String password);

    /**
     * 发送验证码
     *
     * @param phone 手机号
     * @param code  验证码
     */
    @Insert("insert into captcha(phone,code,createTime) values(#{phone},#{code},#{date})")
    int captcha(@Param("phone") String phone, @Param("code") int code, @Param("date") long date);

    /**
     * 获取用户原手机号
     *
     * @param uid uid
     */
    @Select("select phone from user where id = #{uid}")
    String getOriginPhone(Integer uid);

    /**
     * 获取用户空间导航栏
     * @param role 用户角色
     * @return 导航栏列表
     */
    @Select("select * from navigation where role = 0 and type = 'userSpace' or role = #{role} and type = 'userSpace'")
    List<Navigation> listUserSpaceNav(Integer role);
}

