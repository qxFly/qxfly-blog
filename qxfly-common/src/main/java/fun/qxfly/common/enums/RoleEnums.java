package fun.qxfly.common.enums;

public enum RoleEnums {
    ADMIN(1, "管理员"),
    ORDINARY(0, "普通用户"),
    ARTICLE_REVIEWER(2, "文章审核员"),
    COMMENT_REVIEWER(3, "评论审核员"),
    USER_REVIEWER(4, "用户审核员"),
    ;
    private final String name;
    private final Integer id;

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    RoleEnums(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
