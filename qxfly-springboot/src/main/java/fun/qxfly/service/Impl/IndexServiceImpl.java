package fun.qxfly.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import fun.qxfly.common.domain.entity.Navigation;
import fun.qxfly.common.domain.entity.Site;
import fun.qxfly.mapper.IndexMapper;
import fun.qxfly.service.IndexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class IndexServiceImpl implements IndexService {

    private final IndexMapper indexMapper;

    public IndexServiceImpl(IndexMapper indexMapper) {
        this.indexMapper = indexMapper;
    }

    /**
     * 列出网站
     *
     * @return
     */
    @Override
    public PageInfo<Site> listSites(Integer currPage, Integer pageSize, String name) {
        PageHelper.startPage(currPage, pageSize);
        List<Site> sites = indexMapper.listSites(name);
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

    /**
     * 首页导航栏列表
     *
     * @return 首页导航栏列表
     */
    @Override
    public List<Navigation> listIndexNav() {
        List<Navigation> navigations = indexMapper.listIndexNav();
        navigations.sort(Comparator.comparingInt(Navigation::getIndex));
        return navigations;
    }
}
