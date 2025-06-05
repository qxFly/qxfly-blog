package fun.qxfly.admin.controller;

import fun.qxfly.admin.service.SysSettingManageService;
import fun.qxfly.common.domain.entity.SysSetting;
import fun.qxfly.common.domain.po.Result;
import fun.qxfly.common.utils.JwtUtils;
import fun.qxfly.service.User.UserSettingService;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("manage")
@Tag(name = "管理员")
public class SysSettingManageController {
    private final SysSettingManageService sysSettingManageService;
    private final UserSettingService userSettingService;

    public SysSettingManageController(SysSettingManageService sysSettingManageService, UserSettingService userSettingService) {
        this.sysSettingManageService = sysSettingManageService;
        this.userSettingService = userSettingService;
    }

    /**
     * 更新系统设置
     *
     * @param sysSetting
     * @return
     */
    @PostMapping("/updateSysSetting")
    @Operation(description = "更新系统设置", summary = "更新系统设置")
    public Result updateSysSetting(@RequestBody SysSetting sysSetting) {
        boolean f = sysSettingManageService.updateSysSetting(sysSetting);
        return Result.success();
    }

    /**
     * 上传背景图片
     *
     * @param file 上传背景图片
     */
    @Operation(description = "上传背景图片", summary = "上传背景图片")
    @PostMapping("/uploadBgImg")
    public Result uploadBgImg(MultipartFile file) {
        String fileName = sysSettingManageService.uploadBgImg(file);
        return Result.success(fileName);
    }
}
