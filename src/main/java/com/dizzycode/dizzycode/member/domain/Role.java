package com.dizzycode.dizzycode.member.domain;

public enum Role {
    // member 권한
    ROLE_USER,
    ROLE_ADMIN;

    public static Role fromString(String role) {
        for (Role r : Role.values()) {
            if (r.name().equalsIgnoreCase(role)) {
                return r;
            }
        }
        throw new IllegalArgumentException("No role with name " + role + " found");
    }
}
