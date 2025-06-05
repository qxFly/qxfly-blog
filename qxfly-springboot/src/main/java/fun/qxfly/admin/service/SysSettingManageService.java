package fun.qxfly.admin.service;

import fun.qxfly.common.domain.entity.SysSetting;
import org.springframework.web.multipart.MultipartFile;

public interface SysSettingManageService {

    /**
     * 更新系统设置
     * @param sysSetting
     * @return
     */
    boolean updateSysSetting(SysSetting sysSetting);

    /**
     * 上传背景图片
     * @param file
     * @return
     */
    String uploadBgImg(MultipartFile file);
}
