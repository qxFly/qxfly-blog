package fun.qxfly.admin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminMapper {

    /**
     * 判断用户是否为管理员
     *
     * @param uid uid
     */
    @Select("select role from user where id = #{uid}")
    Integer check(Integer uid);
}
