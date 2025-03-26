package fun.qxfly.controller.User;

import fun.qxfly.common.domain.entity.User;
import fun.qxfly.common.domain.po.Result;
import fun.qxfly.common.service.RSAService;
import fun.qxfly.common.utils.JwtUtils;
import fun.qxfly.common.utils.RoleUtils;
import fun.qxfly.service.User.LoginService;
import fun.qxfly.service.User.LogoutService;
import fun.qxfly.service.User.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * 登录Controller
 */
@Slf4j
@CrossOrigin
@RestController
@Tag(name = "用户")
public class LoginController {
    private final UserInfoService userInfoService;

    private final LoginService loginService;
    private final LogoutService logoutService;
    private final RSAService rsaService;

    public LoginController(LoginService loginService, LogoutService logoutService, UserInfoService userInfoService, RSAService rsaService) {
        this.loginService = loginService;
        this.logoutService = logoutService;
        this.userInfoService = userInfoService;
        this.rsaService = rsaService;
    }

    /**
     * 登陆
     *
     * @param user 用户实体
     * @return
     */
    @Operation(description = "登陆", summary = "登陆")
    @PostMapping("/login")
    public Result Login(@RequestBody User user) {
        User u = loginService.login(user);
        /* 登录失败 */
        if (u == null) return Result.error("用户名或密码错误");
        /* 登录成功 */
        String role = RoleUtils.getRoleNameByRoleId(u.getRole());
        HashMap<String, String> resBody = JwtUtils.createAccessAndRefreshToken(u.getId(), u.getUsername(), role, null);
        resBody.put("uid", u.getId().toString());
        resBody.put("username", u.getUsername());
        return Result.success(resBody);
//        HashMap<String, String> resBody = new HashMap<>();
//        resBody.put("uid", u.getId().toString());
//        resBody.put("username", u.getUsername());
//        /*获取用户的token*/
//        Token userToken = loginService.getTokenByUser(u);
//        /*token不为空*/
//        if (userToken != null && userToken.getToken() != null) {
//            /*验证Token是否有效*/
//            Claims claims = JwtUtils.parseJWT(userToken.getToken());
//            /* token有效，登录*/
//            if (claims != null) {
//                logoutService.deleteToken(userToken);
//                resBody.put("token", userToken.getToken());
//                return Result.success(resBody);
//            }
//            loginService.deleteToken(userToken);
//            logoutService.deleteToken(userToken);
//        }
//        /* 用户token失效或者无token，生成token */
//        /* 获取现在的时间，转化为毫秒 */
//        String newToken = JwtUtils.createToken(u.getId(), u.getUsername(), null);
//        long createDate = JwtUtils.parseJWT(newToken).getIssuedAt().getTime();
//        loginService.setToken(u.getUsername(), newToken, createDate);
//        resBody.put("token", newToken);
//        return Result.success(resBody);
    }

    /**
     * 更新登录状态
     */
    @Operation(description = "每次进入站点检查更新登录状态", summary = "更新登录状态")
    @PostMapping("/updateLoginStatue")
    public Result updateLoginStatue(@RequestBody HashMap<String, String> map) {
        String refreshToken = map.get("refreshToken");
        HashMap<String, String> tokenMap = JwtUtils.refreshToken(refreshToken);
        return Result.success(tokenMap);
//        String token = request.getHeader("token");
////        /* 检查token有效性 */
//        Claims claims = JwtUtils.parseJWT(token);
//        /* 检查用户是否退出 */
//        String logoutStatus = logoutService.getLogoutStatus(token);
//        if (logoutStatus != null) {
//            return Result.noLoginError();
//        }
//        /* 检查用户信息是否异常 */
//        Integer uid = (Integer) claims.get("uid");
//        String username = (String) claims.get("username");
//        User userInfo = userInfoService.getUserInfo(uid);
//        if (userInfo == null) {
//            return Result.noLoginError();
//        }
//        /* 有效则判断剩余时间 */
//        long createTime = claims.getIssuedAt().getTime();
//        long updateTime = new Date().getTime();
//        /*判断证书剩余时间，小于1周则发放新证书*/
//        if (updateTime - createTime >= 604800000) {
//            log.info("剩余一周，续期");
//            /*生成token*/
//            String newToken = JwtUtils.refreshToken(token);
//            if (newToken == null) {
//                log.info("newToken");
//                return Result.noLoginError();
//            }
//            loginService.updateToken(username, newToken, updateTime);
//            return Result.success(newToken);
//        } else {
//            return Result.success("ok");
//        }
    }

    /**
     * 获取公匙
     *
     * @return
     */
    @Operation(description = "获取公匙", summary = "获取公匙")
    @PostMapping("/gs")
    public Result gs() {
        String publicKey = rsaService.generatePublicKey();
        return Result.success(publicKey);
    }

}