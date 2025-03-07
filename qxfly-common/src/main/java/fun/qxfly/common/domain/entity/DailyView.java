package fun.qxfly.common.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "日浏览量实体类")
public class DailyView {
    @Schema(description = "文章id")
    private Integer aid;
    @Schema(description = "浏览量,当做点赞、收藏和取消点赞、收藏的标志：0则操作，1则取消")
    private Integer daily_views;
    @Schema(description = "浏览量")
    private Integer Weekly_views;
    @Schema(description = "浏览量")
    private Integer Monthly_views;
}
