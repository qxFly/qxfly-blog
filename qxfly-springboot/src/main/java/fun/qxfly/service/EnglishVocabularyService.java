package fun.qxfly.service;

import com.github.pagehelper.PageInfo;
import fun.qxfly.common.domain.DTO.EnglishVocabularyDTO;
import fun.qxfly.common.domain.entity.EnglishVocabulary;
import org.springframework.web.multipart.MultipartFile;

public interface EnglishVocabularyService {
    /**
     * 查询英语单词
     *
     * @param englishVocabularyDTO 英语单词查询条件
     * @return 英语单词列表
     */
    PageInfo<EnglishVocabulary> getVocabularies(EnglishVocabularyDTO englishVocabularyDTO);

    /**
     * 通过excel表格添加英语单词
     *
     * @param file excel文件
     * @param type 单词类型
     * @return 添加结果
     */
    boolean addVocabulariesByExcel(MultipartFile file, String type);

    /**
     * 修改单词
     *
     * @param englishVocabulary 修改单词
     * @return 修改结果
     */
    boolean updateEnglishVocabulary(EnglishVocabulary englishVocabulary);

    /**
     * 添加英语单词
     *
     * @param englishVocabulary 英语单词
     * @return 添加结果
     */
    boolean addVocabulary(EnglishVocabulary englishVocabulary);

    /**
     * 删除单词
     *
     * @param id 删除单词
     * @return 删除结果
     */
    boolean deleteEnglishVocabulary(Integer id);
}
