package fun.qxfly.mapper.User;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LogoutMapper {
    /**
     * 把退出的用户加入黑名单
     *
     * @param username 用户名
     * @param uid      用户id
     */
    @Insert("insert into logout_users(username,uid)value(#{username},#{uid})")
    void addTokenToBlack(String username, Integer uid);

    /**
     * 删除token
     *
     * @param uid 用户id
     */
    @Delete("delete from logout_users where uid = #{uid}")
    void deleteToken(Integer uid);

    /**
     * 获取退出状态信息
     *
     * @param uid 用户id
     */
    @Select("select username from logout_users where uid = #{uid}")
    String getLogoutStatusByToken(Integer uid);
}
