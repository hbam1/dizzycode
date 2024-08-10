package com.dizzycode.dizzycode.category.controller;

import com.dizzycode.dizzycode.category.exception.NoCategoryException;
import com.dizzycode.dizzycode.channel.controller.ChannelController;
import com.dizzycode.dizzycode.common.exception.ErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {ChannelController.class})
public class CategoryExceptionController {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoCategoryException.class)
    public ErrorResult canNotFindCategoryExHandle(NoCategoryException e) {

        return new ErrorResult("404", e.getMessage());
    }
}
