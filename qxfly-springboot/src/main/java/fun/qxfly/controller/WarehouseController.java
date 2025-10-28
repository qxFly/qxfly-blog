package fun.qxfly.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.github.pagehelper.PageInfo;
import fun.qxfly.common.domain.entity.Site;
import fun.qxfly.common.domain.entity.Warehouse;
import fun.qxfly.common.domain.po.Result;
import fun.qxfly.service.WarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@Slf4j
@Tag(name = "仓库", description = "仓库")
@RequestMapping("/warehouse")
public class WarehouseController {
    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    /**
     * @param currPage 当前页
     * @param pageSize 页大小
     * @param name     站点名称
     * @return 站点列表
     */
    @Operation(description = "列出库存", summary = "列出库存")
    @GetMapping("/listInventory")
    public Result listInventory(@RequestParam Integer currPage, @RequestParam Integer pageSize, @RequestParam(required = false) String name, @RequestParam(required = false) Integer shelf, @RequestParam(required = false) Integer layer) {
        PageInfo<Warehouse> pageInfo = warehouseService.listInventory(currPage, pageSize, name, shelf, layer);
        return Result.success(pageInfo);
    }

    /**
     * 入库
     *
     * @param warehouse
     * @return
     */
    @Operation(description = "入库", summary = "入库")
    @PostMapping("/warehousing")
    public Result warehousing(@RequestBody Warehouse warehouse) {
        Integer f = warehouseService.warehousing(warehouse);
        return Result.success();
    }

    /**
     * 编辑
     *
     * @param warehouse
     * @return
     */
    @Operation(description = "编辑", summary = "编辑")
    @PostMapping("/editWarehouse")
    public Result editWarehouse(@RequestBody Warehouse warehouse) {
        Integer f = warehouseService.editWarehouse(warehouse);
        return Result.success();
    }

    /**
     * 图片上传
     *
     * @param file
     * @return
     */
    @Operation(description = "图片上传", summary = "图片上传")
    @PostMapping("/updatePicture")
    public Result updateImg(MultipartFile file) {

        return null;
    }

    /**
     * 删除
     *
     * @param warehouse
     * @return
     */
    @Operation(description = "删除", summary = "删除")
    @PostMapping("/deleteStock")
    public Result deleteStock(@RequestBody Warehouse warehouse) {
        Integer f = warehouseService.deleteStock(warehouse);
        if (f > 0)
            return Result.success();
        else return Result.error("删除失败");
    }

    /**
     * @return 导出excel
     */
    @Operation(description = "导出excel", summary = "导出excel")
    @GetMapping("/getExcel")
    public void getExcel(HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("库存列表", StandardCharsets.UTF_8);
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            List<Warehouse> warehouseList = warehouseService.listAllStock();
            EasyExcel.write(response.getOutputStream())
                    .head(Warehouse.class)
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet("库存列表")
                    .doWrite(warehouseList);

        } catch (Exception e) {
            throw new RuntimeException("导出失败");
        }
    }
}
