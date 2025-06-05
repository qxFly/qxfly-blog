package fun.qxfly.common.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSetting {

    @Schema(description = "id")
    private Integer id;

    @Schema(description = "uid")
    private Integer uid;

    @Schema(description = "背景图片地址")
    private String bgImgPath;

    @Schema(description = "背景图片开关(0、关，1、开)")
    private Integer bgSwitch;

    @Schema(description = "背景图片模糊（单位:px）")
    private Integer bgBlur;

    @Schema(description = "字体颜色")
    private String fontColor;
}
