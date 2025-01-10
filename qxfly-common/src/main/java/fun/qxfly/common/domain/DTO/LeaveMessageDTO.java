package fun.qxfly.common.domain.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "留言DTO")
public class LeaveMessageDTO {
    @Schema(description = "当前页")
    private Integer currPage;

    @Schema(description = "分页大小")
    private Integer pageSize;

    @Schema(description = "id")
    private Integer id;

    @Schema(description = "用户id")
    private Integer uid;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "发送时间")
    private Date date;

    @Schema(description = "发送时间范围-开始")
    private Date dateTimeStart;

    @Schema(description = "发送时间范围-结束")
    private Date dateTimeEnd;

    @Schema(description = "内容")
    private String content;


}
