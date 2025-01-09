package fun.qxfly.admin.service;

import com.github.pagehelper.PageInfo;
import fun.qxfly.common.domain.DTO.NavigationDTO;
import fun.qxfly.common.domain.entity.Navigation;
import fun.qxfly.common.domain.vo.NavigationVO;

import java.util.List;

public interface NavigationManageService {
    /**
     * 列出管理员导航栏
     *
     * @return
     */
    List<NavigationVO> listAdminNavigations();

    /**
     * 分页列出导航栏
     *
     * @param navigationDTO
     * @return
     */
    PageInfo<Navigation> listNavigations(NavigationDTO navigationDTO);

    /**
     * 新增导航栏
     *
     * @param navigation
     * @return
     */
    boolean addNavigation(Navigation navigation);

    /**
     * 编辑导航栏
     *
     * @param navigation
     * @return
     */
    boolean updateNavigation(Navigation navigation);

    /**
     * 删除导航栏
     * @param navigation
     * @return
     */
    boolean deleteNavigation(Navigation navigation);
}
