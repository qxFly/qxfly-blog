package fun.qxfly.common.enums;

import fun.qxfly.common.utils.FileUtils;

public enum FilePaths {
    APP_BASE_PATH(System.getProperty("user.dir")),
    USER_CONFIG_PATH(APP_BASE_PATH.getPath() + "\\data\\qxfly-conf\\"),
    USER_AVATAR_PATH(APP_BASE_PATH.getPath() + "\\data\\qxfly-userAvatar\\"),
    USER_BACKGROUND_IMAGE_PATH(APP_BASE_PATH.getPath() + "\\data\\qxfly-userBgImg\\"),
    ARTICLE_COVER_PATH(APP_BASE_PATH.getPath() + "\\data\\qxfly-articleCover\\"),
    ARTICLE_IMAGE_PATH(APP_BASE_PATH.getPath() + "\\data\\qxfly-articleImage\\"),
    ARTICLE_ATTACHMENT_PATH(APP_BASE_PATH.getPath() + "\\data\\qxfly-articleAttachment\\"),
    SYSTEM_SETTINGS_PATH(APP_BASE_PATH.getPath() +"\\data\\qxfly-systemSetting\\")
    ;

    private final String path;

    FilePaths(String path) {
        this.path = path;
    }

    public String getPath() {
        return FileUtils.toSystemSeparator(this.path);
    }
}
