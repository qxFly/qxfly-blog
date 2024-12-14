package fun.qxfly.admin.service;

import fun.qxfly.common.domain.entity.Site;

public interface SiteManegeService {
    /**
     * 删除站点
     *
     * @param site
     * @return
     */
    boolean deleteSite(Site site);

    /**
     * 添加站点
     *
     * @param site
     * @return
     */
    boolean addSite(Site site);

    /**
     * 修改站点
     *
     * @param site
     * @return
     */
    boolean updateSite(Site site);

    /**
     * 获取站点状态
     *
     * @param id
     * @return
     */
    Integer getSiteStatus(Integer id);
}
