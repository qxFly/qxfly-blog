package fun.qxfly.common.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "采购实体类")
public class Purchase {
    @Schema(description = "id")
    private Integer id;
    @Schema(description = "申请人")
    private String applicant;
    @Schema(description = "已提报部分")
    private String reported;
    @Schema(description = "待提报部分")
    private String reporting;
    @Schema(description = "申请流程图片")
    private String picture;
    @Schema(description = "申请月份")
    private String month;
    @Schema(description = "采购状态")
    private Integer status;
}
