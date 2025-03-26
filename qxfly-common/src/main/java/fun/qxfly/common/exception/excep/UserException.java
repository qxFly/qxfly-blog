package fun.qxfly.common.exception.excep;

import fun.qxfly.common.enums.ExceptionEnum;
import fun.qxfly.common.exception.BaseException;

public class UserException extends BaseException {
    public UserException(String module, Integer code, Object[] args, String defaultMessage) {
        super(module, code, args, defaultMessage);
    }

    public UserException() {
    }

    public UserException(ExceptionEnum exceptionEnum) {
        super("user", exceptionEnum.getCode(), null, exceptionEnum.getMsg());
    }
}
