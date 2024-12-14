package fun.qxfly.admin.mapper;

import fun.qxfly.common.domain.entity.Site;
import org.apache.ibatis.annotations.*;

@Mapper
public interface SiteManegeMapper {
    /**
     * 删除站点
     *
     * @param site
     * @return
     */
    @Delete("delete from sites where id = #{id}")
    boolean deleteSite(Site site);

    /**
     * 添加站点
     *
     * @param site
     * @return
     */
    @Insert("insert into sites(name,url) values(#{name},#{url})")
    boolean addSite(Site site);

    /**
     * 更新站点
     * @param site
     * @return
     */
    @Update("update sites set name = #{name},url = #{url} where id = #{id}")
    boolean updateSite(Site site);

    /**
     * 根据id获取站点url
     * @param id
     * @return
     */
    @Select("select url from sites where id = #{id}")
    String getSiteUrlById(Integer id);
}
