package fun.qxfly.common.enums;

import lombok.Getter;

@Getter
public enum ExceptionEnum {
    COMMON_ERROR(0, "操作失败"),

    TOKEN_EXPIRED(1101, "token已过期"),
    TOKEN_ERROR(1102, "token错误"),

    USER_LOGIN_EXPIRED(1201, "用户登录过期"),

    FILE_UPLOAD_ERROR(1301, "文件上传失败"),
    FILE_READ_ERROR(1302, "文件读取失败"),
    ;

    private final int code;
    private final String msg;

    ExceptionEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
