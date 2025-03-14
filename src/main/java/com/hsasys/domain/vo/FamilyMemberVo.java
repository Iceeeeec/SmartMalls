package com.hsasys.domain.vo;

import com.hsasys.domain.entity.FamilyRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FamilyMemberVo
{
    // 家庭成员id
    private Integer userId;
    // 家庭成员用户名
    private String username;
    // 家庭成员姓名
    private String name;
    //角色
    private FamilyRole role;
}
