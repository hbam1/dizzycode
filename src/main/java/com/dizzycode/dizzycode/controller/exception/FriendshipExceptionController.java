package com.dizzycode.dizzycode.controller.exception;

import com.dizzycode.dizzycode.controller.FriendshipController;
import com.dizzycode.dizzycode.dto.ErrorResult;
import com.dizzycode.dizzycode.exception.friendship.FriendshipAlreadyExistsException;
import com.dizzycode.dizzycode.exception.friendship.InvalidFriendshipRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = FriendshipController.class)
public class FriendshipExceptionController {

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidFriendshipRequestException.class)
    public ResponseEntity<ErrorResult> handleInvalidRequest(InvalidFriendshipRequestException e) {
        ErrorResult errorResult = new ErrorResult("422", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResult);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(FriendshipAlreadyExistsException.class)
    public ResponseEntity<ErrorResult> handleDuplication(FriendshipAlreadyExistsException e) {
        ErrorResult errorResult = new ErrorResult("409", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResult);
    }
}