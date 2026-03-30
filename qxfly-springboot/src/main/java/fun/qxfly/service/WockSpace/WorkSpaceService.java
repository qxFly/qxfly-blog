package fun.qxfly.service.WockSpace;

import com.github.pagehelper.PageInfo;
import fun.qxfly.common.domain.entity.Purchase;
import fun.qxfly.common.domain.vo.NavigationVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface WorkSpaceService {

    /**
     * 列出工作空间导航栏
     *
     * @return
     */
    List<NavigationVO> listWorkSpaceNavigations();
}
