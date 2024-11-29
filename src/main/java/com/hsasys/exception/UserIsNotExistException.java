package com.hsasys.exception;

public class UserIsNotExistException extends BaseException{
    public UserIsNotExistException()
    {

    }

    public UserIsNotExistException(String msg)
    {
        super(msg);
    }
}
