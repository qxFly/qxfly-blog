package fun.qxfly.admin.service;

public interface AdminService {

    /**
     * 判断用户是否为管理员
     *
     * @param uid uid
     * @return 角色代号。0：普通用户。1~5：管理员。
     */
    Integer check(Integer uid);

}
