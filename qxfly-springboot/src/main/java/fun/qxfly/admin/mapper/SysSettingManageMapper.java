package fun.qxfly.admin.mapper;

import fun.qxfly.common.domain.entity.SysSetting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SysSettingManageMapper {

    /**
     * 更新系统设置
     *
     * @param sysSetting
     * @return
     */
    @Update("update sys_settings set bgSwitch = #{bgSwitch}, bgImgPath = #{bgImgPath}, bgBlur = #{bgBlur}, fontColor = #{fontColor} where id = 1")
    boolean updateSysSetting(SysSetting sysSetting);

}
