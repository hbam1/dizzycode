package com.dizzycode.dizzycode.common.exception;

import com.dizzycode.dizzycode.member.exception.NoMemberException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        String message = "같은 키를 가지는 엔티티를 중복 생성할 수 없습니다.";
        log.info("conflict={}", message);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
    }
}
