package com.hsasys.service.insert;


import com.hsasys.domain.UserType;
import com.hsasys.domain.entity.User;
import com.hsasys.result.Result;

public interface IUserService {
    public Result userInsert(User user);

//    public Result userTypeInsert(UserType userType);

    public Result relaUserUTypeInsert(Integer userId, Integer userTypeId);
}
