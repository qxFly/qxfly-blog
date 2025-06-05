package fun.qxfly.controller.User;

import fun.qxfly.common.domain.entity.UserSetting;
import fun.qxfly.common.domain.po.Result;
import fun.qxfly.common.utils.JwtUtils;
import fun.qxfly.service.User.UserSettingService;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user/setting")
@Tag(name = "用户设置")
public class UserSettingController {

    private final UserSettingService userSettingService;

    public UserSettingController(UserSettingService userSettingService) {
        this.userSettingService = userSettingService;
    }

    /**
     * 获取用户设置
     *
     * @param uid uid
     */
    @Operation(description = "获取用户设置", summary = "获取用户设置")
    @GetMapping("/getUserSettings")
    public Result getUserSettings(Integer uid) {
        UserSetting userSetting = userSettingService.getUserSettings(uid);
        return Result.success(userSetting);
    }

    /**
     * 更新修改用户设置
     *
     * @param userSetting 用户设置对象
     */
    @Operation(description = "用户设置对象", summary = "用户设置对象")
    @PostMapping("/updateUserSettings")
    public Result updateUserSettings(@RequestBody UserSetting userSetting) {
        boolean f = userSettingService.updateUserSettings(userSetting);
        return Result.success();
    }

    /**
     * 上传背景图片
     *
     * @param file 上传背景图片
     */
    @Operation(description = "用户设置对象", summary = "用户设置对象")
    @PostMapping("/uploadBgImg")
    public Result uploadBgImg(MultipartFile file , HttpServletRequest request) {
        String token = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(token);
        Integer uid = Integer.valueOf(claims.get("uid").toString());
        String fileName = userSettingService.uploadBgImg(file,uid);
        return Result.success(fileName);
    }
    /**
     * 删除用户背景
     *
     * @return
     */
    @Operation(description = "删除用户背景", summary = "删除用户背景")
    @PostMapping("/deleteUserBackground")
    public Result deleteUserBackground(@RequestBody Map<String, Object> map) {
        boolean flag = userSettingService.deleteUserBackground((Integer) map.get("uid"), (String) map.get("bgPath"));
        if (flag) {
            return Result.success("修改成功", null);
        } else {
            return Result.error("修改失败");
        }
    }
}
