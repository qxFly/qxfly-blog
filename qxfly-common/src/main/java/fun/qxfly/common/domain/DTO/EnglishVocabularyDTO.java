package fun.qxfly.common.domain.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "留言DTO")
public class EnglishVocabularyDTO {
    @Schema(description = "当前页")
    private Integer currPage;

    @Schema(description = "分页大小")
    private Integer pageSize;

    @Schema(description = "英语单词")
    private Integer id;

    @Schema(description = "英语单词")
    private String word;

    @Schema(description = "音标")
    private String phoneticSymbol;

    @Schema(description = "中文释义")
    private String interpretation;

    @Schema(description = "等级类型")
    private String levelType;
}
