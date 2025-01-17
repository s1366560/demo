package com.tiejun.demo.contsants;

public enum ErrorCode {
    ACCOUNT_NOT_EXIST(1, "Account Not Exist"),
    TRANSACTION_CREATE_ERROR(2, "Transaction create ERROR"),
    TRANSACTION_NOT_EXIST(3, "Transaction Not Exist");

    private final int code;
    private final String msg;

    ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
