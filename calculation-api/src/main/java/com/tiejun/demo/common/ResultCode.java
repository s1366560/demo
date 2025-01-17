package com.tiejun.demo.common;

public enum ResultCode {
    SUCCESS(200,"OK"),FAIL(500, "ERROR"), NOT_FOUND(404,"NOT FOUND" );

    private final int code;
    private final String msg;

   ResultCode(int code, String msg) {
       this.code = code;
       this.msg = msg;
   }

    public int getCode() {
       return code;
    }

    public String getMessage() {
       return msg;
    }
}
