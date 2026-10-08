package fun.qxfly.service.Article;

import fun.qxfly.common.domain.entity.Attachment;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ArticleAttachmentService {
    /**
     * 上传文章附件
     *
     * @param file 文件
     */
    String uploadAttachment(MultipartFile file);

    /**
     * 删除文章附件
     *
     * @param aid      文章id
     * @param fileName 文件名
     */
    boolean deleteAttachment(Integer aid, String fileName);

    /**
     * 保存文章附件
     *
     * @param aid            文章id
     * @param uid            用户id
     * @param attachmentList 附件列表
     */
    void saveAttachment(Integer aid, Integer uid, List<Attachment> attachmentList);

    /**
     * 获取文章附件
     *
     * @param aid 文章id
     * @return 附件列表
     */
    List<Attachment> getArticleAttachment(Integer aid);
}
