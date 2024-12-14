package fun.qxfly.controller.User;

import fun.qxfly.common.domain.entity.Token;
import fun.qxfly.common.domain.po.Result;
import fun.qxfly.common.utils.JwtUtils;
import fun.qxfly.service.User.LogoutService;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name = "用户")
public class LogoutController {
    private final LogoutService logoutService;

    public LogoutController(LogoutService logoutService) {
        this.logoutService = logoutService;
    }

    /**
     * 退出校验
     *
     * @param t
     * @return
     */
    @Operation(description = "退出", summary = "退出")
    @PostMapping("/logout")
    public Result Logout(@RequestBody Token t, HttpServletRequest request) {
        if (t == null) return Result.error("验证失败");

        String reqToken = request.getHeader("token");
        String token = t.getToken();
        if (!StringUtils.hasLength(reqToken) || !reqToken.equals(token)) return Result.error("验证失败");

        Claims claims;
        try {
            claims = JwtUtils.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("退出操作，但是token失效");
            logoutService.deleteToken(t);
            return Result.error("验证失败");
        }
        String username = (String) claims.get("username");
        if (username.equals(t.getUsername())) {
            logoutService.logout(t);
        } else {
            return Result.error("验证失败!");
        }
        return Result.success();
    }
}
