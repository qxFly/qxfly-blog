package fun.qxfly.admin.service.impl;

import fun.qxfly.admin.service.SiteManegeService;
import fun.qxfly.admin.mapper.SiteManegeMapper;
import fun.qxfly.common.domain.entity.Site;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
@Service
public class SiteManegeServiceImpl implements SiteManegeService {

    private final SiteManegeMapper siteManegeMapper;

    public SiteManegeServiceImpl(SiteManegeMapper siteManegeMapper) {
        this.siteManegeMapper = siteManegeMapper;
    }

    /**
     * 删除站点
     *
     * @param site
     * @return
     */
    @Override
    public boolean deleteSite(Site site) {
        return siteManegeMapper.deleteSite(site);
    }

    /**
     * 添加站点
     *
     * @param site
     * @return
     */
    @Override
    public boolean addSite(Site site) {
        return siteManegeMapper.addSite(site);
    }

    /**
     * 更新站点
     *
     * @param site
     * @return
     */
    @Override
    public boolean updateSite(Site site) {
        return siteManegeMapper.updateSite(site);
    }

    /**
     * 获取站点状态
     *
     * @param id
     * @return
     */
    @Override
    public Integer getSiteStatus(Integer id) {
        String url = siteManegeMapper.getSiteUrlById(id);

        try {
            URL u = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) u.openConnection();
            return httpURLConnection.getResponseCode();
        } catch (Exception e) {
            e.printStackTrace();
            return 404;
        }
    }
}
