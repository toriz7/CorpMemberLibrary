package com.david.CorpMemberLibrary.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String userId) {
        super("해당 사용자를 찾을 수 없습니다. userId=" + userId);
    }

    public UserNotFoundException(Long id) {
        super("해당 사용자를 찾을 수 없습니다. id=" + id);
    }
}
