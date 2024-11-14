package com.hsasys.service.update.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hsasys.controller.tools.BMI;
import com.hsasys.controller.tools.Code;
import com.hsasys.controller.tools.Result;
import com.hsasys.dao.domain_mapper.FinishedFoodMapper;
import com.hsasys.dao.domain_mapper.UserMapper;
import com.hsasys.dao.rela_mapper.UserFoodCollectionMapper;
import com.hsasys.dao.rela_mapper.UserUTypeMapper;
import com.hsasys.domain.FinishedFood;
import com.hsasys.domain.User;
import com.hsasys.domain.rela.UserFoodCollection;
import com.hsasys.domain.rela.UserUType;
import com.hsasys.service.update.UUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private final Result err_result = new Result(Code.ERR,null);

    @Override
    public Result updateUserInfo(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getId, user.getId());
        User user1 = userMapper.selectOne(wrapper);
        if(user1 == null)
            return err_result;

        // 先将数据进行更新
        int i = userMapper.updateById(user);
        if(i == 0)
            return err_result;

        User user2 = userMapper.selectOne(wrapper);
        // 判断height和weight是否存在
        if(user2.getHeight() != null && user2.getWeight() != null){
            // bmi算法
            double bmi = BMI.calculateBMI(user2.getWeight(), user2.getHeight());
//            System.out.println(bmi+"\n\n");
            user2.setBmi(bmi);
            Integer type = BMI.interpretBMI(bmi);
//            System.out.println(type+"\n\n");
            // 查询关联表
            LambdaQueryWrapper<UserUType> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(UserUType::getUser, user2.getId());
            UserUType userUType = userUTypeMapper.selectOne(wrapper1);
            // 关联表是否有该用户的关联信息 有则覆盖 没有则添加
            if(userUType == null) {
                int insert = userUTypeMapper.insert(new UserUType(null, user2.getId(), type));
                if(insert <= 0)
                    return err_result;
            }
            else {
                int update = userUTypeMapper.updateById(new UserUType(userUType.getId(), user2.getId(), type));
                if(update <= 0)
                    return  err_result;
            }
        }

        return new Result(Code.OK, null, "successful");
    }

    @Override
    public Result addCollection(Integer userId, Integer foodId) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getId, userId);
        User user = userMapper.selectOne(wrapper);
        if(user == null)
            return err_result;
        LambdaQueryWrapper<FinishedFood> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(FinishedFood::getId, foodId);
        FinishedFood finishedFood = finishedFoodMapper.selectOne(wrapper1);
        if(finishedFood == null)
            return err_result;

        // 判断是否已有收藏
        LambdaQueryWrapper<UserFoodCollection> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(UserFoodCollection::getUserId, userId).eq(UserFoodCollection::getFoodId, foodId);
        UserFoodCollection userFoodCollection = userFoodCollectionMapper.selectOne(wrapper2);
        if(userFoodCollection != null)
            return err_result;

        int insert = userFoodCollectionMapper.insert(new UserFoodCollection(null, userId, foodId));
        if(insert <=0)
            return err_result;

        return new Result(Code.OK, null, "successful");
    }
}
