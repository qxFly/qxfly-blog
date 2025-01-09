package fun.qxfly.controller.User;

import fun.qxfly.common.domain.entity.Token;
import fun.qxfly.common.domain.entity.User;
import fun.qxfly.common.domain.po.Result;
import fun.qxfly.common.service.RSAService;
import fun.qxfly.common.utils.JwtUtils;
import fun.qxfly.common.utils.RSAEncrypt;
import fun.qxfly.service.User.LoginService;
import fun.qxfly.service.User.LogoutService;
import fun.qxfly.service.User.UserInfoService;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
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
        /*判断是否为手机号*/
        boolean isPhone = user.getUsername().matches("^[0-9]*$");
        if (isPhone) {
            user.setPhone(user.getUsername());
        }
        String encodePassword = user.getPassword();
        String decodePassword;
        String privateKey = rsaService.getPrivateKey(user.getSalt());
        try {
            decodePassword = RSAEncrypt.decrypt(encodePassword, privateKey);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("密码错误");
        }
        user.setPassword(decodePassword);
        User u = loginService.login(user);
        if (u != null) {
            HashMap<String, String> resBody = new HashMap<>();
            resBody.put("uid", u.getId().toString());
            resBody.put("username", u.getUsername());
            /*获取用户的token*/
            Token userToken = loginService.getTokenByUser(u);
            /*token不为空*/
            if (userToken != null && userToken.getToken() != null) {
                try {
                    /*验证Token是否有效*/
                    JwtUtils.parseJWT(userToken.getToken());
                    /* token有效，登录*/
                    logoutService.deleteToken(userToken.getToken());
                    resBody.put("token", userToken.getToken());
                    return Result.success(resBody);
                } catch (Exception e) {
                    e.printStackTrace();
                    loginService.deleteToken(userToken);
                    log.info("登录证书过期或错误！");
                }
            }
            /* 用户无token生成token */
            /* 获取现在的时间，转化为毫秒 */
            Date nowdate = new Date();
            String newToken = JwtUtils.createToken(u.getId(), u.getUsername(), nowdate, null);
            long createDate = nowdate.getTime();
            loginService.setToken(u.getUsername(), newToken, createDate);
            if (userToken != null)
                logoutService.deleteToken(userToken.getToken());
            resBody.put("token", newToken);
            return Result.success(resBody);
        }
        return Result.error("用户名或密码错误");
    }

    /**
     * 更新登录状态
     *
     * @param token token
     */
    @Operation(description = "每次进入站点检查更新登录状态", summary = "更新登录状态")
    @PostMapping("/updateLoginStatue")
    public Result updateLoginStatue(@RequestBody Token token, HttpServletRequest request) {
        String requestToken = request.getHeader("token");
        String token1 = token.getToken();
        /*检测token一致性和是否为空*/
        if (token1 == null || !token1.equals(requestToken))
            return Result.noLoginError();
        /* 检查token有效性 */
        Claims claims = JwtUtils.parseJWT(token.getToken());
        if (claims == null) return Result.noLoginError();
        String logoutStatus = logoutService.getLogoutStatus(token1);
        if (logoutStatus != null) {
            return Result.noLoginError();
        }
        Integer uid = (Integer) claims.get("uid");
        User userInfoByToken = userInfoService.getUserInfo(uid);
        if (userInfoByToken == null) {
            return Result.noLoginError();
        }
        /* 有效则判断剩余时间 */
        long createTime = claims.getIssuedAt().getTime();
        Date nowTime = new Date();
        long updateTime = nowTime.getTime();
        /*判断证书剩余时间，小于1周则发放新证书*/
        if (updateTime - createTime >= 604800000) {
            log.info("剩余一周，续期");
            /*生成token*/
            try {
                Integer userId = (Integer) claims.get("uid");
                String username = (String) claims.get("username");
                String newToken = JwtUtils.createToken(userId, username, nowTime, null);
                loginService.updateToken(token.getUsername(), newToken, updateTime);
                return Result.success(newToken);
            } catch (Exception e) {
                e.printStackTrace();
                return Result.noLoginError();
            }
        } else {
//            log.info("无需续期");
            return Result.success("ok");
        }
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