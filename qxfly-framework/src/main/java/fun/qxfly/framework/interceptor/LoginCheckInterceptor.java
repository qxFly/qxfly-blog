package fun.qxfly.framework.interceptor;

import fun.qxfly.common.domain.entity.User;
import fun.qxfly.common.enums.ExceptionEnum;
import fun.qxfly.common.exception.excep.UserException;
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

import java.io.IOException;

@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {
    final InterceptorService interceptorService;

    public LoginCheckInterceptor(InterceptorService interceptorService) {
        this.interceptorService = interceptorService;
    }

    //目标资源方法运行前运行，返回true:放行，返回false:不放行
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws IOException {
        if (HttpMethod.OPTIONS.toString().equals(req.getMethod())) {
            return true;
        }
        //中文支持
        resp.setContentType("text/html;charset=UTF-8");
        //获取请求的url
        String url = req.getRequestURI();
        log.debug("请求的url:{}", url);

        //获取证书token
        String token = req.getHeader("token");
        //解析token，解析失败，返回错误结果（未登录）
        Claims claims = JwtUtils.parseJWT(token);//todo 生异常：token已过期; 异常代码：1101
        User user = new User();
        user.setId(claims.get("uid", Integer.class));
        user.setUsername(claims.get("username", String.class));
        user.setRole(RoleUtils.getRoleIdByRoleName(claims.get("role", String.class)));
        if (interceptorService.isExpirationUser(user)) throw new UserException(ExceptionEnum.USER_LOGIN_EXPIRED);

        /*检查是否为管理员/审核员：后台接口仅限角色不为普通用户的账号*/
        if (url.contains("/manage") && !interceptorService.isAdmin(user.getUsername())) {
            log.warn("用户 {} 无后台权限，拦截请求: {}", user.getUsername(), url);
            resp.getWriter().write("{\"code\":0,\"msg\":\"您没有权限访问！\",\"data\":null}");
            return false;
        }

        LoginHolder.setUser(user);
        log.debug("证书合法，放行");
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse resp, Object handler, Exception ex) {
        //清理ThreadLocal，避免线程池复用导致串号
        LoginHolder.removeUser();
    }
}
