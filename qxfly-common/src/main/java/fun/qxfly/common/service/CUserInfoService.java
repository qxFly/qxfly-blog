package fun.qxfly.common.service;

import fun.qxfly.common.domain.entity.Token;

public interface CUserInfoService {

    /**
     * 刷新用户信息
     *
     * @param uid
     * @return
     */
    boolean refreshUserInfoTask(Integer uid);
}
