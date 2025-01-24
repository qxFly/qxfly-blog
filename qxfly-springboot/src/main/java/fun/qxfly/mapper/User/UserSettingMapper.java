package fun.qxfly.mapper.User;

import fun.qxfly.common.domain.entity.UserSetting;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserSettingMapper {
    /**
     * 获取用户设置
     * @param uid uid
     * @return
     */
    @Select("select * from user_settings where uid = #{uid}")
    UserSetting getUserSettings(Integer uid);

    /**
     * 更新用户设置
     *
     * @param userSetting 用户设置对象
     */
    boolean updateUserSettings(UserSetting userSetting);

    /**
     * 初始化用户设置
     * @param userSetting userSetting
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into user_settings(uid) values(#{uid})")
    boolean initUserSettings(UserSetting userSetting);
}
