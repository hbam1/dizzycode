package com.dizzycode.dizzycode.exception.member;

public class ExistMemberException extends RuntimeException {
    public ExistMemberException() {
        super();
    }

    public ExistMemberException(String message) {
        super(message);
    }

    public ExistMemberException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExistMemberException(Throwable cause) {
        super(cause);
    }

    protected ExistMemberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
