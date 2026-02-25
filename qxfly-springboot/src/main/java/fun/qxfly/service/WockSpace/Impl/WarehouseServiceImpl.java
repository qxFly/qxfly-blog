package fun.qxfly.service.WockSpace.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import fun.qxfly.common.domain.entity.Purchase;
import fun.qxfly.common.domain.entity.Site;
import fun.qxfly.common.domain.entity.Warehouse;
import fun.qxfly.common.enums.ExceptionEnum;
import fun.qxfly.common.enums.FilePaths;
import fun.qxfly.common.exception.excep.FileException;
import fun.qxfly.common.utils.FileUtils;
import fun.qxfly.mapper.WorkSpace.WarehouseMapper;
import fun.qxfly.service.WockSpace.WarehouseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
@Slf4j
public class WarehouseServiceImpl implements WarehouseService {

    @Value("${qxfly.file.path.warehousePicture}")
    private String warehousePicturePath;

    private final WarehouseMapper warehouseMapper;

    public WarehouseServiceImpl(WarehouseMapper warehouseMapper) {
        this.warehouseMapper = warehouseMapper;
    }

    /**
     * 列出库存
     *
     * @return
     */
    @Override
    public PageInfo<Warehouse> listInventory(Integer currPage, Integer pageSize, String name, Integer shelf, Integer layer) {
        PageHelper.startPage(currPage, pageSize);
        List<Warehouse> stock = warehouseMapper.listInventory(name, shelf, layer);
        stock.forEach(warehouse -> {
            warehouse.setPicture(warehousePicturePath + warehouse.getPicture());
        });
        return new PageInfo<>(stock);
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
     *
     * @param warehouse
     * @return
     */
    @Override
    public Integer warehousing(Warehouse warehouse) {
        return warehouseMapper.warehousing(warehouse);
    }

    /**
     * 删除库存
     *
     * @param warehouse
     * @return
     */
    @Override
    public Integer deleteStock(Warehouse warehouse) {
        Warehouse p = warehouseMapper.getItemById(warehouse.getId());
        /* 删除封面 */
        String path = FilePaths.WAREHOUSE_PICTURE_PATH.getPath() + p.getPicture();
        path = FileUtils.toSystemSeparator(path);
        File cover = new File(path);
        if (cover.exists()) cover.delete();
        return warehouseMapper.deleteStock(warehouse);
    }

    /**
     * 列出所有库存
     *
     * @return
     */
    @Override
    public List<Warehouse> listAllStock() {
        return warehouseMapper.listAllStock();
    }

    /**
     * 修改库存
     *
     * @param warehouse
     * @return
     */
    @Override
    public Integer editWarehouse(Warehouse warehouse) {
        Warehouse itemById = warehouseMapper.getItemById(warehouse.getId());
        String oldPic = itemById.getPicture();
        String[] split = warehouse.getPicture().split("warehousePicture/");
        String newPic = "";
        if (split.length > 1) {
            newPic = split[1];
            warehouse.setPicture(newPic);
        }
        log.info("newPic:{}", newPic);
        log.info("oldPic:{}", oldPic);
        if (!Objects.equals(oldPic, newPic)) {
            /* 删除封面 */
            String path = FilePaths.WAREHOUSE_PICTURE_PATH.getPath() + oldPic;
            path = FileUtils.toSystemSeparator(path);
            if (path != null) {
                File cover = new File(path);
                if (cover.exists()) cover.delete();
            }

        }
        return warehouseMapper.editWarehouse(warehouse);
    }

    /**
     * 图片上传
     *
     * @param file
     * @return
     */
    @Override
    public String uploadPicture(MultipartFile file) {
        try {
            return FileUtils.upload(FilePaths.WAREHOUSE_PICTURE_PATH.getPath(), file);
        } catch (Exception e) {
            throw new FileException(ExceptionEnum.FILE_UPLOAD_ERROR);
        }
    }
}
