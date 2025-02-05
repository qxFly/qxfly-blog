package fun.qxfly.common.utils;

import com.alibaba.fastjson2.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

public class FileUtils {

    private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 根据操作系统调整文件路径的分隔符
     *
     * @param path 文件路径
     * @return 调整后的文件路径
     */
    public static String toSystemSeparator(String path) {
        String separator = File.separator;
        path = path.replace("\\", separator).replace("/", separator);
        return path;
    }

    /**
     * 根据操作系统调整文件路径的分隔符
     *
     * @param path 文件路径
     * @return 调整后的文件路径
     */
    public static String toUrlSeparator(String path) {
        path = path.replace("\\", "/");
        return path;
    }

    

    /**
     * 上传文件
     * @param path 文件路径
     * @param file 文件
     * @return 文件名
     * @throws IOException IO异常
     */
    public static String upload(String path, MultipartFile file) throws IOException {
        if (file == null) return null;
        String fileName = genFileName(file);
        File filePath = new File(path + fileName);
        log.info("文件:{}", filePath);
        if (!filePath.exists()) {
            if (!filePath.getParentFile().exists())
                filePath.getParentFile().mkdirs();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        fileOutputStream.write(file.getBytes());
        fileOutputStream.close();
        return fileName;
    }

    /**
     * 生成文件名
     * @param file file
     * @return 文件名
     */
    public static String genFileName(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        log.info("originalFilename：{}", originalFilename);
        String extension = "";
        if (originalFilename != null) {
            String[] split = originalFilename.split("\\.");
            if (split.length > 2) {
                String dot9 = split[split.length - 2];
                if (dot9.equals("9")) extension = "9.";
            }
            extension += split[split.length - 1];
        }

        String date = DateUtils.format(new Date(), "yyyy/MM/dd");
        String fileName = date + "/" + UUID.randomUUID() + "." + extension;
        return toSystemSeparator(fileName);
    }

}
