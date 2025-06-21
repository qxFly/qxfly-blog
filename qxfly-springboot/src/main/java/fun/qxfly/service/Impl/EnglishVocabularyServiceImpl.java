package fun.qxfly.service.Impl;

import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import fun.qxfly.common.domain.DTO.EnglishVocabularyDTO;
import fun.qxfly.common.domain.entity.EnglishVocabulary;
import fun.qxfly.common.enums.ExceptionEnum;
import fun.qxfly.common.exception.excep.FileException;
import fun.qxfly.mapper.EnglishVocabularyMapper;
import fun.qxfly.service.EnglishVocabularyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class EnglishVocabularyServiceImpl implements EnglishVocabularyService {
    private final EnglishVocabularyMapper englishVocabularyMapper;

    public EnglishVocabularyServiceImpl(EnglishVocabularyMapper englishVocabularyMapper) {
        this.englishVocabularyMapper = englishVocabularyMapper;
    }

    /**
     * 查询英语单词
     *
     * @param englishVocabularyDTO 英语单词查询条件
     * @return 英语单词列表
     */
    @Override
    public PageInfo<EnglishVocabulary> getVocabularies(EnglishVocabularyDTO englishVocabularyDTO) {
        PageHelper.startPage(englishVocabularyDTO.getCurrPage(), englishVocabularyDTO.getPageSize());
        List<EnglishVocabulary> vocabularies = englishVocabularyMapper.getVocabularies(englishVocabularyDTO);
        return new PageInfo<>(vocabularies);
    }

    /**
     * 通过excel表格添加英语单词
     *
     * @param file excel文件
     * @param type 单词类型
     * @return
     */
    @Override
    public boolean addVocabulariesByExcel(MultipartFile file, String type) {
        List<EnglishVocabulary> newVocabularies = new ArrayList<>();
        try {
            newVocabularies = EasyExcel.read(file.getInputStream()).head(EnglishVocabulary.class).sheet().doReadSync();
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileException(ExceptionEnum.FILE_READ_ERROR);
        }
        List<EnglishVocabulary> oldVocabularies = englishVocabularyMapper.getAllVocabularies(); // 获取所有英语单词
        if (oldVocabularies.isEmpty()) {
            englishVocabularyMapper.addVocabularies(newVocabularies, type);
            return true;
        }


        // 遍历已有的所有英语单词列表
        for (EnglishVocabulary v : oldVocabularies) {
            // 遍历新添加的英语单词列表
            for (EnglishVocabulary n : newVocabularies) {
                if (n.getWord().equals(v.getWord())) {
                    // 如果新添加的英语单词和已有的英语单词相同，则合并
                    n.setLevelType(type);
                    mergeWithAddVocabulary(v, n);
                    newVocabularies.remove(n);
                    break;
                }
            }
        }
        // 将剩余的英语单词添加到数据库中
        englishVocabularyMapper.addVocabularies(newVocabularies, type);
        return true;
    }

    private void mergeWithAddVocabulary(EnglishVocabulary vocabulary1, EnglishVocabulary vocabulary2) {
        // 获取英语单词等级类型
        String levelType1 = vocabulary1.getLevelType();
        String levelType2 = vocabulary2.getLevelType();
        // 如果等级类型不同
        if (levelType1 != null && !levelType1.equals(levelType2)) {
            // 如果英语单词1的等级类型不为空
            if (!levelType1.isEmpty()) {
                // 如果英语单词2的等级类型不为空
                if (levelType2 != null && !levelType2.isEmpty()) {
                    // 将英语单词2等级类型添加到英语单词1的等级类型中
                    levelType1 += "," + levelType2;
                }
            } else {
                // 否则，将英语单词2的等级类型赋值给英语单词1的等级类型
                levelType1 = levelType2;
            }
            // 将英语单词1的等级类型更新为新的等级类型
            vocabulary1.setLevelType(levelType1);
            // 更新英语单词
            englishVocabularyMapper.updateEnglishVocabulary(vocabulary1);
        }
    }

    /**
     * 修改单词
     *
     * @param englishVocabulary 修改单词
     * @return 修改结果
     */
    @Override
    public boolean updateEnglishVocabulary(EnglishVocabulary englishVocabulary) {
        // 调用英语单词Mapper的更新英语单词方法，并返回更新结果
        return englishVocabularyMapper.updateEnglishVocabulary(englishVocabulary);
    }

    /**
     * 添加英语单词
     *
     * @param englishVocabulary 英语单词
     * @return 添加结果
     */
    @Override
    public boolean addVocabulary(EnglishVocabulary englishVocabulary) {
        return englishVocabularyMapper.addVocabulary(englishVocabulary);
        // 调用英语单词Mapper的添加英语单词方法，并返回添加结果
    }

    /**
     * 删除单词
     *
     * @param id 删除单词
     * @return 删除结果
     */
    @Override
    public boolean deleteEnglishVocabulary(Integer id) {
        return englishVocabularyMapper.deleteEnglishVocabulary(id);
        // 调用英语单词Mapper的删除英语单词方法，并返回删除结果
    }

    /**
     * 随机获取英语单词
     *
     * @param count 获取数量
     * @return 英语单词列表
     */
    @Override
    public List<EnglishVocabulary> getRandomEnglishVocabularies(Integer count) {
        List<EnglishVocabulary> vocabularies = englishVocabularyMapper.getRandomEnglishVocabularies(count);
        return vocabularies;
    }
}
