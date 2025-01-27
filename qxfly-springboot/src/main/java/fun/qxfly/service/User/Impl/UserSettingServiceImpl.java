package fun.qxfly.service.User.Impl;

import fun.qxfly.common.domain.entity.UserSetting;
import fun.qxfly.common.enums.FilePaths;
import fun.qxfly.common.utils.FileUtils;
import fun.qxfly.mapper.User.UserSettingMapper;
import fun.qxfly.service.User.UserSettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
public class UserSettingServiceImpl implements UserSettingService {

    @Value("${qxfly.file.path.userBgImg}")
    String bgImgDownloadPath;

    private final UserSettingMapper userSettingMapper;

    public UserSettingServiceImpl(UserSettingMapper userSettingMapper) {
        this.userSettingMapper = userSettingMapper;
    }

    /**
     * 获取用户设置
     *
     * @param uid uid
     */
    @Override
    public UserSetting getUserSettings(Integer uid) {
        UserSetting userSetting = userSettingMapper.getUserSettings(uid);
        if (userSetting == null) {
            userSetting = new UserSetting();
            userSetting.setUid(uid);
            userSetting.setBgSwitch(0);
            boolean f = userSettingMapper.initUserSettings(userSetting);
        } else {
            if (userSetting.getBgImgPath() != null && !userSetting.getBgImgPath().isEmpty()) {
                String bgImgPath = bgImgDownloadPath + userSetting.getBgImgPath();
                userSetting.setBgImgPath(bgImgPath);
            }
        }
        return userSetting;
    }

    /**
     * 更新修改用户设置
     *
     * @param userSetting 用户设置对象
     */
    @Override
    public boolean updateUserSettings(UserSetting userSetting) {
        userSetting.setBgImgPath("");
        return userSettingMapper.updateUserSettings(userSetting);
    }

    /**
     * 上传背景图片
     *
     * @param file
     * @return
     */
    @Override
    public String uploadBgImg(MultipartFile file, Integer uid) {
        String path = FilePaths.USER_BACKGROUND_IMAGE_PATH.getPath();

        // 获取原背景图片
        String preBgImgPath = null;
        UserSetting userSettings = userSettingMapper.getUserSettings(uid);
        if (userSettings != null) {
            String bgImgPath = userSettings.getBgImgPath();
            if (bgImgPath != null && !bgImgPath.isEmpty()) {
                preBgImgPath = path + bgImgPath;
            }
        }

        // 上传背景图片
        String fileName;
        try {
            fileName = FileUtils.upload(path, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 上传成功后更新数据库
        UserSetting userSetting = new UserSetting();
        userSetting.setUid(uid);
        userSetting.setBgImgPath(fileName);
        boolean b = userSettingMapper.updateUserSettings(userSetting);
        if (!b) throw new RuntimeException("更新数据库失败");
        // 上传成功后删除原背景图片
        if (preBgImgPath != null) {
            File preBgImg = new File(preBgImgPath);
            if (preBgImg.exists()) {
                boolean delete = preBgImg.delete();
            }
        }
        return bgImgDownloadPath + fileName;
    }
}
