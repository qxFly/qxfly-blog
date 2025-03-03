package fun.qxfly.admin.mapper;

import fun.qxfly.common.domain.entity.User;
import fun.qxfly.common.domain.entity.UserSetting;
import fun.qxfly.common.domain.vo.UserSettingVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserManageMapper {
    /**
     * 根据id删除用户
     *
     * @param id
     * @return
     */
    @Delete("delete from user where id = #{id}")
    boolean deleteUserById(Integer id);

    /**
     * 编辑用户信息
     *
     * @param user
     * @return
     */
    @Update("update user set username = #{username}, role = #{role}, email = #{email}, phone = #{phone}, introduction = #{introduction},location = #{location}, birthday = #{birthday} where id = #{id}")
    boolean editUserInfo(User user);

    /**
     * 根据条件列出用户
     *
     * @param user
     * @return
     */
    List<User> listUser(User user);

    /**
     * 删除用户头像
     *
     * @param user
     * @return
     */
    @Update("update user set avatar = null where id = #{id}")
    boolean deleteUserAvatar(User user);

    /**
     * 根据id获取用户信息
     *
     * @param id
     * @return
     */
    @Select("select * from user where id = #{id}")
    User getUserById(Integer id);

    /**
     * 删除用户token
     *
     * @param username
     */
    @Delete("delete from user_token where username = #{username}")
    void remoUserToken(String username);

    /**
     * 设置用户token
     *
     * @param id
     * @param username
     * @param token
     * @param time
     */
    @Insert("insert into user_token(userId, username, token, create_time) values(#{id}, #{username}, #{token}, #{time})")
    void setUserToken(@Param("uid") Integer id, @Param("uid") String username, @Param("uid") String token, @Param("uid") long time);

    /**
     * 列出用户设置
     *
     * @param uid
     * @param username
     * @return
     */
    List<UserSettingVO> listUserSetting(@Param("uid") Integer uid, @Param("username") String username);

    /**
     * 编辑用户设置信息
     *
     * @param userSetting 用户设置信息
     * @return
     */
    @Update("update user_settings set bgImgPath = #{bgImgPath}, bgSwitch = #{bgSwitch}, bgBlur = #{bgBlur} where uid = #{uid}")
    boolean editUserSetting(UserSetting userSetting);

    /**
     * 删除用户背景
     *
     * @param uid uid
     * @return boolean
     */
    @Update("update user_settings set bgImgPath = null where uid = #{uid}")
    boolean deleteUserBackground(Integer uid);
}
