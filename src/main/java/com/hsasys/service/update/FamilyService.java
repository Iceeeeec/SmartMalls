package com.hsasys.service.update;

import com.hsasys.domain.dto.UserDto;
import com.hsasys.domain.dto.UserRegisterDto;
import com.hsasys.domain.entity.FamilyRole;
import com.hsasys.domain.vo.FamilyMemberVo;
import com.hsasys.result.Result;

import java.util.List;

public interface FamilyService
{

    /**
     * 创建家庭
     * @return
     */
    Result createFamily();

    /**
     * 获取家庭成员
     * @return
     */
    Result<List<FamilyMemberVo>> getFamilyMember();

    /**
     * 查看自己在家庭的角色
     * @param userId
     * @return
     */
    FamilyRole getFamilyRoleByUserId(Long userId);

    /**
     * 邀请家人账号
     * @param userDto
     * @return
     */
    Result loginFamilyAccount(UserDto userDto);

    /**
     * 停止家庭共享
     * @return
     */
    Result stopFamilyShare();

    /**
     * 删除家庭成员
     * @param id
     * @return
     */
    Result deleteFamilyMember(Long id);

    /**
     * 创建孩子账号
     * @param userRegisterDto
     * @return
     */
    Result createChildAccount(UserRegisterDto userRegisterDto);
}
