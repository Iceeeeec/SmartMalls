package com.hsasys.service.update;


import com.hsasys.domain.entity.User;
import com.hsasys.result.Result;

public interface UUserService {
    Result addCollection(Integer userId, Integer foodId);

    Result updateUserInfo(User user);
}
