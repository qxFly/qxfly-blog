package fun.qxfly.controller.User;

import fun.qxfly.common.domain.entity.User;
import fun.qxfly.common.domain.po.Result;
import fun.qxfly.common.service.RSAService;
import fun.qxfly.common.utils.AliyunDysmsapi;
import fun.qxfly.common.utils.RSAEncrypt;
import fun.qxfly.service.User.RegisterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@Tag(name = "用户")
public class RegisterController {

    private final AliyunDysmsapi aliyunDysmsapi;
    private final RegisterService registerService;
    private final RSAService rsaService;

    public RegisterController(RegisterService registerService, AliyunDysmsapi aliyunDysmsapi, RSAService rsaService) {
        this.registerService = registerService;
        this.aliyunDysmsapi = aliyunDysmsapi;
        this.rsaService = rsaService;
    }

    /**
     * 用户注册
     *
     * @return
     */
    @Operation(description = "用户注册", summary = "用户注册")
    @PostMapping("/register")
    public Result register(@RequestBody Map<String, Object> map) {
        /* 判断用户是否注册 */
        String username = (String) map.get("username");
        if (registerService.checkUsername(username) != null) {
            return Result.error("用户名已注册");
        }
        String phone = (String) map.get("phone");
        if (registerService.checkPhone(phone) != null) {
            return Result.error("手机号已注册!");
        }
        /* 检查邀请码和验证码 */
        String invite = (String) map.get("invite");
        String SysInvite = System.getProperty("Invite");

        String ucode = (String) map.get("code");
        boolean isCode = false;
        if (invite == null) {
            isCode = true;
        } else {
            if (!invite.equals(SysInvite)) {
                isCode = true;
            }
        }
        if (isCode) {
            try {
                int code = aliyunDysmsapi.testCode(phone, Integer.valueOf(ucode));
                if (code == 0) {
                    return Result.error("验证码错误！");
                } else if (code == -1) {
                    return Result.error("请获取验证码！");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return Result.error("请输入正确的验证码或邀请码");
            }

        }

        /* 解码密码 */
        String encodePassword = (String) map.get("password");
        String publicKey = (String) map.get("salt");
        String decodePassword;
        String privateKey = rsaService.getPrivateKey(publicKey);
        try {
            decodePassword = RSAEncrypt.decrypt(encodePassword, privateKey);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("系统错误");
        }
        /* 创建用户 */
        User user = new User(username, decodePassword, phone);
        registerService.register(user);
        return Result.success("注册成功！");
    }
}

