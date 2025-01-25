package fun.qxfly.admin.service;

import com.github.pagehelper.PageInfo;
import fun.qxfly.common.domain.DTO.ImageDTO;
import fun.qxfly.common.domain.po.Result;
import fun.qxfly.common.domain.entity.Image;

public interface ImageManageService {
    /**
     * 更新图库
     *
     * @return
     */
    Result updateImage();

    /**
     * 分页查询图库
     * @param imageDTO
     * @return
     */
    PageInfo<Image> getImagesByPage(ImageDTO imageDTO);
}
