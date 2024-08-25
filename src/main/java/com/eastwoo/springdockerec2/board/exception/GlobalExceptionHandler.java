package com.eastwoo.springdockerec2.board.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * 전체 애플리케이션에서 발생하는 예외를 처리하기 위한 전역 Exception Handler
 *
 * 이 클래스는 @ControllerAdvice 애너테이션을 사용하여 애플리케이션의 모든 컨트롤러에서
 * 발생하는 예외를 처리. 특정 예외에 대한 사용자 정의 응답을 제공.
 *
 * @author : dongwoo
 * @fileName : GlobalExceptionHandler
 * @since : 2024-08-24
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 메서드 인수의 유효성 검사 실패 시 발생하는 예외를 처리
     *
     * 이 메서드는 MethodArgumentNotValidException이 발생할 때 호출되며,
     * 이는 요청 본문이 유효성 검사 제약 조건을 충족하지 않을 때 발생
     *
     * @param ex 유효성 검사 오류를 포함하는 예외 객체
     * @return 필드 오류와 해당 메시지를 포함하는 맵과 HTTP 상태 BAD_REQUEST(400)를 가진 ResponseEntity를 반환
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // 모든 유효성 검사 오류를 순회
        ex.getBindingResult().getAllErrors().forEach(error -> {
            // 각 오류에서 필드 이름과 오류 메시지를 추출
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();

            // 필드 이름과 오류 메시지를 맵에 추가
            errors.put(fieldName, errorMessage);
        });

        // 필드 오류와 메시지를 담은 맵과 HTTP 상태 BAD_REQUEST를 반환
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * 중복된 사용자 ID로 회원가입을 시도할 때 발생하는 예외를 처리
     *
     * 이 메서드는 DuplicateUserException이 발생할 때 호출되며, 이는 사용자 ID가 이미 사용 중일 때 발생
     *
     * @param ex 중복 사용자에 대한 메시지를 포함하는 예외 객체
     * @return 예외 메시지와 HTTP 상태 CONFLICT(409)를 가진 ResponseEntity를 반환
     */
    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<String> handleDuplicateUserException(DuplicateUserException ex) {
        // 예외 메시지와 HTTP 상태 CONFLICT를 반환
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }
}