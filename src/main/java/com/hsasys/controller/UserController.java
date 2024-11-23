package com.hsasys.controller;

import com.hsasys.domain.dto.UserLoginDto;
import com.hsasys.domain.entity.User;
import com.hsasys.domain.dto.UserRegisterDto;
import com.hsasys.domain.vo.UserLoginVo;
import com.hsasys.result.Result;
import com.hsasys.service.insert.IUserService;
import com.hsasys.service.select.SUserService;
import com.hsasys.service.update.UUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping("/login")
    Result login(@RequestBody UserLoginDto userLoginDto)
    {
         return sUserService.login(userLoginDto);
    }

    @RequestMapping("/register")
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

    @RequestMapping("/updateuser")
    Result updateUserInfo(@RequestBody User user)
    {
//        return uUserService.updateUserInfo(user);
        return null;
    }


}
