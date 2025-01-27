package fun.qxfly.admin.service.impl;

import com.alibaba.fastjson2.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import fun.qxfly.admin.mapper.ImageManageMapper;
import fun.qxfly.admin.service.ImageManageService;
import fun.qxfly.common.domain.DTO.ImageDTO;
import fun.qxfly.common.domain.entity.Image;
import fun.qxfly.common.domain.po.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ImageManageServiceImpl implements ImageManageService {
    @Autowired
    private ImageManageMapper imageManageMapper;
    @Value("${qxfly.file.path.articleImage}")
    private String articleImageDownloadPath;

    /**
     * 更新图库
     *
     * @return
     */
    @Override
    public Result updateImage() {
        HttpURLConnection con;
        try {
            URL reqUrl = new URL("https://api.github.com/repos/qxFly/qxfly-image/git/trees/main?recursive=1");
            con = (HttpURLConnection) reqUrl.openConnection();
            /*获取 GitHub Api*/
            String githubAPI = System.getProperty("GithubAPI");
            if (!githubAPI.isEmpty()) {
                con.setRequestProperty("Authorization", "token " + githubAPI);
            }
            con.connect();
            /*判断连接是否成功*/
            int responseCode = con.getResponseCode();
            if (responseCode != 200) {
                return Result.error("接口请求失败，" + responseCode + "错误！");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("接口请求超时！");
        }
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String line;
            if ((line = bufferedReader.readLine()) != null) {
                Map map = (Map) JSON.parse(line);
                List tree = (List) map.get("tree");
                String sha = (String) map.get("sha");// commit的sha值
                List<String> names = new ArrayList<>();
                int iflag = 0;
                int df = imageManageMapper.deleteAllGithubImage();
                for (int i = 2; i < tree.size(); i++) {
                    Map map1 = (Map) tree.get(i);
                    String a = (String) map1.get("path");
                    if (a.equals("run.bat") || a.equals("README.md")) continue;
                    String name = a.split("/")[1];
                    String url = "https://fastly.jsdelivr.net/gh/qxFly/qxfly-image@"+ sha + "/api/" + name;
                    boolean flag = imageManageMapper.addImage(name, url);
                    if (flag) {
                        iflag++;
                        names.add(url);
                    }
                }
                return Result.success("共添加了" + iflag + "张", names);
            } else {
                return Result.error("名称列表为空！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("名称列表读取错误！");
        }
    }

    /**
     * 分页查询图库
     *
     * @param imageDTO
     * @return
     */
    @Override
    public PageInfo<Image> getImagesByPage(ImageDTO imageDTO) {
        log.info("分页查询图库，参数：{}", imageDTO);
        String galleryType = System.getProperty("galleryType");
        if (galleryType != null && galleryType.equals("2d")) {
            PageHelper.startPage(imageDTO.getCurrPage(), imageDTO.getPageSize());
            List<Image> imageList =  imageManageMapper.getGithubImagesByPage(imageDTO);
            return new PageInfo<>(imageList);
        }else{
            PageHelper.startPage(imageDTO.getCurrPage(), imageDTO.getPageSize());
            List<Image> imageList = imageManageMapper.getImagesByPage(imageDTO);
            for (Image image : imageList) {
                image.setUrl(articleImageDownloadPath + image.getName());
            }
            return new PageInfo<>(imageList);
        }
    }
}
