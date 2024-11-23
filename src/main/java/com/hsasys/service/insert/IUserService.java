package com.hsasys.service.insert;

import com.hsasys.controller.tools.Result;
import com.hsasys.domain.entity.User;

public interface IUserService {
    public Result userInsert(User user);

//    public Result userTypeInsert(UserType userType);

    public Result relaUserUTypeInsert(Integer userId, Integer userTypeId);
}
