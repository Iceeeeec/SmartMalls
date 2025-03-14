package com.hsasys.service.update.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hsasys.context.BaseContext;
import com.hsasys.domain.dto.UserDto;
import com.hsasys.domain.dto.UserRegisterDto;
import com.hsasys.domain.entity.Family;
import com.hsasys.domain.entity.FamilyMember;
import com.hsasys.domain.entity.FamilyRole;
import com.hsasys.domain.entity.User;
import com.hsasys.domain.vo.FamilyMemberVo;
import com.hsasys.exception.*;
import com.hsasys.mapper.FamilyMapper;
import com.hsasys.mapper.UserMapper;
import com.hsasys.result.Result;
import com.hsasys.service.update.FamilyService;
import com.hsasys.service.update.UUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FamilyServiceImpl implements FamilyService
{
    @Autowired
    private FamilyMapper familyMapper;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UUserService userUpdateService;
    /**
     * 创建家庭
     * @return
     */
    @Override
    @Transactional
    public Result createFamily()
    {
        Long userId = BaseContext.getCurrentId();
        //查询用户信息
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getId, userId);
        User user = userMapper.selectOne(wrapper);
        String name = user.getName();
        String familyName = name + "的小窝";
        //添加家庭,设置为组织者
        Family family = Family.builder()
                .ownId(userId.intValue())
                .familyName(familyName)
                .createAt(LocalDateTime.now())
                .familySize(0).build();

        familyMapper.createFamily(family);
        FamilyMember familyMember = FamilyMember.builder()
                .userId(userId.intValue())
                .familyId(family.getId())
                .joinedAt(LocalDateTime.now()).build();
        familyMapper.addFamilyMember(familyMember);
        //添加家庭角色
        familyMapper.addFamilyRole(userId, 1);
        return Result.success();
    }

    /**
     * 查看家庭成员
     * @return
     */
    @Override
    @Transactional
    public Result<List<FamilyMemberVo>> getFamilyMember()
    {
        Long userId = BaseContext.getCurrentId();
        //先查找家庭
        Family family = familyMapper.selectFamilyByUserId(userId);
        if(family == null)
        {
            //TODO 定义变量
            throw new UserNotAddFamilyException("用户未加入家庭");
        }
        //通过家庭id查找所有成员Id
        List<Integer> ids = familyMapper.selectFamilyMemberByFamilyId(family.getId());
        //排除自己
        ids.remove(Integer.valueOf(userId.intValue()));
        //通过成员id查找成员信息
        List<FamilyMemberVo> familyMembers = new ArrayList<>();
        if(!ids.isEmpty())
        {
            for(Integer id : ids)
            {
                LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(User::getId, id);
                User user = userMapper.selectOne(wrapper);
                FamilyRole role = familyMapper.selectFamilyRoleByUserId(id.longValue());
                FamilyMemberVo familyMember = FamilyMemberVo.builder()
                        .userId(id)
                        .username(user.getUsername())
                        .name(user.getName())
                        .role(role).build();
                    familyMembers.add(familyMember);
            }
        }
        return Result.success(familyMembers);
    }

    /**
     * 查看自己在家庭的角色
     * @param userId
     * @return
     */
    @Override
    public FamilyRole getFamilyRoleByUserId(Long userId)
    {
        return familyMapper.selectFamilyRoleByUserId(userId);
    }

    /**
     * 邀请家人成员
     * @param userDto
     * @return
     */
    @Override
    @Transactional
    public Result loginFamilyAccount(UserDto userDto)
    {
        System.out.println(userDto);
        //先获取本人的家庭
        Long userId = BaseContext.getCurrentId();
        Family family = familyMapper.selectFamilyByUserId(userId);
        if(family == null)
        {
            throw new UserNotAddFamilyException("用户未加入家庭");
        }
        List<Integer> ids = familyMapper.selectFamilyMemberByFamilyId(family.getId());
        if(family.getFamilySize() == 5)
        {
            throw new FamilyIsFullException("家庭已满");
        }
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, userDto.getUsername());
        User user = userMapper.selectOne(wrapper);
        if(user == null)
        {
            throw new UserNotAddFamilyException("用户不存在");
        }
        if(user.getId() == userId.intValue())
        {
            throw new UnableToShareWithOneselfException("不能与自己共享");
        }
        if(ids.contains(user.getId()))
        {
            throw new AlreadyExistsFamilyMemberException("该用户已是家庭成员");
        }
        if(!user.getPassword().equals(userDto.getPassword()))
        {
            throw new UserNotAddFamilyException("密码错误");
        }
        //家庭数量加一
        family.setFamilySize(family.getFamilySize() + 1);
        familyMapper.updateFamily(family);
        //加入家庭
        FamilyMember familyMember = FamilyMember.builder()
                .familyId(family.getId())
                .userId(user.getId())
                .joinedAt(LocalDateTime.now()).build();
        familyMapper.addFamilyMember(familyMember);
        //添加家庭角色
        //TODO 定义变量 2为成员
        familyMapper.addFamilyRole(user.getId().longValue(), 2);
        return Result.success();
    }

    /**
     * 停止家庭共享
     * @return
     */
    @Override
    @Transactional
    public Result stopFamilyShare()
    {
        Long userId = BaseContext.getCurrentId();
        //删除家庭和用户的关联的表
        Family family = familyMapper.selectFamilyByUserId(userId);
        if(family == null)
        {
            throw new UserNotAddFamilyException("用户未加入家庭");
        }
        List<Integer> userIds = familyMapper.selectFamilyMemberByFamilyId(family.getId());
        //删除家庭成员
        familyMapper.deleteFamilyMemberByFamilyId(family.getId());
        //删除家庭
        familyMapper.deleteFamilyByFamilyId(family.getId());
        //删除家庭角色
        familyMapper.deleteFamilyRoleByUserIds(userIds);
        return Result.success();
    }

    /**
     * 删除家庭成员
     * @param id
     * @return
     */
    @Override
    @Transactional
    public Result deleteFamilyMember(Long id)
    {
        Family family = familyMapper.selectFamilyByUserId(id);
        if(family == null)
        {
            throw new UserNotAddFamilyException("用户未加入家庭");
        }
        //删除家庭成员
        familyMapper.deleteFamilyMemberByUserId(id);
        //删除家庭角色
        familyMapper.deleteFamilyRoleByUserId(id);
        //家庭成员-1
        family.setFamilySize(family.getFamilySize() - 1);
        //更新家庭信息
        familyMapper.updateFamily(family);
        return Result.success();
    }

    /**
     * 创建孩子账号
     * @param userRegisterDto
     * @return
     */
    @Override
    public Result createChildAccount(UserRegisterDto userRegisterDto)
    {
        //判断年龄，必须在14岁以下
        Integer age = userRegisterDto.getAge();
        if(age > 14 || age < 0)
        {
            throw new AgeDoesNotMeetRequirement("年龄不符合要求");
        }
        //更新用户的值
        User user = User.builder()
                .username(userRegisterDto.getUsername())
                .age(age)
                .sex(userRegisterDto.getSex())
                .height(userRegisterDto.getHeight())
                .weight(userRegisterDto.getWeight())
                .name(userRegisterDto.getFullName())
                .build();
        userUpdateService.updateUserInfo(user);
        //查询用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, userRegisterDto.getUsername());
        user = userMapper.selectOne(wrapper);
        //将用户绑定到家庭
        Long userId = BaseContext.getCurrentId();
        Family family = familyMapper.selectFamilyByUserId(userId);
        //家庭加一
        family.setFamilySize(family.getFamilySize() + 1);
        familyMapper.updateFamily(family);
        //添加家庭成员
        FamilyMember familyMember = FamilyMember.builder()
                .familyId(family.getId())
                .userId(user.getId())
                .joinedAt(LocalDateTime.now()).build();
        familyMapper.addFamilyMember(familyMember);
        //添加家庭角色
        //TODO 定义变量 3为儿童
        familyMapper.addFamilyRole(user.getId().longValue(), 3);
        return Result.success();
    }


}
