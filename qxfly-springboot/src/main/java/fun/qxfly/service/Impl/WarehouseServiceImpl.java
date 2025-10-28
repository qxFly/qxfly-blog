package fun.qxfly.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import fun.qxfly.common.domain.entity.Navigation;
import fun.qxfly.common.domain.entity.Site;
import fun.qxfly.common.domain.entity.Warehouse;
import fun.qxfly.common.utils.RoleUtils;
import fun.qxfly.mapper.WarehouseMapper;
import fun.qxfly.service.WarehouseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
@Slf4j
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseMapper warehouseMapper;

    public WarehouseServiceImpl(WarehouseMapper warehouseMapper) {
        this.warehouseMapper = warehouseMapper;
    }

    /**
     * 列出网站
     *
     * @return
     */
    @Override
    public PageInfo<Warehouse> listInventory(Integer currPage, Integer pageSize, String name, Integer shelf, Integer layer) {
        PageHelper.startPage(currPage, pageSize);
        List<Warehouse> sites = warehouseMapper.listInventory(name,shelf,layer);
        return new PageInfo<>(sites);
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

    /**
     * 入库
     * @param warehouse
     * @return
     */
    @Override
    public Integer warehousing(Warehouse warehouse) {
        return warehouseMapper.warehousing(warehouse);
    }

    /**
     * 删除库存
     * @param warehouse
     * @return
     */
    @Override
    public Integer deleteStock(Warehouse warehouse) {
        return warehouseMapper.deleteStock(warehouse);
    }

    /**
     * 列出所有库存
     * @return
     */
    @Override
    public List<Warehouse> listAllStock() {
        return warehouseMapper.listAllStock();
    }

    /**
     * 修改库存
     * @param warehouse
     * @return
     */
    @Override
    public Integer editWarehouse(Warehouse warehouse) {
        return warehouseMapper.editWarehouse(warehouse);
    }
}
