package com.hsasys.exception;

public class UserIsExistException extends BaseException{
    public UserIsExistException()
    {

    }

    public UserIsExistException(String msg)
    {
        super(msg);
    }
}
