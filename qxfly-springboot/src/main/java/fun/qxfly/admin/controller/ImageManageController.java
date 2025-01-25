package fun.qxfly.admin.controller;

import com.github.pagehelper.PageInfo;
import fun.qxfly.admin.service.ImageManageService;
import fun.qxfly.common.domain.DTO.ImageDTO;
import fun.qxfly.common.domain.entity.Image;
import fun.qxfly.common.domain.po.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/manage")
@Tag(name = "图库")
@Tag(name = "管理员")
public class ImageManageController {
    private final ImageManageService imageManageService;

    public ImageManageController(ImageManageService imageManageService) {
        this.imageManageService = imageManageService;
    }

    /**
     * 更新图库
     *
     * @return
     */
    @Operation(description = "更新图库", summary = "更新图库")
    @GetMapping("/updateImage")
    public Result updateImage() {
        return imageManageService.updateImage();
    }

    /**
     * 分页查询图库
     *
     * @return
     */
    @Operation(description = "分页查询图库", summary = "分页查询图库")
    @PostMapping("/listImages")
    public Result getImagesByPage(@RequestBody ImageDTO imageDTO) {
        PageInfo<Image> pageBean = imageManageService.getImagesByPage(imageDTO);
        return Result.success(pageBean);
    }

    /**
     * 删除图片
     *
     * @param id
     * @return
     */
    @Operation(description = "删除图片", summary = "删除图片")
    @GetMapping("/deleteImage")
    public Result deleteImage(@RequestParam Integer id) {
        return Result.success();
    }

    /**
     * 获取图库类型
     * 2d:自己的二次元图片，blog:博客图片
     *
     * @return
     */
    @Operation(description = "获取图库类型,2d:自己的二次元图片，blog:博客图片", summary = "获取图库类型")
    @GetMapping("/getGalleryType")
    public Result getGalleryType() {
        String sysGalleryType = System.getProperty("galleryType");
        if (sysGalleryType == null) {
            return Result.error("获取图库类型失败，默认为博客图片");
        } else {
            return Result.success(sysGalleryType);
        }
    }

    /**
     * 切换图库
     * 2d:自己的二次元图片，blog:博客图片
     *
     * @return
     */
    @Operation(description = "切换图库", summary = "切换图库")
    @GetMapping("/changeGallery")
    public Result changeGallery(@RequestParam String type) {
        System.setProperty("galleryType", type);
        String sysGalleryType = System.getProperty("galleryType");
        if (type.equals(sysGalleryType)) {
            return Result.success();
        }
        return Result.error("切换图库失败");
    }
}
