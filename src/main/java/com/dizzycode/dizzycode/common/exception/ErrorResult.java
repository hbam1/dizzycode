package com.dizzycode.dizzycode.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ErrorResult {

    private final String statusCode;
    private final String message;
}
