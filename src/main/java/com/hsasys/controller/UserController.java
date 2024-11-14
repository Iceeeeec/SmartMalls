package com.hsasys.controller;

import com.hsasys.DecisionTree;
import com.hsasys.controller.tools.Code;
import com.hsasys.controller.tools.Result;
import com.hsasys.domain.User;
import com.hsasys.domain.UserType;
import com.hsasys.service.insert.IUserService;
import com.hsasys.service.select.SUserService;
import com.hsasys.service.update.UUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

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

    @RequestMapping("/recommend")
    Result recommendFood(@RequestParam("userId") Integer userId){
        Double BMI = sUserService.getBMI( userId );
        ArrayList<String> featLabels = new ArrayList<>();
        if(BMI<18.5){
            ArrayList<Object[]> dataSet = new ArrayList<>();
            dataSet.add(new Object[]{0, 0, 0, 0, "no"});
            dataSet.add(new Object[]{0, 0, 0, 1, "no"});
            dataSet.add(new Object[]{0, 1, 0, 1, "yes"});
            dataSet.add(new Object[]{0, 1, 1, 0, "yes"});
            dataSet.add(new Object[]{0, 0, 0, 0, "no"});
            dataSet.add(new Object[]{1, 0, 0, 0, "no"});
            dataSet.add(new Object[]{1, 0, 0, 1, "no"});
            dataSet.add(new Object[]{1, 1, 1, 1, "yes"});
            dataSet.add(new Object[]{1, 0, 1, 2, "yes"});
            dataSet.add(new Object[]{1, 0, 1, 2, "yes"});
            dataSet.add(new Object[]{2, 0, 1, 2, "yes"});
            dataSet.add(new Object[]{2, 0, 1, 1, "yes"});
            dataSet.add(new Object[]{2, 1, 0, 1, "yes"});
            dataSet.add(new Object[]{2, 1, 0, 2, "yes"});
            dataSet.add(new Object[]{2, 0, 0, 0, "no"});
            ArrayList<String> labels = new ArrayList<>();
            labels.add("水果");
            labels.add("甜品");
            labels.add("坚果，鱼类，蛋白");
            labels.add("零食");
            labels.add("偏瘦");
            DecisionTree.TreeNode tree = DecisionTree.createTree( dataSet, labels, featLabels );
            return new Result(Code.OK,tree.label);
        }else if(BMI>=18.5&&BMI<25){
            ArrayList<Object[]> dataSet = new ArrayList<>();
            dataSet.add(new Object[]{0, 0, 0, 0, "no"});
            dataSet.add(new Object[]{0, 0, 0, 1, "no"});
            dataSet.add(new Object[]{0, 1, 0, 1, "yes"});
            dataSet.add(new Object[]{0, 1, 1, 0, "yes"});
            dataSet.add(new Object[]{0, 0, 0, 0, "no"});
            dataSet.add(new Object[]{1, 0, 0, 0, "no"});
            dataSet.add(new Object[]{1, 0, 0, 1, "no"});
            dataSet.add(new Object[]{1, 1, 1, 1, "yes"});
            dataSet.add(new Object[]{1, 0, 1, 2, "yes"});
            dataSet.add(new Object[]{1, 0, 1, 2, "yes"});
            dataSet.add(new Object[]{2, 0, 1, 2, "yes"});
            dataSet.add(new Object[]{2, 0, 1, 1, "yes"});
            dataSet.add(new Object[]{2, 1, 0, 1, "yes"});
            dataSet.add(new Object[]{2, 1, 0, 2, "yes"});
            dataSet.add(new Object[]{2, 0, 0, 0, "no"});
            ArrayList<String> labels = new ArrayList<>();
            labels.add("水果");
            labels.add("甜品");
            labels.add("燕麦，瘦肉");
            labels.add("零食");
            labels.add("正常");
            DecisionTree.TreeNode tree = DecisionTree.createTree( dataSet, labels, featLabels );
            return new Result(Code.OK,tree.label);
        }else{
            ArrayList<Object[]> dataSet = new ArrayList<>();
            dataSet.add(new Object[]{0, 0, 0, 0, "no"});
            dataSet.add(new Object[]{0, 0, 0, 1, "no"});
            dataSet.add(new Object[]{0, 1, 0, 1, "yes"});
            dataSet.add(new Object[]{0, 1, 1, 0, "yes"});
            dataSet.add(new Object[]{0, 0, 0, 0, "no"});
            dataSet.add(new Object[]{1, 0, 0, 0, "no"});
            dataSet.add(new Object[]{1, 0, 0, 1, "no"});
            dataSet.add(new Object[]{1, 1, 1, 1, "yes"});
            dataSet.add(new Object[]{1, 0, 1, 2, "yes"});
            dataSet.add(new Object[]{1, 0, 1, 2, "yes"});
            dataSet.add(new Object[]{2, 0, 1, 2, "yes"});
            dataSet.add(new Object[]{2, 0, 1, 1, "yes"});
            dataSet.add(new Object[]{2, 1, 0, 1, "yes"});
            dataSet.add(new Object[]{2, 1, 0, 2, "yes"});
            dataSet.add(new Object[]{2, 0, 0, 0, "no"});
            ArrayList<String> labels = new ArrayList<>();
            labels.add("水果");
            labels.add("甜品");
            labels.add("蔬菜水果");
            labels.add("零食");
            labels.add("偏胖");
            DecisionTree.TreeNode tree = DecisionTree.createTree( dataSet, labels, featLabels );
            return new Result(Code.OK,tree.label);
        }

    }
}
