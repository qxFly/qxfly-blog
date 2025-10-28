package fun.qxfly.controller;

import com.alibaba.fastjson2.JSONObject;
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
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @PostMapping("/jpConvert")
    public Result jpConvert(@RequestBody Map map) {
        String o = (String) map.get("jp");
        HttpPost post = new HttpPost("https://api.okmiku.com/kuroshiro");
        try {
            //创建参数集合
            List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
            //添加参数
            list.add(new BasicNameValuePair("content", o));
            list.add(new BasicNameValuePair("mode", "normal"));
            list.add(new BasicNameValuePair("to", "hiragana"));
            list.add(new BasicNameValuePair("romajiSystem", "nippon"));
            post.addHeader("Authsign", "82a9c284748cdea6bc716f0db692c47be85cece564ee74d088f30fe6992e2ee0.U2FsdGVkX18MURVjdrr5UaBbeyiEaSVnMZcPq6Gg+0g=.MzIwOTIzNjE3Ng==");
            post.addHeader("Origin", "https://tools.miku.ac");
            post.addHeader("Referer", "https://tools.miku.ac");
            //把参数放入请求对象，，post发送的参数list，指定格式
            post.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
            //添加请求头参数
//            post.addHeader("timestamp","1594695607545");
            CloseableHttpClient client = HttpClients.createDefault();
            //启动执行请求，并获得返回值
            CloseableHttpResponse response = client.execute(post);
            //得到返回的entity对象
            HttpEntity entity = response.getEntity();
            //把实体对象转换为string
            String result = EntityUtils.toString(entity, "UTF-8");
            log.info("result:{}", result);
            JSONObject from = JSONObject.parseObject(result);
            result = from.get("data").toString();
            log.info("返回内容:{}", from.get("data"));
            return Result.success("result");
        } catch (Exception e1) {
            e1.printStackTrace();
            return Result.error("error");
        }
    }

    @PostMapping("/jpConvert1")
    public Result jpConvert1(@RequestBody Map map) {
        return Result.success();
    }
}
