package com.hsasys.service.select.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hsasys.constant.JwtClaimsConstant;

import com.hsasys.context.BaseContext;
import com.hsasys.domain.dto.UserUpdateDto;
import com.hsasys.domain.entity.*;
import com.hsasys.domain.vo.FamilyMemberVo;
import com.hsasys.domain.vo.ReportVo;
import com.hsasys.exception.UserNoAuthToFindHeath;
import com.hsasys.mapper.*;

import com.hsasys.domain.dto.UserLoginDto;
import com.hsasys.domain.dto.UserRegisterDto;

import com.hsasys.domain.vo.UserLoginVo;
import com.hsasys.domain.vo.UserUTypeVo;
import com.hsasys.exception.PasswordErrorException;
import com.hsasys.exception.UserIsExistException;
import com.hsasys.exception.UserIsNotExistException;
import com.hsasys.properties.JwtProperties;
import com.hsasys.result.Result;
import com.hsasys.service.select.SUserService;
import com.hsasys.service.update.FamilyService;
import com.hsasys.service.update.UUserService;
import com.hsasys.utils.BeanCopyUtils;
import com.hsasys.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private FamilyService familyService;

    /**
     * 获取用户健康信息
     * @param user
     * @return
     */
    public UserLoginVo getUserHealthVo(User user)
    {
        //根据id查询用户的类型(只是bmi的)
        UserType type = userMapper.selectUserTypeById(user.getId().longValue());
        //根据id查询过敏源
        List<Allergen> allergens =  allergenMapper.selectAllergensById(user.getId());
        //根据id查询慢性疾病
        List<ChronicDisease> chronicDiseases = chronicDiseaseMapper.selectChronicDiseaseById(user.getId());
        //根据id查询饮食偏好
        List<FoodPreference> foodPreferences = foodPreferenceMapper.selectPreferenceById(user.getId());
        //查询用户在家庭的角色
        FamilyRole familyRole = familyService.getFamilyRoleByUserId(user.getId().longValue());
        //封装返回用户信息
        UserLoginVo userLoginVo = BeanCopyUtils.copyBean(user, UserLoginVo.class);
        userLoginVo.setUserType(type);
        userLoginVo.setAllergen(allergens);
        userLoginVo.setDisease(chronicDiseases);
        userLoginVo.setPreference(foodPreferences);
        userLoginVo.setFamilyRole(familyRole);
        return userLoginVo;
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
        UserLoginVo userLoginVo = getUserHealthVo(user);
        userLoginVo.setToken(token);
        return Result.success(userLoginVo);
    }

    /**
     * 用户注册
     * @param userRegisterDto
     * @return
     */
    @Override
    @Transactional
    public Result register(UserRegisterDto userRegisterDto)
    {
        if(!userRegisterDto.getPassword().equals(userRegisterDto.getRePassword()))
        {
            throw new PasswordErrorException("两次密码不一致！请重新输入");
        }
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, userRegisterDto.getUsername());
        User u = userMapper.selectOne(wrapper);
        if(userRegisterDto.getFullName() == null || userRegisterDto.getFullName().isEmpty())//通过昵称来判断添加还是更新
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

    @Override
    public Result<List<ReportVo>> selectReports()
    {
        Long userId = BaseContext.getCurrentId();
        //查询用户信息
        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(User::getId, userId);
        User user = userMapper.selectOne(userWrapper);
        //根据用户id查询相关上传体检报告记录
        LambdaQueryWrapper<PhysicalReport> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PhysicalReport::getUserId, userId)
                .orderByAsc(PhysicalReport::getUploadDate);
        List<PhysicalReport> reports = reportMapper.selectList(wrapper);
        //返回vo
        List<ReportVo> reportVos = BeanCopyUtils.copyBeanList(reports, ReportVo.class);
        //为每一条记录userName
        reportVos.forEach(reportVo -> reportVo.setUserName(user.getName()));

        return Result.success(reportVos);
    }

    @Override
    public Result<UserLoginVo> selectUserHealthInfo(Integer userId) {
        // 获取到User的信息
        User user = userMapper.selectById(userId);
        Result<List<FamilyMemberVo>> familyMember = familyService.getFamilyMember();
        // 获取到familyMember的信息，根据familyMember的id来判断这个userId是否在familyMember中
        List<FamilyMemberVo> familyMemberData = familyMember.getData();
        List<Integer> familyMemberIds = familyMemberData.stream()
                .map(familyMemberVo -> familyMemberVo.getUserId())
                .collect(Collectors.toList());
        if(!familyMemberIds.contains(userId)){
            throw new UserNoAuthToFindHeath("用户没有权限获取用户健康信息");
        }
        System.out.println(user);
        UserLoginVo userHealthVo = getUserHealthVo(user);
        // 成员信息就不需要封装token了
        return Result.success(userHealthVo);
    }

}
