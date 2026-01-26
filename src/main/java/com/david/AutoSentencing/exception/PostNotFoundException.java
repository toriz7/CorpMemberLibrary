package com.david.AutoSentencing.exception;

public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException(Long id) {
        super("해당 게시글을 찾을 수 없습니다. id=" + id);
    }

    public PostNotFoundException(String message) {
        super(message);
    }
}
