package fun.qxfly.controller;

import com.github.pagehelper.PageInfo;
import fun.qxfly.common.domain.entity.Navigation;
import fun.qxfly.common.domain.entity.Site;
import fun.qxfly.common.domain.po.Result;
import fun.qxfly.common.utils.JwtUtils;
import fun.qxfly.service.IndexService;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@Tag(name = "首页", description = "首页")
@RequestMapping("/index")
public class IndexController {
    private final IndexService indexService;

    public IndexController(IndexService indexService) {
        this.indexService = indexService;
    }

    /**
     * @param currPage 当前页
     * @param pageSize 页大小
     * @param name     站点名称
     * @return 站点列表
     */
    @Operation(description = "右侧栏站点列表,列出站点", summary = "列出站点")
    @GetMapping("/listSite")
    public Result listSite(@RequestParam Integer currPage, @RequestParam Integer pageSize, @RequestParam String name) {
        PageInfo<Site> pageInfo = indexService.listSites(currPage, pageSize, name);
        return Result.success(pageInfo);
    }

    /**
     * 获取站点状态
     *
     * @param sites
     * @return
     */
    @Operation(description = "获取站点状态", summary = "获取站点状态")
    @PostMapping("/getSiteStatus")
    public Result getSiteStatus(@RequestBody List<Site> sites) {
        List<Site> status = indexService.getSiteStatus(sites);
        return Result.success(status);
    }


    /**
     * 列出首页导航栏
     *
     * @return 首页导航栏列表
     */
    @Operation(description = "列出首页导航栏", summary = "列出首页导航栏")
    @GetMapping("/listIndexNav")
    public Result listIndexNav(HttpServletRequest request) {
        String token = request.getHeader("token");
        String role = null;
        if (StringUtils.hasLength(token)) {
            Claims claims = JwtUtils.parseJWT(token);
            role = (String) claims.get("role");
        }
        List<Navigation> navigationList = indexService.listIndexNav(role);
        return Result.success(navigationList);
    }

    @GetMapping("/hello")
    public Result hello() {
           return Result.success("hello");
    }
}
