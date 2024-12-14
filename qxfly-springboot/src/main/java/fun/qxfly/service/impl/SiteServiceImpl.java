package fun.qxfly.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import fun.qxfly.common.domain.entity.Site;
import fun.qxfly.mapper.SiteMapper;
import fun.qxfly.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class SiteServiceImpl implements SiteService {
    @Autowired
    private SiteMapper siteMapper;


    /**
     * 列出网站
     *
     * @return
     */
    @Override
    public PageInfo<Site> listSites(Integer currPage, Integer pageSize, String name) {
        PageHelper.startPage(currPage, pageSize);
        List<Site> sites = siteMapper.listSites(name);
        // 检查网站是否可用

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (Site site : sites) {
            executorService.submit(() -> {
                try {
                    HttpURLConnection httpURLConnection;
                    URL url = new URL(site.getUrl());
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    site.setStatus(httpURLConnection.getResponseCode());
                } catch (Exception e) {
                    e.printStackTrace();
                    site.setStatus(404);
                }
            });
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new PageInfo<>(sites);
    }
}
