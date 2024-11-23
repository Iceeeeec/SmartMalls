package com.hsasys.service.insert.impl;

import com.hsasys.controller.tools.Code;
import com.hsasys.controller.tools.Result;
import com.hsasys.dao.domain_mapper.UserMapper;
import com.hsasys.dao.domain_mapper.UserTypeMapper;
import com.hsasys.dao.rela_mapper.UserUTypeMapper;
import com.hsasys.domain.entity.User;
import com.hsasys.service.insert.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IUserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserTypeMapper userTypeMapper;
    @Autowired
    private UserUTypeMapper userUTypeMapper;

    private final Result err_result = new Result(Code.ERR,null);

    @Override
    public Result userInsert(User user) {
//        if(userMapper.insert(user) <= 0)
//            return err_result;
        return new Result(Code.OK, null, "OK");
    }

//    @Override
//    public Result userTypeInsert(UserType userType) {
//        if(userTypeMapper.insert(userType) <= 0)
//            return err_result;
//        return new Result(Code.OK, null , "OK");
//    }

    @Override
    public Result relaUserUTypeInsert(Integer userId, Integer userTypeId) {
//        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(User::getId, userId);
//        User user1 = userMapper.selectOne(wrapper);
        // 获取height和weight

//        LambdaQueryWrapper<UserType> wrapper1 = new LambdaQueryWrapper<>();
//        wrapper1.eq(UserType::getId, userTypeId);
//        UserType userType1 = userTypeMapper.selectOne(wrapper1);
//
//        int insert = userUTypeMapper.insert(new UserUType(null, user1.getId(), userType1.getId()));
//        if(insert <= 0)
//            return err_result;

        return new Result(Code.OK, null, "OK");
    }
}
