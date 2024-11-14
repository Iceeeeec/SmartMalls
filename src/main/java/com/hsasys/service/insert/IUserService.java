package com.hsasys.service.insert;

import com.hsasys.controller.tools.Result;
import com.hsasys.domain.User;
import com.hsasys.domain.UserType;

public interface IUserService {
    public Result userInsert(User user);

//    public Result userTypeInsert(UserType userType);

    public Result relaUserUTypeInsert(Integer userId, Integer userTypeId);
}
