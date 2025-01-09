package fun.qxfly.admin.mapper;

import fun.qxfly.common.domain.DTO.NavigationDTO;
import fun.qxfly.common.domain.entity.Navigation;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NavigationManageMapper {

    /**
     * 列出管理员导航栏
     */
    @Select("select * from navigation where (type = 'admin' or type = 'common') and parent IS NULL")
    List<Navigation> listAdminNavigations();

    /**
     * 分页列出导航栏
     *
     * @param navigationDTO
     */
    List<Navigation> listAdminNavByPage(NavigationDTO navigationDTO);


    /**
     * 新增导航栏
     *
     * @param navigation
     */
    @Insert("insert into navigation(name, icon, path, css_class, role, type,`index`) values(#{name}, #{icon}, #{path}, #{cssClass}, #{role}, #{type}, #{index})")
    boolean addNavigation(Navigation navigation);

    /**
     * 编辑导航栏
     *
     * @param navigation
     * @return
     */
    @Update("update navigation set name=#{name}, icon=#{icon}, path=#{path}, css_class=#{cssClass}, role=#{role}, type=#{type}, `index`= #{index} where id=#{id}")
    boolean updateNavigation(Navigation navigation);

    /**
     * 删除导航栏
     *
     * @param navigation
     * @return
     */
    @Delete("delete from navigation where id=#{id}")
    boolean deleteNavigation(Navigation navigation);

    /**
     * 列出所有子导航栏
     *
     * @return
     */
    @Select("select * from navigation where parent=#{id}")
    List<Navigation> listChildrenNavigations(Integer id);
}
