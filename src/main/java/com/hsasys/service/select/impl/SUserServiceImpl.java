package com.hsasys.service.select.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hsasys.constant.JwtClaimsConstant;

import com.hsasys.mapper.AllergenMapper;
import com.hsasys.mapper.ChronicDiseaseMapper;
import com.hsasys.mapper.FoodPreferenceMapper;
import com.hsasys.mapper.UserMapper;

import com.hsasys.domain.entity.UserType;
import com.hsasys.domain.dto.UserLoginDto;
import com.hsasys.domain.entity.Allergen;
import com.hsasys.domain.entity.ChronicDisease;
import com.hsasys.domain.entity.FoodPreference;
import com.hsasys.domain.entity.User;
import com.hsasys.domain.dto.UserRegisterDto;

import com.hsasys.domain.vo.UserLoginVo;
import com.hsasys.domain.vo.UserUTypeVo;
import com.hsasys.exception.PasswordErrorException;
import com.hsasys.exception.UserIsExistException;
import com.hsasys.exception.UserIsNotExistException;
import com.hsasys.properties.JwtProperties;
import com.hsasys.result.Result;
import com.hsasys.service.select.SUserService;
import com.hsasys.service.update.UUserService;
import com.hsasys.utils.BeanCopyUtils;
import com.hsasys.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SUserServiceImpl implements SUserService {
    @Autowired
    private UserMapper userMapper;

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

    /**
     * 用户注册
     * @param userRegisterDto
     * @return
     */
    @Override
    public Result register(UserRegisterDto userRegisterDto)
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
        return Result.success();
    }

}
