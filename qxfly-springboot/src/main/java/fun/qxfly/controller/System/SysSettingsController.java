package fun.qxfly.controller.System;

import fun.qxfly.common.domain.entity.SysSetting;
import fun.qxfly.common.domain.po.Result;
import fun.qxfly.service.System.SysSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("sysSetting")
@Tag(name = "系统设置")
public class SysSettingsController {
    private final SysSettingsService sysSettingsService;

    public SysSettingsController(SysSettingsService sysSettingsService) {
        this.sysSettingsService = sysSettingsService;
    }

    /**
     * 获取系统设置
     *
     * @return
     */
    @Operation(description = "获取系统设置", summary = "获取系统设置")
    @PostMapping("/getSysSettings")
    public Result getSysSetting() {
        SysSetting sysSetting = sysSettingsService.getSysSetting();
        return Result.success(sysSetting);
    }
}
