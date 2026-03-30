package fun.qxfly.mapper.WorkSpace;

import fun.qxfly.common.domain.entity.Navigation;
import fun.qxfly.common.domain.entity.Purchase;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface WorkSpaceMapper {
    /**
     * 列出工作空间导航栏
     * @return
     */
    @Select("select * from navigation where type='workspace';")
    List<Navigation> listWorkSpaceNavigations();
}
