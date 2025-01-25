package fun.qxfly.controller;

import com.github.pagehelper.PageInfo;
import fun.qxfly.common.domain.entity.Image;
import fun.qxfly.common.domain.po.Result;
import fun.qxfly.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@RestController
@CrossOrigin
@Tag(name = "图库")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @Value("${qxfly.file.articleImage.download.path}")
    private String articleImageDownloadPath;

    /**
     * 获取图片
     *
     * @param currPage
     * @param pageSize
     * @param sort
     * @return
     */
//    /* 图库缓存，避免重复多次调用数据库 */
//    List<Image> listCache = new ArrayList<>();
//    int cacheUsage = 0;
    @Operation(description = "获取图片，可传入数量", summary = "获取图片")
    @GetMapping("/getImage")
    public Result getImage(@RequestParam(defaultValue = "1") int currPage, @RequestParam int pageSize, @RequestParam(defaultValue = "new") String sort) {
        String galleryType = System.getProperty("galleryType");
        List<Image> images = new ArrayList<>();
        // 获取自己的二次元图库 galleryType == "2d"
        if (galleryType != null && galleryType.equals("2d")) {
            List<Image> allGithubImage = imageService.getAllGithubImage();
            for (int i = 0; i < pageSize; i++) {
                images.add(allGithubImage.get(new Random().nextInt(allGithubImage.size())));
            }
        } else {
            // 获取博客图库 galleryType == "blog"
            if (!sort.equals("random")) {
                PageInfo<Image> pageInfo = imageService.getImageByPage(currPage, pageSize, sort);
                return Result.success(pageInfo.getList());
            } else {
                List<Image> imageList = imageService.getAllImage();
                Random random = new Random();
                for (int i = 0; i < pageSize; i++) {
                    Image image = imageList.get(random.nextInt(imageList.size()));
                    image.setUrl(articleImageDownloadPath + image.getName());
                    images.add(image);
                }
                return Result.success(images);
            }
        }
        return Result.success(images);
    }
}
