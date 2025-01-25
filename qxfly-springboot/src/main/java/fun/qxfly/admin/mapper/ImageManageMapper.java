package fun.qxfly.admin.mapper;

import fun.qxfly.common.domain.DTO.ImageDTO;
import fun.qxfly.common.domain.entity.Image;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ImageManageMapper {

    /**
     * 添加图片
     *
     * @param name
     * @param url
     */
    @Insert("insert into github_image(name, url)values(#{name}, #{url}) ")
    boolean addImage(@Param("name") String name,@Param("url")  String url);

    /**
     * 分页查询图库
     *
     * @param imageDTO
     * @return
     */
    List<Image> getImagesByPage(ImageDTO imageDTO);

    /**
     * 清空所有图片数
     *
     * @return
     */
    @Delete("delete from github_image")
    int deleteAllGithubImage();

    /**
     * 获取github图库
     * @return
     */
    List<Image> getGithubImagesByPage(ImageDTO imageDTO);
}
