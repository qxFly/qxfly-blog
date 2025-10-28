package fun.qxfly.common.domain.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "仓库")
public class Warehouse {
    @ExcelProperty(index = 0, value = "id")
    @Schema(description = "id")
    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER)
    private Integer id;


    @ExcelProperty(index = 2, value = "货架")
    @Schema(description = "货架")
    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER)
    private Integer shelf;

    @ExcelProperty(index = 3, value = "层数")
    @Schema(description = "层数")
    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER)
    private Integer layer;

    @ColumnWidth(50)
    @ExcelProperty(index = 1, value = "货物名称")
    @Schema(description = "货物名称")
    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER)
    private String name;

    @ExcelProperty(index = 4, value = "数量")
    @Schema(description = "数量")
    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER)
    private Integer quantity;

    @ExcelProperty(index = 5, value = "图片")
    @Schema(description = "图片")
    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER)
    private String picture;
}
