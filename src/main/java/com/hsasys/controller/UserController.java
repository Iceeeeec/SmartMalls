package com.hsasys.controller;

import com.hsasys.controller.tools.Result;
import com.hsasys.domain.User;
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
    Result login(@RequestBody User user){
        System.out.println(user);
        return sUserService.login(user);
    }

    @RequestMapping("/register")
    Result register(@RequestBody User user){
//        System.out.println(user);
        return sUserService.register(user);
    }

    @RequestMapping("/hello")
    Result hello(@RequestBody User user){
//        System.out.println(user);
        return sUserService.getUsertype(user);
    }

    @RequestMapping("/user")
    Result insertUser(@RequestBody User user){
        return iUserService.userInsert(user);
    }

//    @RequestMapping("/usertype")
//    Result insertUserType(@RequestBody UserType userType){
//        return userInsertService.userTypeInsert(userType);
//    }

    @RequestMapping("/uusertype")
    Result insertRelaUUserType(@RequestParam("userId")Integer userId, @RequestParam("userTypeId")Integer userTypeId){
        return iUserService.relaUserUTypeInsert(userId, userTypeId);
    }

    @RequestMapping("/addcollection")
    Result addCollection(@RequestParam("userId")Integer userId, @RequestParam("foodId")Integer foodId){
        return uUserService.addCollection(userId, foodId);
    }

    @RequestMapping("/personal")
    Result getPersonalInfo(@RequestParam("userId")Integer userId){
        return sUserService.getPersonalInfo(userId);
    }

    @RequestMapping("/getcollection")
    Result getCollectionInfo(@RequestParam("userId")Integer userId){
        return sUserService.getCollectionInfo(userId);
    }

    @RequestMapping("/updateuser")
    Result updateUserInfo(@RequestBody User user){
        return uUserService.updateUserInfo(user);
    }

}
