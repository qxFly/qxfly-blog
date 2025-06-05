package fun.qxfly.service.User;

import fun.qxfly.common.domain.entity.UserSetting;
import org.springframework.web.multipart.MultipartFile;

public interface UserSettingService {

    /**
     * 获取用户设置
     *
     * @param uid uid
     */
    UserSetting getUserSettings(Integer uid);

    /**
     * 更新修改用户设置
     *
     * @param userSetting 用户设置对象
     */
    boolean updateUserSettings(UserSetting userSetting);

    /**
     * 上传背景图片
     * @param file
     * @return
     */
    String uploadBgImg(MultipartFile file, Integer uid);
    /**
     * 删除用户背景
     *
     * @param uid    用户id
     * @param bgPath 背景路径
     */
    boolean deleteUserBackground(Integer uid, String bgPath);
}
