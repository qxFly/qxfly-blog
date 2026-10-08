package fun.qxfly.service.Article.Impl;

import fun.qxfly.common.domain.entity.Attachment;
import fun.qxfly.common.enums.ExceptionEnum;
import fun.qxfly.common.enums.FilePaths;
import fun.qxfly.common.exception.excep.FileException;
import fun.qxfly.common.utils.FileUtils;
import fun.qxfly.mapper.Article.ArticleMapper;
import fun.qxfly.service.Article.ArticleAttachmentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class ArticleAttachmentServiceImpl implements ArticleAttachmentService {
    @Value("${qxfly.file.path.articleAttachment}")
    private String articleAttachmentDownloadPath;
    private final ArticleMapper articleMapper;

    public ArticleAttachmentServiceImpl(ArticleMapper articleMapper) {
        this.articleMapper = articleMapper;
    }

    /**
     * 上传文章附件
     *
     * @param file 文件
     * @return 文件名
     */
    @Override
    public String uploadAttachment(MultipartFile file) {
        String path = FilePaths.ARTICLE_ATTACHMENT_PATH.getPath();
        String fileName;
        try {
            fileName = FileUtils.upload(path, file);
        } catch (IOException e) {
            throw new FileException(ExceptionEnum.FILE_UPLOAD_ERROR);
        }
        return fileName;
    }

    /**
     * 删除文章附件
     *
     * @param aid      文章id
     * @param fileName 文件名
     * @return boolean
     */
    @Override
    public boolean deleteAttachment(Integer aid, String fileName) {
        File file = new File(FilePaths.ARTICLE_ATTACHMENT_PATH.getPath() + fileName);
        if (aid != null && aid != 0) {
            articleMapper.deleteAttachment(aid, fileName);
        }
        return file.exists() && file.delete();
    }

    /**
     * 保存文章附件
     *
     * @param aid            文章id
     * @param uid            用户id
     * @param attachmentList 附件列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAttachment(Integer aid, Integer uid, List<Attachment> attachmentList) {
        for (Attachment attachment : attachmentList) {
            articleMapper.saveAttachment(aid, uid, attachment);
        }
    }

    /**
     * 获取文章附件
     *
     * @param aid 文章id
     * @return 附件列表
     */
    @Override
    public List<Attachment> getArticleAttachment(Integer aid) {
        List<Attachment> articleAttachment = articleMapper.getArticleAttachmentByAid(aid);
        for (Attachment attachment : articleAttachment) {
            attachment.setDownloadUrl(articleAttachmentDownloadPath + attachment.getFileName());
        }
        return articleAttachment;
    }
}
