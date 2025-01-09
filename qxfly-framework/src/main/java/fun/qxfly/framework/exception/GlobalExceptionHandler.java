package fun.qxfly.framework.exception;

import fun.qxfly.common.domain.po.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e, HttpServletRequest request) {
        e.printStackTrace();
        log.error("请求地址：{}，发生异常：{}", request.getRequestURL(), e.getMessage());
        return Result.error(e.getMessage());
    }
}
