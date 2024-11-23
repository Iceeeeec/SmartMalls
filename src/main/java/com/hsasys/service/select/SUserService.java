package com.hsasys.service.select;

import com.hsasys.domain.dto.UserLoginDto;
import com.hsasys.domain.entity.User;

import com.hsasys.domain.dto.UserRegisterDto;
import com.hsasys.domain.vo.UserLoginVo;
import com.hsasys.result.Result;

public interface SUserService {
    Result<UserLoginVo> login(UserLoginDto userLoginDto);

    void register(UserRegisterDto userRegisterDto);

    Result getUsertype(User user);

    Result getPersonalInfo(Integer userId);

    Result getCollectionInfo(Integer userId);

    Double getBMI(Integer userId);
}
