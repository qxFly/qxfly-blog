package fun.qxfly.admin.service;

public interface AdminService {

    /**
     * 判断用户是否为管理员
     *
     * @param username
     * @return
     */
    Integer check(String username);

}
