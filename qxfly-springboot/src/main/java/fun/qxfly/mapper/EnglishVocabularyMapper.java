package fun.qxfly.mapper;

import fun.qxfly.common.domain.DTO.EnglishVocabularyDTO;
import fun.qxfly.common.domain.entity.EnglishVocabulary;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface EnglishVocabularyMapper {
    /**
     * 查询英语单词
     *
     * @param englishVocabularyDTO 英语单词查询条件
     * @return 英语单词列表
     */
    List<EnglishVocabulary> getVocabularies(EnglishVocabularyDTO englishVocabularyDTO);

    /**
     * 通过excel表格添加英语单词
     *
     * @param list 英语单词列表
     * @param type 单词等级类型
     * @return 结果
     */
    Integer addVocabularies(@Param("list") List<EnglishVocabulary> list, @Param("levelType") String levelType);

    /**
     * 修改英语单词
     *
     * @param englishVocabulary 修改单词
     * @return 修改结果
     */
    @Update("update english_vocabulary set word = #{word}, phoneticSymbol = #{phoneticSymbol}, interpretation = #{interpretation},levelType = #{levelType} where id = #{id}")
    boolean updateEnglishVocabulary(EnglishVocabulary englishVocabulary);

    /**
     * 添加英语单词
     *
     * @param englishVocabulary 英语单词
     * @return 添加结果
     */
    @Insert("insert into english_vocabulary(word, phoneticSymbol, interpretation, levelType) values(#{word}, #{phoneticSymbol}, #{interpretation}, #{levelType})")
    boolean addVocabulary(EnglishVocabulary englishVocabulary);

    /**
     * 删除单词
     *
     * @param id 删除单词
     * @return 删除结果
     */
    @Delete("delete from english_vocabulary where id = #{id}")
    boolean deleteEnglishVocabulary(Integer id);

    /**
     * 获取所有单词
     *
     * @return 所有单词
     */
    @Select("select * from english_vocabulary")
    List<EnglishVocabulary> getAllVocabularies();

    /**
     * 获取指定数量的随机单词
     *
     * @param count 数量
     * @return 随机单词
     */
    List<EnglishVocabulary> getRandomEnglishVocabularies(Integer count);
}
