package com.hsasys.service.update;

import com.hsasys.controller.tools.Result;
import com.hsasys.domain.User;

public interface UUserService {
    Result addCollection(Integer userId, Integer foodId);

    Result updateUserInfo(User user);
}
