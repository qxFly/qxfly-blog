package fun.qxfly.mapper.User;

import fun.qxfly.common.domain.entity.Token;
import fun.qxfly.common.domain.entity.User;
import org.apache.ibatis.annotations.*;

/**
 * 部门管理
 */
@Mapper
public interface LoginMapper {

    /**
     * 登陆校验
     *
     * @param user
     * @return
     */
//    @Select("select * from user where username = #{username} and password = #{password}")
    User login(User user);

    /**
     * 查询用户是否有token
     *
     * @param user
     * @return
     */
    @Select("select * from user_token where username = #{username}")
    Token getTokenByUser(User user);

    /**
     * 设置用户的token
     *
     * @param username
     * @param newJwt
     */
    @Insert("insert into user_token(username,token,create_time)value(#{username},#{newJwt},#{createDate})")
    void setTokenByUser(@Param("username") String username, @Param("newJwt") String newJwt, @Param("createDate") long createDate);

    /**
     * 删除用户token
     *
     * @param token
     */
    @Delete("delete from user_token where username = #{username}")
    void deleteToken(Token token);

    /**
     * 更新用户token
     *
     * @param username
     * @param newJwt
     */
    @Update("update user_token set token = #{newJwt}, create_time = #{nowDate} where username = #{username}")
    void updateJwt(@Param("username") String username, @Param("newJwt") String newJwt, @Param("nowDate") long nowDate);

    /**
     * 获取token创建时间
     *
     * @param jwt
     * @return
     */
    @Select("select create_time from user_token where username = #{username}")
    Long getJwtCreateTime(Token jwt);

}
