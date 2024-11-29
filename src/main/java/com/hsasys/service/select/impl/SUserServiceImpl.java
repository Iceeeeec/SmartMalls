package com.hsasys.service.select.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hsasys.constant.JwtClaimsConstant;
import com.hsasys.controller.tools.Code;

import com.hsasys.dao.domain_mapper.*;
import com.hsasys.dao.rela_mapper.UserFoodCollectionMapper;
import com.hsasys.dao.rela_mapper.UserUTypeMapper;
import com.hsasys.domain.FinishedFood;
import com.hsasys.domain.UserType;
import com.hsasys.domain.dto.UserLoginDto;
import com.hsasys.domain.entity.Allergen;
import com.hsasys.domain.entity.ChronicDisease;
import com.hsasys.domain.entity.FoodPreference;
import com.hsasys.domain.entity.User;
import com.hsasys.domain.dto.UserRegisterDto;
import com.hsasys.domain.rela.UserFoodCollection;
import com.hsasys.domain.vo.UserLoginVo;
import com.hsasys.domain.vo.UserUTypeVo;
import com.hsasys.exception.PasswordErrorException;
import com.hsasys.exception.UserIsExistException;
import com.hsasys.exception.UserIsNotExistException;
import com.hsasys.properties.JwtProperties;
import com.hsasys.result.Result;
import com.hsasys.service.select.SUserService;
import com.hsasys.service.update.UUserService;
import com.hsasys.utils.BMI;
import com.hsasys.utils.BeanCopyUtils;
import com.hsasys.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private AllergenMapper allergenMapper;

    @Autowired
    private ChronicDiseaseMapper chronicDiseaseMapper;

    @Autowired
    private FoodPreferenceMapper foodPreferenceMapper;

    @Autowired
    private UUserService userUpdateService;

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public Result getPersonalInfo(Integer userId) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getId, userId);
        User user = userMapper.selectOne(wrapper);
        if(user == null)
            return Result.error("用户不存在");

        return Result.success(user);
    }

    @Override
    public Result getCollectionInfo(Integer userId) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getId, userId);
        User user = userMapper.selectOne(wrapper);
        if(user == null)
            return Result.error("用户不存在");

        LambdaQueryWrapper<UserFoodCollection> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(UserFoodCollection::getUserId, userId);
        List<UserFoodCollection> userFoodCollections = userFoodCollectionMapper.selectList(wrapper1);
        if(userFoodCollections == null)
            return Result.error("用户没有收藏");

        List<FinishedFood> finishedFoods = new ArrayList<>();
        for(UserFoodCollection userFoodCollection:userFoodCollections){
            LambdaQueryWrapper<FinishedFood> wrapper2 = new LambdaQueryWrapper<>();
            wrapper2.eq(FinishedFood::getId, userFoodCollection.getFoodId());
            FinishedFood finishedFood = finishedFoodMapper.selectOne(wrapper2);
            if(finishedFood == null)
                return Result.error("收藏的菜品不存在");
            finishedFoods.add(finishedFood);
        }

        return Result.success(finishedFoods);
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
    public Result<UserLoginVo> login(UserLoginDto userLoginDto)
    {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, userLoginDto.getUsername());
        User user = userMapper.selectOne(wrapper);
        if(user == null)
        {
            throw new UserIsNotExistException("用户不存在！");
        }
        if(!user.getPassword().equals(userLoginDto.getPassword()))
        {
            throw new PasswordErrorException("密码错误");
        }
        //判断完成后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                claims
        );
        //根据id查询用户的类型
        List<UserType> types = userMapper.selectUserTypesById(user.getId());
        UserUTypeVo typeList = UserUTypeVo.builder().type(types).build();
        //根据id查询过敏源
        List<Allergen> allergens =  allergenMapper.selectAllergensById(user.getId());
        //根据id查询慢性疾病
        List<ChronicDisease> chronicDiseases = chronicDiseaseMapper.selectChronicDiseaseById(user.getId());
        //根据id查询饮食偏好
        List<FoodPreference> foodPreferences = foodPreferenceMapper.selectPreferenceById(user.getId());
        //封装返回用户信息
        UserLoginVo userLoginVo = BeanCopyUtils.copyBean(user, UserLoginVo.class);
        userLoginVo.setUserUTypeVo(typeList);
        userLoginVo.setAllergen(allergens);
        userLoginVo.setDisease(chronicDiseases);
        userLoginVo.setPreference(foodPreferences);
        userLoginVo.setToken(token);
        return Result.success(userLoginVo);
    }

    @Override
    public void register(UserRegisterDto userRegisterDto)
    {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, userRegisterDto.getUsername());
        User u = userMapper.selectOne(wrapper);
        if(userRegisterDto.getFullName() == null)//通过昵称来判断添加还是更新
        {
            if (u != null)
            {
                throw new UserIsExistException("用户已存在, 请重新注册！");
            }
            User user = User.builder()
                    .username(userRegisterDto.getUsername())
                    .password(userRegisterDto.getPassword())
                    .build();
            userMapper.addUser(user);
        }
        else
        {
            User user = User.builder()
                    .username(userRegisterDto.getUsername())
                    .age(userRegisterDto.getAge())
                    .sex(userRegisterDto.getSex())
                    .height(userRegisterDto.getHeight())
                    .weight(userRegisterDto.getWeight())
                    .name(userRegisterDto.getFullName())
                    .build();
            userUpdateService.updateUserInfo(user);
        }
    }

    @Override
    public Result getUsertype(User user)
    {
//        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(User::getName, user.getName());
//        User user1 = userMapper.selectOne(wrapper);
//        System.out.println(user1);
//
//        LambdaQueryWrapper<UserUType> wrapper2 = new LambdaQueryWrapper<>();
//        wrapper2.eq(UserUType::getUser,user1.getId());
//        List<UserUType> userUTypes = userUTypeMapper.selectList(wrapper2);
//        System.out.println(userUTypes);
//        if(userUTypes == null)
//            return err_result;
//
//        List<UserType> userTypes = new ArrayList<>();
//        for(UserUType u:userUTypes)
//        {
//            LambdaQueryWrapper<UserType> wrapper1 = new LambdaQueryWrapper<>();
//            wrapper1.eq(UserType::getId,u.getUtype());
//            UserType userType = userTypeMapper.selectOne(wrapper1);
//            userTypes.add(userType);
//        }
//
//        return new Result(Code.OK,user1, userTypes);
        return null;
    }
}
