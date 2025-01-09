package fun.qxfly.mapper.User;

import fun.qxfly.common.domain.entity.Token;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LogoutMapper {
    /**
     * 把退出的用户加入黑名单
     *
     * @param token
     */
    @Insert("insert into logout_users(username,token)value(#{username},#{token})")
    void addTokenToBlack(Token token);

    /**
     * 删除token
     *
     * @param token
     */
    @Delete("delete from logout_users where token = #{token}")
    void deleteToken(String token);

    /**
     * 获取退出状态信息
     *
     * @param token
     */
    @Select("select username from logout_users where token = #{token}")
    String getLogoutStatusByToken(String token);
}
