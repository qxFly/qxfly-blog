package fun.qxfly.mapper.User;

import fun.qxfly.common.domain.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface RegisterMapper {

    /**
     * 用户注册
     *
     * @param user
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into user(username,password,phone)values(#{username},#{password},#{phone})")
    void register(User user);

    /**
     * 检测用户名是否被注册
     *
     * @param user
     * @return
     */
    @Select("select * from user where username = #{username}")
    User findUserByUsername(String user);

    /**
     * 检测手机是否被注册
     *
     * @param user
     * @return
     */
    @Select("select * from user where phone = #{phone}")
    User findUserByPhone(String user);

    /**
     * 获取用户
     *
     * @param user
     * @return
     */
    @Select("select id from user where username = #{username}")
    Integer getUserId(User user);

    /**
     * 创建用户信息
     *
     * @param user
     */
    @Insert("insert into user_card(id, username)VALUES (#{id},#{username})")
    void createUserInfo(User user);
}
