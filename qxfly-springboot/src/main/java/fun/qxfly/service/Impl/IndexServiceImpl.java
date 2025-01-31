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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;

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

    @Override
    public List<Site> getSiteStatus(List<Site> sites) {
        // 检查网站是否可用
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        List<Site> status = new ArrayList<>();
        for (Site site : sites) {
            Future<Site> submit = executorService.submit(() -> {
                HttpURLConnection httpURLConnection;
                try {
                    URL url = new URL(site.getUrl());
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    int responseCode = httpURLConnection.getResponseCode();
                    site.setStatus(responseCode);
                } catch (Exception e) {
                    site.setStatus(404);
                    e.printStackTrace();
                }
                return site;
            });
            try {
                status.add(submit.get());
            } catch (Exception e) {
               e.printStackTrace();
            }
        }
        executorService.shutdown();
        return status;
    }
}
