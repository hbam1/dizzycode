package com.dizzycode.dizzycode.controller.exception;

import com.dizzycode.dizzycode.controller.MemberController;
import com.dizzycode.dizzycode.dto.ErrorResult;
import com.dizzycode.dizzycode.exception.member.ExistMemberException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = MemberController.class)
public class MemberExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ExistMemberException.class)
    public ErrorResult illegalExHandle(ExistMemberException e) {

        return new ErrorResult("400", e.getMessage());
    }
}
