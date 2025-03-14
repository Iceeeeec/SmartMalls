package com.hsasys.controller;

import com.hsasys.context.BaseContext;
import com.hsasys.domain.dto.UserDto;
import com.hsasys.domain.dto.UserRegisterDto;
import com.hsasys.domain.entity.FamilyMember;
import com.hsasys.domain.entity.FamilyRole;
import com.hsasys.domain.vo.FamilyMemberVo;
import com.hsasys.result.Result;
import com.hsasys.service.update.FamilyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/family")
public class FamilyController
{
    @Autowired
    private FamilyService familyService;

    /**
     * 创建家庭
     */
    @GetMapping("/create")
    Result createFamily()
    {
        return familyService.createFamily();
    }

    /**
     * 查看自己在家庭中的角色
     */
    @GetMapping("/role")
    Result<FamilyRole> getFamilyRole()
    {
        Long userId = BaseContext.getCurrentId();
        FamilyRole role = familyService.getFamilyRoleByUserId(userId);
        return Result.success(role);
    }
    /**
     *  查看已经关联的用户
     */
    @GetMapping("/member")
    Result<List<FamilyMemberVo>> getFamilyMember()
    {
        return familyService.getFamilyMember();
    }

    /**
     * 登录家人账号
     */
    @PostMapping("/invite")
    Result loginFamilyAccount(@RequestBody UserDto userDto)
    {
        return familyService.loginFamilyAccount(userDto);
    }
    /**
     * 停止家人共享
     */
    @GetMapping("/stop")
    Result shopFamilyShare()
    {
        return familyService.stopFamilyShare();
    }
    /**
     * 退出家庭或者移除家庭成员
     */
    @GetMapping("/delete/{id}")
    Result deleteFamilyMember(@PathVariable Long id)
    {
        return familyService.deleteFamilyMember(id);
    }
    /**
     * 创建儿童账号
     */
    @PostMapping("/createChild")
    Result createChildAccount(@RequestBody UserRegisterDto userRegisterDto)
    {
        return familyService.createChildAccount(userRegisterDto);
    }
}
