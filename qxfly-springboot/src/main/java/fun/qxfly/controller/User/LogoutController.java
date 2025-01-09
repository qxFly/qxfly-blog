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
     */
    @Operation(description = "退出", summary = "退出")
    @PostMapping("/logout")
    public Result Logout(HttpServletRequest request) {
        String reqToken = request.getHeader("token");
        if (!StringUtils.hasLength(reqToken)) return Result.error("验证失败");

        Claims claims = JwtUtils.parseJWT(reqToken);
        if (claims == null) {
            log.info("退出操作，但是token失效");
            logoutService.deleteToken(reqToken);
            return Result.error("验证失败");
        }
        String username = (String) claims.get("username");
        logoutService.logout(new Token(username, reqToken));
        return Result.success();
    }
}
