package fun.qxfly.service;

import com.github.pagehelper.PageInfo;
import fun.qxfly.common.domain.entity.Navigation;
import fun.qxfly.common.domain.entity.Site;

import java.io.IOException;
import java.util.List;

public interface IndexService {
    /**
     * 列出网站
     *
     * @return
     */
    PageInfo<Site> listSites(Integer currPage, Integer pageSize, String name) ;

    /**
     * 首页导航栏列表
     *
     * @return 首页导航栏列表
     */
    List<Navigation> listIndexNav();

    /**
     * 获取站点状态
     * @param sites
     * @return
     */
    List<Site> getSiteStatus(List<Site> sites);
}
