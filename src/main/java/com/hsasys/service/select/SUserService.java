package com.hsasys.service.select;

import com.hsasys.controller.tools.Result;
import com.hsasys.domain.User;

public interface SUserService {
    Result login(User user);

    Result register(User user);

    Result getUsertype(User user);

    Result getPersonalInfo(Integer userId);

    Result getCollectionInfo(Integer userId);

    Double getBMI(Integer userId);
}
