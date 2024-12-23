package com.hsasys.service.update.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.hsasys.mapper.*;

import com.hsasys.domain.dto.UserUpdateDto;
import com.hsasys.domain.entity.Allergen;
import com.hsasys.domain.entity.ChronicDisease;
import com.hsasys.domain.entity.FoodPreference;
import com.hsasys.domain.entity.User;

import com.hsasys.domain.rela.UserUType;
import com.hsasys.exception.PasswordErrorException;
import com.hsasys.exception.PasswordIsEqualException;
import com.hsasys.exception.UpdateIsErrorException;
import com.hsasys.exception.UserIsExistException;
import com.hsasys.result.Result;
import com.hsasys.service.update.UUserService;
import com.hsasys.utils.BMI;
import com.hsasys.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UUserServiceImpl implements UUserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserUTypeMapper userUTypeMapper;

    @Autowired
    private AllergenMapper allergenMapper;

    @Autowired
    private ChronicDiseaseMapper chronicDiseaseMapper;

    @Autowired
    private FoodPreferenceMapper foodPreferenceMapper;

    @Override
    @Transactional
    public Result updateUserInfo(User user)
    {

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername());
        User u= userMapper.selectOne(wrapper);
        if(u == null)
        {
            throw new UserIsExistException("用户不存在");
        }
        user.setId(u.getId());
        // 先将数据进行更新
        int i = userMapper.updateById(user);
        if(i == 0)
        {
            throw new UpdateIsErrorException("更新失败,请重试");
        }

        u = userMapper.selectOne(wrapper);
        // 判断height和weight是否存在
        if(u.getHeight() != null && u.getWeight() != null)
        {
            // bmi算法
            double bmi = BMI.calculateBMI(u.getWeight(), u.getHeight());
            u.setBmi(bmi);
            userMapper.updateById(u); // 更新bmi
            Integer type = BMI.interpretBMI(bmi); // 获取bmi类型
            // 查询关联表
            LambdaQueryWrapper<UserUType> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(UserUType::getUserId, user.getId());
            UserUType userUType = userUTypeMapper.selectOne(wrapper1);
            // 关联表是否有该用户的关联信息 有则覆盖 没有则添加
            if(userUType == null)
            {
                int insert = userUTypeMapper.insert(new UserUType(null, u.getId(), type));
                if(insert <= 0)
                {
                    throw new UpdateIsErrorException("更新失败, 请重试");
                }
            }
            else
            {
                int update = userUTypeMapper.updateById(new UserUType(userUType.getId(), u.getId(), type));
                if(update <= 0)
                {
                    throw new UpdateIsErrorException("更新失败, 请重试");
                }
            }
        }

        return Result.success();
    }

    /**
     * 更新用户信息
     * @param userUpdateDto
     * @return
     */
    @Override
    @Transactional
    public Result updateUser(UserUpdateDto userUpdateDto)
    {
        System.out.println(userUpdateDto);
        System.out.println(userUpdateDto.getPreferenceIds());
        //如果密码更改
        if(userUpdateDto.getPassword() != null && userUpdateDto.getOldPassword() != null)
        {
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getId, userUpdateDto.getId());
            User u = userMapper.selectOne(wrapper);
            if(!u.getPassword().equals(userUpdateDto.getOldPassword()))
            {
                throw new PasswordErrorException("初始密码错误，请重试！");
            }
            if(u.getPassword().equals(userUpdateDto.getPassword()))
            {
                throw new PasswordIsEqualException("新旧密码不能相同");
            }

        }
        User user = BeanCopyUtils.copyBean(userUpdateDto, User.class);
        if(userUpdateDto.getHeight() != null && userUpdateDto.getWeight() != null)
        {
            double bmi = BMI.calculateBMI(userUpdateDto.getWeight(), userUpdateDto.getHeight());
            user.setBmi(bmi);
        }
        //更新基本的数据
        userMapper.updateById(user);
        //更新过敏源
        allergenMapper.deleteByUserId(user.getId()); // 先删除
        if(userUpdateDto.getAllergenIds() != null && !userUpdateDto.getAllergenIds().isEmpty())
        {
            allergenMapper.insertBatch(userUpdateDto.getAllergenIds(), user.getId()); //批量添加
        }
        //更新慢性疾病
        chronicDiseaseMapper.deleteByUserId(user.getId()); // 先删除
        if (userUpdateDto.getDiseaseIds() != null && !userUpdateDto.getDiseaseIds().isEmpty())
        {
            chronicDiseaseMapper.insertBatch(userUpdateDto.getDiseaseIds(), user.getId()); //批量添加
        }
        //更新饮食偏好
        foodPreferenceMapper.deleteByUserId(user.getId()); // 先删除
        if(userUpdateDto.getPreferenceIds() != null && !userUpdateDto.getPreferenceIds().isEmpty())
        {
            foodPreferenceMapper.insertBatch(userUpdateDto.getPreferenceIds(), user.getId()); //批量添加
        }
        return Result.success();
    }

    @Override
    public Result<List<Allergen>> getAllergens()
    {
        return Result.success(allergenMapper.selectList(null));
    }

    @Override
    public Result<List<ChronicDisease>> getDiseases() {
        return Result.success(chronicDiseaseMapper.selectList(null));
    }

    @Override
    public Result<List<FoodPreference>> getPreferences()
    {
        return Result.success(foodPreferenceMapper.selectList(null));
    }


}
