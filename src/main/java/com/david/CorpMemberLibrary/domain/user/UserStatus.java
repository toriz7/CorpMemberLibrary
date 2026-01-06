package com.david.CorpMemberLibrary.domain.user;

/**
 * 사용자 상태를 나타내는 Enum
 * 회원가입 승인 프로세스에 사용됩니다.
 */
public enum UserStatus {
    PENDING("승인대기"),
    APPROVED("승인완료"),
    REJECTED("승인거부");

    private final String description;

    UserStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
