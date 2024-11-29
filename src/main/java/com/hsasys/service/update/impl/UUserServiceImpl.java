package com.hsasys.service.update.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hsasys.dao.domain_mapper.FinishedFoodMapper;
import com.hsasys.dao.domain_mapper.UserMapper;
import com.hsasys.dao.rela_mapper.UserFoodCollectionMapper;
import com.hsasys.dao.rela_mapper.UserUTypeMapper;
import com.hsasys.domain.FinishedFood;
import com.hsasys.domain.entity.User;
import com.hsasys.domain.rela.UserFoodCollection;
import com.hsasys.domain.rela.UserUType;
import com.hsasys.exception.UpdateIsErrorException;
import com.hsasys.exception.UserIsExistException;
import com.hsasys.result.Result;
import com.hsasys.service.update.UUserService;
import com.hsasys.utils.BMI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UUserServiceImpl implements UUserService {
    @Autowired
    private UserFoodCollectionMapper userFoodCollectionMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FinishedFoodMapper finishedFoodMapper;
    @Autowired
    private UserUTypeMapper userUTypeMapper;


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

    @Override
    public Result addCollection(Integer userId, Integer foodId)
    {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getId, userId);
        User user = userMapper.selectOne(wrapper);
        if(user == null)
        {
            return Result.error("用户不存在");
        }
        LambdaQueryWrapper<FinishedFood> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(FinishedFood::getId, foodId);
        FinishedFood finishedFood = finishedFoodMapper.selectOne(wrapper1);
        if(finishedFood == null)
        {
            return Result.error("食物不存在");

        }

        // 判断是否已有收藏
        LambdaQueryWrapper<UserFoodCollection> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(UserFoodCollection::getUserId, userId).eq(UserFoodCollection::getFoodId, foodId);
        UserFoodCollection userFoodCollection = userFoodCollectionMapper.selectOne(wrapper2);
        if(userFoodCollection != null)
        {
            Result.error("已收藏");
        }

        int insert = userFoodCollectionMapper.insert(new UserFoodCollection(null, userId, foodId));
        if(insert <=0)
        {
            return Result.error("收藏失败");
        }

        return Result.success();
    }
}
