package fun.qxfly.common.domain.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "英语实体类")
public class EnglishVocabulary {
    @Schema(description = "英语单词")
    private Integer id;

    @ExcelProperty("单词")
    @Schema(description = "英语单词")
    private String word;

    @ExcelProperty("音标")
    @Schema(description = "音标")
    private String phoneticSymbol;

    @ExcelProperty("释义")
    @Schema(description = "中文释义")
    private String interpretation;

    @Schema(description = "等级类型")
    private String levelType;
}
