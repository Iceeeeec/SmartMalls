package com.hsasys.service.select;

import com.hsasys.domain.dto.UserLoginDto;
import com.hsasys.domain.dto.UserUpdateDto;
import com.hsasys.domain.entity.User;

import com.hsasys.domain.dto.UserRegisterDto;
import com.hsasys.domain.vo.ReportVo;
import com.hsasys.domain.vo.UserLoginVo;
import com.hsasys.result.Result;

import java.util.List;

public interface SUserService {
    /**
     * 登录
     * @param userLoginDto
     * @return
     */
    Result<UserLoginVo> login(UserLoginDto userLoginDto);

    /**
     * 注册
     * @param userRegisterDto
     * @return
     */
    Result register(UserRegisterDto userRegisterDto);

    /**
     * 查询用户提交的报告
     * @return
     */
    Result<List<ReportVo>> selectReports();

    Result<UserLoginVo> selectUserHealthInfo(Integer userId);
}
