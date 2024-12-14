package fun.qxfly.framework.service;

public interface InterceptorService {
    /**
     * 获取退出状态信息
     *
     * @param token
     */
    String getLogoutStatus(String token);

    /**
     * 判断用户是否为管理员
     *
     * @param username
     * @return
     */
    boolean isAdmin(String username);

    /**
     * 判断是否需要拦截
     * @param path
     * @return
     */
    boolean pathBypass(String path);
}
