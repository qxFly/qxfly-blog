package fun.qxfly.mapper.System;

import fun.qxfly.common.domain.entity.SysSetting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysSettingsMapper {
    /**
     * 获取系统设置
     *
     * @return
     */
    @Select("select * from sys_settings")
    SysSetting getSysSetting();
}
