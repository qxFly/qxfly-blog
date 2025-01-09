package fun.qxfly.common.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "管理员导航栏")
public class Navigation {
    @Schema(description = "id")
    private Integer id;

    @Schema(description = "导航栏名字")
    private String name;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "可访问角色")
    private Integer role;

    @Schema(description = "路径")
    private String path;

    @Schema(description = "css类名")
    private String cssClass;

    @Schema(description = "父导航栏")
    private Integer parent;

    @Schema(description = "导航所属类别 admin、userSpace、index")
    private String type;

    @Schema(description = "导航索引")
    private Integer index;


}
