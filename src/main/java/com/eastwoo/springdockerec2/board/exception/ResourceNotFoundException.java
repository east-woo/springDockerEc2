package com.eastwoo.springdockerec2.board.exception;

/**
 * 자원을 찾을 수 없을 때 발생하는 사용자 정의 예외 클래스
 *
 * 이 클래스는 RuntimeException을 상속하여, 특정 자원(예: 데이터베이스 레코드 등)을
 * 찾을 수 없을 때 발생하는 예외를 표현. 이 예외는 일반적으로 자원 검색 실패 시
 * 사용자에게 적절한 오류 메시지를 전달하는 데 사용됨
 *
 * @author : dongwoo
 * @fileName : ResourceNotFoundException
 * @since : 2024-08-24
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * 예외를 생성하는 생성자
     *
     * @param message 예외 메시지를 포함하는 문자열입니다. 이 메시지는 예외 발생 시
     *                사용자에게 제공됩니다.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
