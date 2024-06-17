package com.dizzycode.dizzycode.domain.enumerate;

public enum RoleEnum {
    // member 권한
    ROLE_USER,
    ROLE_ADMIN;

    public static RoleEnum fromString(String role) {
        for (RoleEnum r : RoleEnum.values()) {
            if (r.name().equalsIgnoreCase(role)) {
                return r;
            }
        }
        throw new IllegalArgumentException("No role with name " + role + " found");
    }
}
