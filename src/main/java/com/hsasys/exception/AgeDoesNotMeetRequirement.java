package com.hsasys.exception;

public class AgeDoesNotMeetRequirement extends BaseException{
    public AgeDoesNotMeetRequirement()
    {

    }

    public AgeDoesNotMeetRequirement(String msg)
    {
        super(msg);
    }
}
