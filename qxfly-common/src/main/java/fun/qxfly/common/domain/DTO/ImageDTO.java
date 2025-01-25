package fun.qxfly.common.domain.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "图片DTO类")
public class ImageDTO {
    @Schema(description = "当前页")
    private Integer currPage = 1;

    @Schema(description = "分页大小")
    private Integer pageSize = 10;

    @Schema(description = "是否开启自己的二次元图库")
    private boolean isMyImage;

    @Schema(description = "id")
    private Integer id;

    @Schema(description = "文章id")
    private Integer aid;

    @Schema(description = "名字")
    private String name;

    @Schema(description = "原始名字")
    private String originName;

    @Schema(description = "链接")
    private String url;

    @Schema(description = "创建时间-起始")
    String createTimeStart;

    @Schema(description = "创建时间-结束")
    String createTimeEnd;

    @Schema(description = "审核")
    private Integer verify;

    @Schema(description = "排序")
    private String sort;
}
