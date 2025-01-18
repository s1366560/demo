package com.tiejun.demo.exception;

import com.tiejun.demo.contsants.ErrorCode;

public class BizException extends RuntimeException {
    private final ErrorCode errorCode;

    public BizException(ErrorCode errorCode) {
        super(String.format("%s:%s", errorCode.getCode(), errorCode.getMsg()));
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
