package com.dizzycode.dizzycode.controller.exception;

import com.dizzycode.dizzycode.controller.RoomController;
import com.dizzycode.dizzycode.dto.ErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = RoomController.class)
public class RoomExceptionController {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ClassNotFoundException.class)
    public ErrorResult canNotFindRoomExHandle(ClassNotFoundException e) {

        return new ErrorResult("404", e.getMessage());
    }
}
