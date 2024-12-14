package fun.qxfly.admin.controller;

import fun.qxfly.admin.service.SiteManegeService;
import fun.qxfly.common.domain.po.Result;
import fun.qxfly.common.domain.entity.Site;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/manage")
@Tag(name = "管理员")
@Tag(name = "站点管理")
public class SiteManegeController {
    private final SiteManegeService siteManegeService;

    public SiteManegeController(SiteManegeService siteManegeService) {
        this.siteManegeService = siteManegeService;
    }

    /**
     * 删除站点
     *
     * @return
     */
    @Operation(description = "删除站点", summary = "删除站点")
    @PostMapping("/deleteSite")
    public Result deleteSite(@RequestBody Site site) {
        boolean f = siteManegeService.deleteSite(site);
        return Result.success();
    }

    /**
     * 添加站点
     *
     * @return
     */
    @Operation(description = "添加站点", summary = "添加站点")
    @PostMapping("/addSite")
    public Result addSite(@RequestBody Site site) {
        if (site.getUrl() != null) {
            String url = site.getUrl().toLowerCase();
            if (url.contains("https://") || url.contains("http://")) {
                return siteManegeService.addSite(site) ? Result.success("添加成功") : Result.error("添加失败");
            } else {
                return Result.error("添加失败，请加上 https:// 或 http://");
            }
        }
        return Result.error("请输入链接");
    }

    /**
     * 修改站点
     *
     * @return
     */
    @Operation(description = "修改站点", summary = "修改站点")
    @PostMapping("/updateSite")
    public Result updateSite(@RequestBody Site site) {
        boolean f = siteManegeService.updateSite(site);
        return Result.success();
    }

    /**
     * 获取站点状态
     *
     * @return
     */
    @Operation(description = "修改站点", summary = "修改站点")
    @GetMapping("/getSiteStatus")
    public Result getSiteStatus(@RequestParam Integer id) {
        Integer status = siteManegeService.getSiteStatus(id);
        return Result.success(status);
    }


}
