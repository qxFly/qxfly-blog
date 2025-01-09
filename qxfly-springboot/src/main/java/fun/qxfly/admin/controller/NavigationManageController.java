package fun.qxfly.admin.controller;


import com.github.pagehelper.PageInfo;
import fun.qxfly.admin.service.NavigationManageService;
import fun.qxfly.common.domain.DTO.NavigationDTO;
import fun.qxfly.common.domain.entity.Navigation;
import fun.qxfly.common.domain.po.Result;
import fun.qxfly.common.domain.vo.NavigationVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/manage")
@Tag(name = "导航栏管理")
@Tag(name = "管理员")
public class NavigationManageController {
    private final NavigationManageService navigationManageService;

    public NavigationManageController(NavigationManageService navigationManageService) {
        this.navigationManageService = navigationManageService;
    }

    /**
     * 列出管理员导航栏
     *
     * @return
     */
    @GetMapping("listAdminNavigations")
    @Operation(description = "列出管理员导航栏", summary = "列出管理员导航栏")
    public Result listAdminNavigations() {
        List<NavigationVO> navigationList = navigationManageService.listAdminNavigations();
        return Result.success(navigationList);
    }

    /**
     * 分页列出导航栏
     *
     * @param navigationDTO
     * @return
     */
    @PostMapping("listNavigations")
    @Operation(description = "分页列出导航栏", summary = "分页列出导航栏")
    public Result listNavigations(@RequestBody NavigationDTO navigationDTO) {
        PageInfo<Navigation> pageInfo = navigationManageService.listNavigations(navigationDTO);
        return Result.success(pageInfo);
    }

    /**
     * 新增导航栏
     *
     * @param navigation
     * @return
     */
    @PostMapping("addNavigation")
    @Operation(description = "新增导航栏", summary = "分页列出导航栏")
    public Result addNavigation(@RequestBody Navigation navigation) {
        boolean f = navigationManageService.addNavigation(navigation);
        if (f) return Result.success();
        else return Result.error("新增失败");
    }

    /**
     * 编辑导航栏
     *
     * @param navigation
     * @return
     */
    @PostMapping("updateNavigation")
    @Operation(description = "编辑导航栏", summary = "编辑导航栏")
    public Result updateNavigation(@RequestBody Navigation navigation) {
        boolean f = navigationManageService.updateNavigation(navigation);
        if (f) return Result.success();
        else return Result.error("编辑失败");
    }

    /**
     * 删除导航栏
     *
     * @param navigation
     * @return
     */
    @PostMapping("deleteNavigation")
    @Operation(description = "删除导航栏", summary = "删除导航栏")
    public Result deleteNavigation(@RequestBody Navigation navigation) {
        boolean f = navigationManageService.deleteNavigation(navigation);
        if (f) return Result.success();
        else return Result.error("编辑失败");
    }
}
