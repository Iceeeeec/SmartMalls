package com.hsasys.controller.tools;

import lombok.Data;


@Data
public class Result {
    private Integer code;
    private Object data1;
    private Object data2;
    private String msg;

    public Result(Integer code, Object data1, String msg) {
        this.data1 = data1;
        this.code = code;
        this.msg = msg;
    }

    public Result(Integer code, Object data1, Object data2, String msg) {
        this.code = code;
        this.data1 = data1;
        this.data2 = data2;
        this.msg = msg;
    }

    public Result(Integer code, Object data1) {
        this.data1 = data1;
        this.code = code;
    }

    public Result(Integer code, Object data1, Object data2) {
        this.code = code;
        this.data1 = data1;
        this.data2 = data2;
    }
}
