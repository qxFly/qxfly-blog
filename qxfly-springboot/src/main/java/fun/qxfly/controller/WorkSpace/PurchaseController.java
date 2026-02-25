package fun.qxfly.controller.WorkSpace;

import com.github.pagehelper.PageInfo;
import fun.qxfly.common.domain.entity.Purchase;
import fun.qxfly.common.domain.po.Result;
import fun.qxfly.service.WockSpace.PurchaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@Tag(name = "采购", description = "采购")
@RequestMapping("/purchase")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService warehouseService) {
        this.purchaseService = warehouseService;
    }

    /**
     * 列出库存
     *
     * @param currPage
     * @param pageSize
     * @param applicant
     * @param item
     * @param status
     * @return
     */
    @Operation(description = "列出项目", summary = "列出项目")
    @GetMapping("/listItems")
    public Result listItems(@RequestParam Integer currPage, @RequestParam Integer pageSize,
                            @RequestParam(required = false) String applicant,
                            @RequestParam(required = false) String reporting,
                            @RequestParam(required = false) String reported,
                            @RequestParam(required = false) Integer status,
                            @RequestParam(required = false) String month) {
        log.info("status:{}", status);
        PageInfo<Purchase> pageInfo = purchaseService.listItems(currPage, pageSize, applicant, reporting, reported, status, month);
        return Result.success(pageInfo);
    }

    /**
     * 添加项目
     *
     * @param warehouse
     * @return
     */
    @Operation(description = "添加项目", summary = "添加项目")
    @PostMapping("/addItem")
    public Result addItem(@RequestBody Purchase purchase) {
        Integer f = purchaseService.addItem(purchase);
        return Result.success();
    }

    /**
     * 编辑
     *
     * @param warehouse
     * @return
     */
    @Operation(description = "编辑", summary = "编辑")
    @PostMapping("/editItem")
    public Result editItem(@RequestBody Purchase purchase) {
        Integer f = purchaseService.editItem(purchase);
        return Result.success();
    }

    /**
     * 图片上传
     *
     * @param file
     * @return
     */
    @Operation(description = "图片上传", summary = "图片上传")
    @PostMapping("/uploadPicture")
    public Result uploadPicture(MultipartFile file) {
        String path = purchaseService.uploadPicture(file);
        return Result.success(path);
    }

    /**
     * 删除
     *
     * @param purchase
     * @return
     */
    @Operation(description = "删除", summary = "删除")
    @PostMapping("/deleteItem")
    public Result deleteItem(@RequestBody Purchase purchase) {
        Integer f = purchaseService.deleteItem(purchase);
        if (f > 0)
            return Result.success();
        else return Result.error("删除失败");
    }
}
