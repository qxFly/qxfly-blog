package fun.qxfly.common.utils;

import fun.qxfly.common.enums.RoleEnums;

public class RoleUtils {
    public static String getRoleNameByRoleId(Integer id) {
        String role = null;
        for (RoleEnums r : RoleEnums.values()) {
            if (r.getId().equals(id)) {
                role = r.getName();
                break;
            }
        }
        return role;
    }

    public static Integer getRoleIdByRoleName(String name) {
        if (name == null) return 0;
        Integer role = null;
        for (RoleEnums r : RoleEnums.values()) {
            if (r.getName().equals(name)) {
                role = r.getId();
                break;
            }
        }
        return role;
    }
}
