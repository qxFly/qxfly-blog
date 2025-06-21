package fun.qxfly.controller;

import com.github.pagehelper.PageInfo;
import fun.qxfly.common.domain.DTO.EnglishVocabularyDTO;
import fun.qxfly.common.domain.entity.EnglishVocabulary;
import fun.qxfly.common.domain.po.Result;
import fun.qxfly.service.EnglishVocabularyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/englishVocabulary")
@Slf4j
@Tag(name = "英语词汇接口")
public class EnglishVocabularyController {

    private final EnglishVocabularyService englishVocabularyService;

    public EnglishVocabularyController(EnglishVocabularyService englishVocabularyService) {
        this.englishVocabularyService = englishVocabularyService;
    }

    /**
     * 查询英语单词
     *
     * @param englishVocabularyDTO 英语单词查询条件
     * @return 英语单词列表
     */
    @PostMapping("/getVocabularies")
    @Operation(description = "获取单词", summary = "获取单词")
    public Result getVocabularies(@RequestBody EnglishVocabularyDTO englishVocabularyDTO) {
        log.info("查询英语单词:{}", englishVocabularyDTO);
        PageInfo<EnglishVocabulary> pageInfo = englishVocabularyService.getVocabularies(englishVocabularyDTO);
        return Result.success(pageInfo);
    }

    /**
     * 获取随机英语词汇
     *
     * @param count 数量
     * @return 英语单词列表
     */
    @GetMapping("/getRandomEnglishVocabularies")
    @Operation(description = "获取随机英语词汇", summary = "获取随机英语词汇")
    public Result getRandomEnglishVocabularies(Integer count) {
        List<EnglishVocabulary> vocabularies = englishVocabularyService.getRandomEnglishVocabularies(count);
        return Result.success(vocabularies);
    }

    /**
     * 添加英语单词
     *
     * @param englishVocabulary 英语单词
     * @return 添加结果
     */
    @PostMapping("/addVocabulary")
    @Operation(description = "添加英语单词", summary = "添加英语单词")
    public Result addVocabulary(@RequestBody EnglishVocabulary englishVocabulary) {
        boolean f = englishVocabularyService.addVocabulary(englishVocabulary);
        return Result.success(f);
    }

    /**
     * 通过excel表格添加英语单词
     *
     * @param file      excel文件
     * @param levelType 单词类型
     * @return 添加结果
     */
    @PostMapping("/addVocabulariesByExcel")
    @Operation(description = "获取单词", summary = "获取单词")
    public Result addVocabulariesByExcel(MultipartFile file, String levelType) {
        boolean f = englishVocabularyService.addVocabulariesByExcel(file, levelType);
        return Result.success(f);
    }

    /**
     * 修改单词
     *
     * @param englishVocabulary 修改单词
     * @return 修改结果
     */
    @PostMapping("/updateEnglishVocabulary")
    @Operation(description = "修改单词", summary = "修改单词")
    public Result updateEnglishVocabulary(@RequestBody EnglishVocabulary englishVocabulary) {
        boolean f = englishVocabularyService.updateEnglishVocabulary(englishVocabulary);
        return Result.success(f);
    }

    /**
     * 删除单词
     *
     * @param id 删除单词
     * @return 删除结果
     */
    @GetMapping("/deleteEnglishVocabulary")
    @Operation(description = "修改单词", summary = "修改单词")
    public Result deleteEnglishVocabulary(Integer id) {
        boolean f = englishVocabularyService.deleteEnglishVocabulary(id);
        return Result.success(f);
    }
}
