package com.hsasys.exception;

public class FoodNotFoundException extends BaseException{
    public FoodNotFoundException()
    {

    }

    public FoodNotFoundException(String msg)
    {
        super(msg);
    }
}
