package fun.qxfly.common.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSettingVO {

    @Schema(description = "uid")
    private Integer uid;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "背景图片地址")
    private String bgImgPath;

    @Schema(description = "背景图片开关(0、关，1、开)")
    private Integer bgSwitch;

    @Schema(description = "背景图片模糊（单位:px）")
    private Integer bgBlur;
}
