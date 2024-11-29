package com.hsasys.result;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable
{
    private Integer code;
    private String msg;
    private T data;

    public static <T> Result<T> success()
    {
        Result<T> result = new Result<>();
        result.code = 200;
        return result;
    }

    public static <T> Result<T> success(T object)
    {
        Result<T> result = new Result<>();
        result.data = object;
        result.code = 200;
        return result;
    }

    public static <T> Result<T> success(String msg)
    {
        Result result = new Result();
        result.msg = msg;
        result.code = 200;
        return result;
    }
    public static <T> Result<T> error(String msg) {
        Result result = new Result();
        result.msg = msg;
        result.code = 500;
        return result;
    }
    public static <T> Result<T> error(Integer code, String msg) {
        Result result = new Result();
        result.msg = msg;
        result.code = code;
        return result;
    }
}
