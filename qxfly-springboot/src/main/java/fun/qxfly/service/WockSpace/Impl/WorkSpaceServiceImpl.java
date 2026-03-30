package fun.qxfly.service.WockSpace.Impl;

import fun.qxfly.common.domain.entity.Navigation;
import fun.qxfly.common.domain.vo.NavigationVO;
import fun.qxfly.common.utils.RoleUtils;
import fun.qxfly.mapper.WorkSpace.WorkSpaceMapper;
import fun.qxfly.service.WockSpace.WorkSpaceService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class WorkSpaceServiceImpl implements WorkSpaceService {
    final private WorkSpaceMapper workSpaceMapper;

    public WorkSpaceServiceImpl(WorkSpaceMapper workSpaceMapper) {
        this.workSpaceMapper = workSpaceMapper;
    }

    /**
     * 列出工作空间导航栏
     *
     * @return
     */
    @Override
    public List<NavigationVO> listWorkSpaceNavigations() {
        List<Navigation> parentNavs = workSpaceMapper.listWorkSpaceNavigations();
        List<NavigationVO> navigationVOS = new ArrayList<>();

        for (Navigation parentNav : parentNavs) {
            NavigationVO Pvo = new NavigationVO();
            BeanUtils.copyProperties(parentNav, Pvo);
            navigationVOS.add(Pvo);
        }
        navigationVOS.sort(Comparator.comparingInt(NavigationVO::getIndex));
        return navigationVOS;
    }
}
