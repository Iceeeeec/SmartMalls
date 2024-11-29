package com.hsasys.controller;

import com.hsasys.domain.dto.UserLoginDto;
import com.hsasys.domain.dto.UserUpdateDto;
import com.hsasys.domain.entity.Allergen;
import com.hsasys.domain.entity.ChronicDisease;
import com.hsasys.domain.entity.FoodPreference;
import com.hsasys.domain.entity.User;
import com.hsasys.domain.dto.UserRegisterDto;
import com.hsasys.result.Result;
import com.hsasys.service.insert.IUserService;
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
    private IUserService iUserService;
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

    @RequestMapping("/hello")
    Result hello(@RequestBody User user){
//        System.out.println(user);
//        return sUserService.getUsertype(user);
        return null;
    }

    @RequestMapping("/user")
    Result insertUser(@RequestBody User user){
//        return iUserService.userInsert(user);
        return null;
    }


    @RequestMapping("/uusertype")
    Result insertRelaUUserType(@RequestParam("userId")Integer userId, @RequestParam("userTypeId")Integer userTypeId){
//        return iUserService.relaUserUTypeInsert(userId, userTypeId);
        return null;
    }

    @RequestMapping("/addcollection")
    Result addCollection(@RequestParam("userId")Integer userId, @RequestParam("foodId")Integer foodId){
//        return uUserService.addCollection(userId, foodId);
        return null;
    }

    @RequestMapping("/personal")
    Result getPersonalInfo(@RequestParam("userId")Integer userId)
    {
//        return sUserService.getPersonalInfo(userId);
        return null;
    }

    @RequestMapping("/getcollection")
    Result getCollectionInfo(@RequestParam("userId")Integer userId){
//        return sUserService.getCollectionInfo(userId);
        return null;
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
    Result<List<ChronicDisease>> getDiseases()
    {
        return uUserService.getDiseases();
    }
    /**
     * 所有的饮食偏好
     */
    @GetMapping("/preference")
    Result<List<FoodPreference>> getPreferences()
    {
        return uUserService.getPreferences();
    }
}
