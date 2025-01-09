package fun.qxfly.mapper;

import fun.qxfly.common.domain.entity.Navigation;
import fun.qxfly.common.domain.entity.Site;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IndexMapper {
    /**
     * 分页查询站点
     *
     * @param name
     * @return
     */
    @Select("select * from sites where name like concat('%',#{name},'%')")
    List<Site> listSites(String name);

    /**
     * 首页导航栏列表
     *
     * @return 首页导航栏列表
     */
    @Select("select * from navigation where type = 'index' or type = 'common'")
    List<Navigation> listIndexNav();
}
