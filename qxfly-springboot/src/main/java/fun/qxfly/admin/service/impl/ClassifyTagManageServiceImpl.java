package fun.qxfly.admin.service.impl;

import fun.qxfly.admin.mapper.ClassifyTagManageMapper;
import fun.qxfly.admin.service.ClassifyTagManageService;
import fun.qxfly.common.domain.entity.Classify;
import fun.qxfly.common.domain.entity.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ClassifyTagManageServiceImpl implements ClassifyTagManageService {
    private final ClassifyTagManageMapper classifyTagManageMapper;

    public ClassifyTagManageServiceImpl(ClassifyTagManageMapper classifyTagManageMapper) {
        this.classifyTagManageMapper = classifyTagManageMapper;
    }

    /**
     * 删除分类
     *
     * @param classify
     * @return
     */
    @Override
    public boolean deleteClassify(Classify classify) {
        return classifyTagManageMapper.deleteClassify(classify);
    }

    /**
     * 添加分类
     *
     * @param classify
     * @return
     */
    @Override
    public boolean addClassify(Classify classify) {
        Classify classify1 = classifyTagManageMapper.findClassifyByName(classify.getName());
        if (classify1 != null)
            return false;
        return classifyTagManageMapper.addClassify(classify);
    }

    /**
     * 删除标签
     *
     * @param tag
     * @return
     */
    @Override
    public boolean deleteTag(Tag tag) {
        return classifyTagManageMapper.deleteTag(tag);
    }

    /**
     * 添加标签
     *
     * @param tag
     * @return
     */
    @Override
    public boolean addTag(Tag tag) {
        Tag tag1 = classifyTagManageMapper.findTagByName(tag.getName());
        if (tag1 != null)
            return false;
        return classifyTagManageMapper.addTag(tag);
    }

    /**
     * 更新分类
     *
     * @param classify
     * @return
     */
    @Override
    public boolean updateClassify(Classify classify) {
        return classifyTagManageMapper.updateClassify(classify);
    }

    /**
     * 更新标签
     *
     * @param tag
     * @return
     */
    @Override
    public boolean updateTag(Tag tag) {
        return classifyTagManageMapper.updateTag(tag);
    }
}
