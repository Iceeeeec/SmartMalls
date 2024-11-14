package com.hsasys.service.select.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hsasys.controller.tools.BMI;
import com.hsasys.controller.tools.Code;
import com.hsasys.controller.tools.Result;
import com.hsasys.dao.domain_mapper.FinishedFoodMapper;
import com.hsasys.dao.domain_mapper.UserMapper;
import com.hsasys.dao.domain_mapper.UserTypeMapper;
import com.hsasys.dao.rela_mapper.UserFoodCollectionMapper;
import com.hsasys.dao.rela_mapper.UserUTypeMapper;
import com.hsasys.domain.FinishedFood;
import com.hsasys.domain.User;
import com.hsasys.domain.UserType;
import com.hsasys.domain.rela.UserFoodCollection;
import com.hsasys.domain.rela.UserUType;
import com.hsasys.service.select.SUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SUserServiceImpl implements SUserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserTypeMapper userTypeMapper;
    @Autowired
    private UserUTypeMapper userUTypeMapper;
    @Autowired
    private UserFoodCollectionMapper userFoodCollectionMapper;
    @Autowired
    private FinishedFoodMapper finishedFoodMapper;

    private static final Result err_result = new Result(Code.ERR,null);

    @Override
    public Result getPersonalInfo(Integer userId) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getId, userId);
        User user = userMapper.selectOne(wrapper);
        if(user == null)
            return err_result;

        return new Result(Code.OK, user, "successful");
    }

    @Override
    public Result getCollectionInfo(Integer userId) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getId, userId);
        User user = userMapper.selectOne(wrapper);
        if(user == null)
            return err_result;

        LambdaQueryWrapper<UserFoodCollection> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(UserFoodCollection::getUserId, userId);
        List<UserFoodCollection> userFoodCollections = userFoodCollectionMapper.selectList(wrapper1);
        if(userFoodCollections == null)
            return err_result;

        List<FinishedFood> finishedFoods = new ArrayList<>();
        for(UserFoodCollection userFoodCollection:userFoodCollections){
            LambdaQueryWrapper<FinishedFood> wrapper2 = new LambdaQueryWrapper<>();
            wrapper2.eq(FinishedFood::getId, userFoodCollection.getFoodId());
            FinishedFood finishedFood = finishedFoodMapper.selectOne(wrapper2);
            if(finishedFood == null)
                return err_result;
            finishedFoods.add(finishedFood);
        }

        return new Result(Code.OK, finishedFoods, "successful");
    }

    @Override
    public Double getBMI(Integer userId) {
//        LambdaQueryWrapper<User> wrapper=new LambdaQueryWrapper<>();
//        wrapper.eq( User::getHeight,user.getHeight() ).eq( User::getWeight,user.getWeight() );
//        User user1 = userMapper.selectOne( wrapper );
//        double bmi = BMI.calculateBMI(user1.getWeight(), user1.getHeight());
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getId, userId);
        User user = userMapper.selectOne(wrapper);
        double bmi = BMI.calculateBMI(user.getWeight(), user.getHeight());
        return  bmi;
    }

    @Override
    public Result login(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getName,user.getName());
        wrapper.select(User::getPassword);
        User user1 = userMapper.selectOne(wrapper);

        if (user1 == null)
        {  return err_result;}

        if(user1.getPassword().equals(user.getPassword()))
        {
            System.out.println(user1);
            return new Result(Code.OK,user1,"login was successful");}
        else
        {
        return err_result;}
    }

    @Override
    public Result register(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getName, user.getName());
        User user1 = userMapper.selectOne(wrapper);

        if(user1 != null)
            return err_result;

        int insert = userMapper.insert(user);

        if(insert <= 0)
            return err_result;

        return new Result(Code.OK, null, "register was successful");
    }

    @Override
    public Result getUsertype(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getName, user.getName());
        User user1 = userMapper.selectOne(wrapper);
        System.out.println(user1);

        LambdaQueryWrapper<UserUType> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(UserUType::getUser,user1.getId());
        List<UserUType> userUTypes = userUTypeMapper.selectList(wrapper2);
        System.out.println(userUTypes);
        if(userUTypes == null)
            return err_result;

        List<UserType> userTypes = new ArrayList<>();
        for(UserUType u:userUTypes){
            LambdaQueryWrapper<UserType> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(UserType::getId,u.getUtype());
            UserType userType = userTypeMapper.selectOne(wrapper1);
            userTypes.add(userType);
        }

        return new Result(Code.OK,user1, userTypes);
    }
}
