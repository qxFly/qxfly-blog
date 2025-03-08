package fun.qxfly.framework.interceptor;

import fun.qxfly.common.domain.entity.User;
import fun.qxfly.common.utils.JwtUtils;
import fun.qxfly.common.utils.RoleUtils;
import fun.qxfly.framework.custom.LoginHolder;
import fun.qxfly.framework.service.InterceptorService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {
    final InterceptorService interceptorService;

    public LoginCheckInterceptor(InterceptorService interceptorService) {
        this.interceptorService = interceptorService;
    }

    //目标资源方法运行前运行，返回true:放行，返回false:不放行
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) {
        if (HttpMethod.OPTIONS.toString().equals(req.getMethod())) {
            return true;
        }
        // 错误信息
//        Result error = Result.error("Not Login");
//        String errorMsg = JSONObject.toJSONString(error);
        //中文支持
        resp.setContentType("text/html;charset=UTF-8");
        //获取请求的url
        String url = req.getRequestURI();
        log.info("请求的url:{}", url);

        //获取证书token
        String token = req.getHeader("token");
        //解析token，解析失败，返回错误结果（未登录）
        Claims claims = JwtUtils.parseJWT(token);
        User user = new User();
        user.setId(claims.get("uid", Integer.class));
        user.setUsername(claims.get("username", String.class));
        user.setRole(RoleUtils.getRoleIdByRoleName(claims.get("role", String.class)));
        LoginHolder.setUser(user);
        log.info("证书合法，放行");
        return true;
//        if (claims == null) {
//            log.info("解析证书失败，未登录");
//            resp.getWriter().write(errorMsg);
//            return false;
//        }
//        /*查询用户是否退出，退出则返回错误结果*/
//        String logoutUser = interceptorService.getLogoutStatus(token);
//        if (logoutUser != null) {
//            resp.getWriter().write(errorMsg);
//            return false;
//        }
//
//        /*检查是否为管理员*/
//        if (url.contains("manage")) {
//            String username = (String) claims.get("username");
//            if (!interceptorService.isAdmin(username)) return false;
//        }
//        //放行
//        log.info("证书合法，放行");
//        return true;
    }
}
