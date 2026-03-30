package fun.qxfly.controller.WorkSpace;

import com.github.pagehelper.PageInfo;
import fun.qxfly.common.domain.entity.Purchase;
import fun.qxfly.common.domain.po.Result;
import fun.qxfly.common.domain.vo.NavigationVO;
import fun.qxfly.common.utils.JwtUtils;
import fun.qxfly.service.WockSpace.PurchaseService;
import fun.qxfly.service.WockSpace.WorkSpaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@Tag(name = "工作空间", description = "工作空间")
@RequestMapping("/workspace")
public class WorkSpaceController {

    private final WorkSpaceService WorkSpaceService;

    public WorkSpaceController(WorkSpaceService WorkSpaceService) {
        this.WorkSpaceService = WorkSpaceService;
    }

    /**
     * 列出工作空间导航栏
     *
     * @return
     */
    @GetMapping("listWorkSpaceNavigations")
    @Operation(description = "列出工作空间导航栏", summary = "列出工作空间导航栏")
    public Result listWorkSpaceNavigations(HttpServletRequest request) {
        List<NavigationVO> navigationList = WorkSpaceService.listWorkSpaceNavigations();
        return Result.success(navigationList);
    }

}
