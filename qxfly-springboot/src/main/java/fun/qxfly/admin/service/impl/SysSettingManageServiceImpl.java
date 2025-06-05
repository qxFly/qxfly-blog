package fun.qxfly.admin.service.impl;

import fun.qxfly.admin.mapper.SysSettingManageMapper;
import fun.qxfly.admin.service.SysSettingManageService;
import fun.qxfly.common.domain.entity.SysSetting;
import fun.qxfly.common.enums.FilePaths;
import fun.qxfly.common.utils.FileUtils;
import fun.qxfly.service.System.SysSettingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
public class SysSettingManageServiceImpl implements SysSettingManageService {
    @Value("${qxfly.file.path.systemBgImg}")
    String systemBgImgPath;
    private final SysSettingManageMapper sysSettingManageMapper;
    private final SysSettingsService sysSettingsService;

    public SysSettingManageServiceImpl(SysSettingManageMapper sysSettingManageMapper, SysSettingsService sysSettingsService) {
        this.sysSettingManageMapper = sysSettingManageMapper;
        this.sysSettingsService = sysSettingsService;
    }

    /**
     * 更新系统设置
     *
     * @param sysSetting
     * @return
     */
    @Override
    public boolean updateSysSetting(SysSetting sysSetting) {
        String name = sysSetting.getBgImgPath().split("/systemBgImg")[1];
        sysSetting.setBgImgPath(name);
        return sysSettingManageMapper.updateSysSetting(sysSetting);
    }

    /**
     * 上传背景图片
     *
     * @param file
     * @return
     */
    @Override
    public String uploadBgImg(MultipartFile file) {
        String path = FilePaths.SYSTEM_SETTINGS_PATH.getPath();

        // 获取原背景图片
        String preBgImgPath = null;
        SysSetting sysSettings = sysSettingsService.getSysSetting();
        if (sysSettings != null) {
            String bgImgPath = sysSettings.getBgImgPath().split("systemBgImg/")[1];
            if (bgImgPath != null && !bgImgPath.isEmpty()) {
                preBgImgPath = path + bgImgPath;
            }
        }
        log.info(preBgImgPath);
        // 上传背景图片
        String fileName;
        try {
            fileName = FileUtils.upload(path, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 上传成功后删除原背景图片
        if (preBgImgPath != null) {
            File preBgImg = new File(preBgImgPath);
            if (preBgImg.exists()) {
                boolean delete = preBgImg.delete();
            }
        }
        return systemBgImgPath + fileName;
    }
}
