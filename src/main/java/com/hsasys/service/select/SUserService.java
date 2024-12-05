package com.hsasys.service.select;

import com.hsasys.domain.dto.UserLoginDto;
import com.hsasys.domain.entity.User;

import com.hsasys.domain.dto.UserRegisterDto;
import com.hsasys.domain.vo.UserLoginVo;
import com.hsasys.result.Result;

public interface SUserService {
    Result<UserLoginVo> login(UserLoginDto userLoginDto);

    Result register(UserRegisterDto userRegisterDto);


    Double getBMI(Integer userId);
}
