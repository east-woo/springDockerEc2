package com.eastwoo.springdockerec2.board.exception;

/**
 * 사용자 중복에 대한 예외 처리
 *
 * @author : dongwoo
 * @fileName : ResourceNotFoundException
 * @since : 2024-08-25
 */
public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException(String message) {
        super(message);
    }
}