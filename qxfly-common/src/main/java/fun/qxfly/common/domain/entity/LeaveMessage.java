package fun.qxfly.common.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "留言")
public class LeaveMessage {
    @Schema(description = "id")
    private Integer id;

    @Schema(description = "用户id")
    private Integer uid;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "发送时间")
    private Date date;

    @Schema(description = "内容")
    private String content;
}