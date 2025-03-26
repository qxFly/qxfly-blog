package fun.qxfly.common.exception.excep;

import fun.qxfly.common.enums.ExceptionEnum;
import fun.qxfly.common.exception.BaseException;

public class FileException extends BaseException {
    public FileException(String module, Integer code, Object[] args, String defaultMessage) {
        super(module, code, args, defaultMessage);
    }

    public FileException() {
    }

    public FileException(ExceptionEnum exceptionEnum) {
        super("File", exceptionEnum.getCode(), null, exceptionEnum.getMsg());
    }
}
