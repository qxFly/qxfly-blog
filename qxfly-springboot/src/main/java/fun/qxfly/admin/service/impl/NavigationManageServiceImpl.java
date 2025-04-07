package fun.qxfly.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import fun.qxfly.admin.mapper.NavigationManageMapper;
import fun.qxfly.admin.service.NavigationManageService;
import fun.qxfly.common.domain.DTO.NavigationDTO;
import fun.qxfly.common.domain.entity.Navigation;
import fun.qxfly.common.domain.vo.NavigationVO;
import fun.qxfly.common.utils.RoleUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class NavigationManageServiceImpl implements NavigationManageService {

    private final NavigationManageMapper navigationManageMapper;

    public NavigationManageServiceImpl(NavigationManageMapper navigationManageMapper) {
        this.navigationManageMapper = navigationManageMapper;
    }

    /**
     * 列出管理员导航栏
     *
     * @return
     */
    @Override
    public List<NavigationVO> listAdminNavigations(String role) {
        Integer roleIdByRoleName = RoleUtils.getRoleIdByRoleName(role);
        List<Navigation> parentNavs = navigationManageMapper.listAdminNavigations(roleIdByRoleName);
        List<NavigationVO> navigationVOS = new ArrayList<>();
        for (Navigation parentNav : parentNavs) {
            List<Navigation> childrenList = navigationManageMapper.listChildrenNavigations(parentNav.getId());
            List<NavigationVO> childrenVOList = new ArrayList<>();
            for (Navigation child : childrenList) {
                NavigationVO Cvo = new NavigationVO();
                BeanUtils.copyProperties(child, Cvo);
                childrenVOList.add(Cvo);
            }
            NavigationVO Pvo = new NavigationVO();
            BeanUtils.copyProperties(parentNav, Pvo);
            Pvo.setChildren(childrenVOList);
            navigationVOS.add(Pvo);
        }
        navigationVOS.sort(Comparator.comparingInt(NavigationVO::getIndex));
        return navigationVOS;
    }

    /**
     * 分页列出导航栏
     *
     * @param navigationDTO
     * @return
     */
    @Override
    public PageInfo<Navigation> listNavigations(NavigationDTO navigationDTO) {
        PageHelper.startPage(navigationDTO.getCurrPage(), navigationDTO.getPageSize());
        List<Navigation> navigations = navigationManageMapper.listAdminNavByPage(navigationDTO);
        PageInfo<Navigation> pageInfo = new PageInfo<>(navigations);
        return pageInfo;
    }

    /**
     * 新增导航栏
     *
     * @param navigation
     * @return
     */
    @Override
    public boolean addNavigation(Navigation navigation) {
        ;
        return navigationManageMapper.addNavigation(navigation);
    }

    /**
     * 编辑导航栏
     *
     * @param navigation
     * @return
     */
    @Override
    public boolean updateNavigation(Navigation navigation) {
        return navigationManageMapper.updateNavigation(navigation);
    }

    /**
     * 删除导航栏
     *
     * @param navigation
     * @return
     */
    @Override
    public boolean deleteNavigation(Navigation navigation) {
        return navigationManageMapper.deleteNavigation(navigation);
    }
}
