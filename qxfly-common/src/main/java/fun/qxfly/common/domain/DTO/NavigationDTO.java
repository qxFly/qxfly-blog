package fun.qxfly.common.domain.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "导航栏DTO")
public class NavigationDTO {
    @Schema(description = "当前页")
    private Integer currPage;

    @Schema(description = "分页大小")
    private Integer pageSize;

    @Schema(description = "id")
    private Integer id;

    @Schema(description = "导航所属类别 admin、userSpace、index")
    private String type;

    @Schema(description = "导航栏名字")
    private String name;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "可访问角色")
    private Integer role;

    @Schema(description = "路径")
    private String path;

    @Schema(description = "导航索引")
    private Integer index;

    @Schema(description = "css类名")
    private String cssClass;

    @Schema(description = "父导航栏")
    private Integer parent;
}
