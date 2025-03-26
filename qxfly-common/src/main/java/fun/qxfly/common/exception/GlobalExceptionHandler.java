package fun.qxfly.common.exception;

import fun.qxfly.common.domain.po.Result;
import fun.qxfly.common.enums.ExceptionEnum;
import fun.qxfly.common.exception.excep.FileException;
import fun.qxfly.common.exception.excep.JwtException;
import fun.qxfly.common.exception.excep.UserException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /*通用异常捕获*/
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e, HttpServletRequest request) {
        e.printStackTrace();
        log.error("模块：Jwt; 请求地址：{}; 发生异常：{}; 异常代码：{}", request.getRequestURL(), e.getMessage(), 0);
        return Result.error(e.getMessage());
    }

    /**
     * Jwt过期异常捕获
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(JwtException.class)
    public Result JwtExpiredException(JwtException e, HttpServletRequest request) {
        e.printStackTrace();
        log.error("模块：Jwt; 请求地址：{}; 发生异常：{}; 异常代码：{}", request.getRequestURL(), e.getMsg(), e.getCode());
        return getResultError(e);

    }

    /**
     * 用户异常捕获
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(UserException.class)
    public Result UserException(UserException e, HttpServletRequest request) {
        e.printStackTrace();
        log.error("模块：Jwt; 请求地址：{}; 发生异常：{}; 异常代码：{}", request.getRequestURL(), e.getMsg(), e.getCode());
        return getResultError(e);
    }

    /**
     * 用户异常捕获
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(FileException.class)
    public Result FileException(FileException e, HttpServletRequest request) {
        e.printStackTrace();
        log.error("模块：File; 请求地址：{}; 发生异常：{}; 异常代码：{}", request.getRequestURL(), e.getMsg(), e.getCode());
        return getResultError(e);
    }


    private Result getResultError(BaseException e) {
        for (ExceptionEnum value : ExceptionEnum.values()) {
            if (value.getCode() == e.getCode()) {
                return Result.error(value);
            }
        }
        return Result.error(ExceptionEnum.COMMON_ERROR);
    }
}
