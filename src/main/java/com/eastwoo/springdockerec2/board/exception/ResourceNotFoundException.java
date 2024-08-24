package com.eastwoo.springdockerec2.board.exception;

/**
 * Please explain the class!!
 *
 * @author : dongwoo
 * @fileName : ResourceNotFoundException
 * @since : 2024-08-24
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
