package fun.qxfly.mapper.User;

import fun.qxfly.common.domain.entity.Token;
import fun.qxfly.common.domain.entity.User;
import fun.qxfly.common.domain.vo.UserVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserInfoMapper {
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
    @Select("select * from user where username = #{username}")
    User checkUsername(User user);

    @Update("update user set avatar = #{path} where id = #{id}")
    void updateImg(String path, int id);

    /**
     * 获取推荐作者
     *
     * @return
     */
    @Select("SELECT u.* FROM user_card uc, user u WHERE uc.Views >= ( SELECT floor( RAND() * ( SELECT avg(Views) FROM user_card ) ) ) and uc.Views != 0 and u.id = uc.id ORDER BY uc.Views DESC")
    List<UserVO> getSuggestAuthor();

    /**
     * 找回密码
     *
     * @param phone
     * @param password
     * @return
     */
    @Update("update user set password = #{password} where phone = #{phone}")
    Integer resetPassword(String phone, String password);

    /**
     * 发送验证码
     *
     * @param phone
     * @param code
     */
    @Insert("insert into captcha(phone,code,createTime) values(#{phone},#{code},#{date})")
    int captcha(String phone, int code, long date);

    /**
     * 获取手机验证
     *
     * @param phone
     * @return
     */
    @Select("select code from captcha where phone = #{phone}")
    List<String> getCode(String phone);

    /**
     * 根据手机号获取用户
     *
     * @param user
     * @return
     */
    @Select("select * from user where phone = #{phone}")
    Integer getUserByPhone(User user);

    /**
     * 删除验证码
     *
     * @param phone
     */
    @Delete("delete from captcha where phone = #{phone}")
    void deleteCode(String phone);


    /**
     * 获取用户原手机号
     *
     * @param uid
     * @return
     */
    @Select("select phone from user where id = #{uid}")
    String getOriginPhone(Integer uid);
}

