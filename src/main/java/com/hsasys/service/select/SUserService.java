package com.hsasys.service.select;

import com.hsasys.domain.dto.UserLoginDto;
import com.hsasys.domain.entity.User;

import com.hsasys.domain.dto.UserRegisterDto;
import com.hsasys.domain.vo.ReportVo;
import com.hsasys.domain.vo.UserLoginVo;
import com.hsasys.result.Result;

import java.util.List;

public interface SUserService {
    Result<UserLoginVo> login(UserLoginDto userLoginDto);

    Result register(UserRegisterDto userRegisterDto);

    /**
     * 查询用户提交的报告
     * @return
     */
    Result<List<ReportVo>> selectReports();
}
