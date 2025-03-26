package fun.qxfly.common.exception.excep;

import fun.qxfly.common.enums.ExceptionEnum;
import fun.qxfly.common.exception.BaseException;


public class JwtException extends BaseException {
    public JwtException(String module, Integer code, Object[] args, String defaultMessage) {
        super(module, code, args, defaultMessage);
    }

    JwtException() {
        super("token", ExceptionEnum.TOKEN_ERROR.getCode(), null, ExceptionEnum.TOKEN_ERROR.getMsg());
    }

    public JwtException(ExceptionEnum exceptionEnum) {
        super("token", exceptionEnum.getCode(), null, exceptionEnum.getMsg());
    }
}
