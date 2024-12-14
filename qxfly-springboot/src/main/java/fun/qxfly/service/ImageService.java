package fun.qxfly.service;

import com.github.pagehelper.PageInfo;
import fun.qxfly.common.domain.entity.Image;

import java.util.List;

public interface ImageService {
    /**
     * 获取所有图片
     *
     * @return
     */
    List<Image> getAllImage();


    /**
     * 分页获取图片
     *
     * @param currPage
     * @param pageSize
     * @param sort
     * @return
     */
    PageInfo<Image> getImageByPage(int currPage, int pageSize, String sort);

    /**
     * 获取所有github图片
     * @return
     */
    List<Image> getAllGithubImage();
}
