package fun.qxfly.framework.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface InterceptorMapper {
    /**
     * 获取退出状态信息
     *
     * @param token
     */
    @Select("select username from logout_users where token = #{token}")
    String getLogoutStatusByToken(String token);

    /**
     * 判断用户是否为管理员
     *
     * @param username
     * @return
     */
    @Select("select role from user where username = #{username}")
    Integer isAdmin(String username);
}
