package com.hsasys.controller;

import com.hsasys.domain.dto.UserLoginDto;
import com.hsasys.domain.dto.UserUpdateDto;
import com.hsasys.domain.entity.Allergen;
import com.hsasys.domain.entity.ChronicDisease;
import com.hsasys.domain.entity.FoodPreference;
import com.hsasys.domain.dto.UserRegisterDto;
import com.hsasys.domain.entity.UserType;
import com.hsasys.domain.vo.ReportVo;
import com.hsasys.domain.vo.UserLoginVo;
import com.hsasys.result.Result;
import com.hsasys.service.select.SUserService;
import com.hsasys.service.update.UUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Transactional
@CrossOrigin
@ResponseBody
@RequestMapping("/user")
public class UserController {
    @Autowired
    private SUserService sUserService;

    @Autowired
    private UUserService uUserService;

    @PostMapping("/login")
    Result login(@RequestBody UserLoginDto userLoginDto)
    {
         return sUserService.login(userLoginDto);
    }

    @PostMapping("/register")
    Result register(@RequestBody UserRegisterDto userRegisterDto)
    {
        sUserService.register(userRegisterDto);
        return Result.success();
    }


    @PutMapping("/update")
    Result updateUserInfo(@RequestBody UserUpdateDto userUpdateDto)
    {
        return uUserService.updateUser(userUpdateDto);
    }

    /**
     * 所有的过敏源
     * @return
     */
    @GetMapping("/allergen")
    Result<List<Allergen>> getAllergens()
    {
        return uUserService.getAllergens();
    }
    /**
     * 所有的慢性疾病
     */
    @GetMapping("/disease")
    public Result<List<ChronicDisease>> getDiseases()
    {
        return uUserService.getDiseases();
    }

    /**
     * 所有的饮食偏好
     */
    @GetMapping("/preference")
    public Result<List<FoodPreference>> getPreferences()
    {
        return uUserService.getPreferences();
    }

    /**
     * 用户类型
     * @return
     */
    @GetMapping("/type")
    public Result<UserType> getUserTypes()
    {
        return uUserService.selectType();
    }

    /**
     * 健康评分
     * @return
     */
    @GetMapping("/score")
    public Result<Integer> getScore(@RequestParam(required = false) Long memberId)
    {
        return uUserService.getScore(memberId);
    }

    /**
     * 查看提交的体检报告
     */
    @GetMapping("/report")
    public Result<List<ReportVo>> getReports()
    {
        return sUserService.selectReports();
    }
    /**
     * 根据userId查询相关健康信息
     */
    @GetMapping("/healthInfo/{userId}")
    public Result<UserLoginVo> getUserInfo(@PathVariable Integer userId)
    {
        return sUserService.selectUserHealthInfo(userId);
    }
}
