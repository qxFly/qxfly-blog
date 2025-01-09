package fun.qxfly.admin.controller;

import fun.qxfly.admin.service.AdminService;
import fun.qxfly.common.domain.po.Result;
import fun.qxfly.common.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/manage")
@Tag(name = "管理员")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * 检查是否为管理员
     *
     * @return
     */
    @Operation(description = "检查是否为管理员、审核员", summary = "检查是否为管理员、审核员")
    @PostMapping("/check")
    public Result check(HttpServletRequest request) {
        String token = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(token);
        if (claims == null) return Result.error("您没有权限访问！");
        Integer uid = (Integer) JwtUtils.parseJWT(token).get("uid");
        Integer f = adminService.check(uid);
        if (f != 0) {
            return Result.success(f);
        } else {
            return Result.error("您没有权限访问！");
        }
    }
}
