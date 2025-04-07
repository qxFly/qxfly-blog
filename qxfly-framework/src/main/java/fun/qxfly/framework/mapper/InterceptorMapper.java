package fun.qxfly.framework.mapper;

import fun.qxfly.common.domain.entity.User;
import org.apache.ibatis.annotations.Delete;
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

    /**
     * 检查用户是否过期
     *
     * @param user 用户
     * @return
     */
    @Select("select username from expiration_user where username = #{username}")
    String isExpirationUser(User user);

    /**
     * 移除过期用户
     * @param user
     */
    @Delete("delete from expiration_user where username = #{username}")
    void removeExpirationUser(User user);
}
