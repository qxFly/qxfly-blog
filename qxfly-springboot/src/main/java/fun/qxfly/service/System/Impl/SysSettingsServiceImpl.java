package fun.qxfly.service.System.Impl;

import fun.qxfly.common.domain.entity.SysSetting;
import fun.qxfly.mapper.System.SysSettingsMapper;
import fun.qxfly.service.System.SysSettingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SysSettingsServiceImpl implements SysSettingsService {
    private final SysSettingsMapper sysSettingsMapper;

    @Value("${qxfly.file.path.systemBgImg}")
    String sysSettingBgImgPath;

    public SysSettingsServiceImpl(SysSettingsMapper sysSettingsMapper) {
        this.sysSettingsMapper = sysSettingsMapper;
    }

    /**
     * 获取系统设置
     *
     * @return
     */
    @Override
    public SysSetting getSysSetting() {
        SysSetting sysSetting = sysSettingsMapper.getSysSetting();
        sysSetting.setBgImgPath(sysSettingBgImgPath + sysSetting.getBgImgPath());
        return sysSetting;
    }
}
