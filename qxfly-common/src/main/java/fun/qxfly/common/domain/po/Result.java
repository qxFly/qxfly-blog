package fun.qxfly.common.domain.po;

import fun.qxfly.common.enums.ExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Integer code;//响应码，1 代表成功; 0 代表失败
    private String msg;  //响应信息 描述字符串
    private Object data; //返回的数据

    //增删改 成功响应
    public static Result success() {
        return new Result(1, "success", null);
    }

    //查询 成功响应
    public static Result success(Object data) {
        return new Result(1, "success", data);
    }

    public static Result success(String msg, Object data) {
        return new Result(1, msg, data);
    }

    //失败响应
    public static Result error(String msg) {
        return new Result(0, msg, null);
    }

    //失败响应
    public static Result error(String msg, Object data) {
        return new Result(0, msg, data);
    }

    //失败响应
    public static Result error(ExceptionEnum exceptionEnum, Object data) {
        return new Result(exceptionEnum.getCode(), exceptionEnum.getMsg(), data);
    }

    //失败响应
    public static Result error(ExceptionEnum exceptionEnum) {
        return new Result(exceptionEnum.getCode(), exceptionEnum.getMsg(), null);
    }

    //失败响应，未登入
    public static Result noLoginError() {
        return new Result(0, "未登入", null);
    }

    //失败响应，未登入
    public static Result operationError() {
        return new Result(0, "操作失败", null);
    }


}
