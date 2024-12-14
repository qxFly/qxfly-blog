package fun.qxfly.admin.mapper;

import fun.qxfly.common.domain.entity.Image;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ImageManageMapper {
    /**
     * 查询数据库是否有相同名字的图片
     *
     * @param name
     * @return
     */
    @Select("select id from github_image where name = #{name}")
    Integer getIdByName(String name);

    /**
     * 添加图片
     *
     * @param name
     * @param url
     */
    @Insert("insert into github_image(name, url)values(#{name}, #{url}) ")
    boolean addImage(String name, String url);

    /**
     * 分页查询图库
     *
     * @return
     */
    @Select("select id, name, url from image limit #{startPage}, #{pageSize}")
    List<Image> getAllImageInfo(int startPage, int pageSize);

    /**
     * 获取所有图片数
     *
     * @return
     */
    @Select("select count(id) from image")
    int getAllImagesCount();

    /**
     * 获取所有图片
     *
     * @return
     */
    @Select("select * from image")
    List<Image> getAllImages();

    /**
     * 分页查询图库
     *
     * @param createTimeStart
     * @param createTimeEnd
     * @return
     */
    List<Image> getImagesByPage(@Param("aid") Integer aid, @Param("originName") String originName, @Param("createTimeStart") String createTimeStart, @Param("createTimeEnd") String createTimeEnd, @Param("verify") Integer verify);

    /**
     * 清空所有图片数
     *
     * @return
     */
    @Delete("delete from github_image")
    int deleteAllGithubImage();
}
