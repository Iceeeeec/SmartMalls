package com.hsasys.controller;

import com.hsasys.result.Result;
import com.hsasys.service.select.SFinishedFoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@Transactional
@ResponseBody
@CrossOrigin
@RequestMapping("/food")
public class FoodController {
    @Autowired
    private SFinishedFoodService sFinishedFoodService;

    @RequestMapping("/all")
    Result selectAllFoods(){
        return sFinishedFoodService.selectAllFoods();
//        return null;
    }

    @RequestMapping("/one")
    Result selectOneFood(@RequestParam("id")String id){
        return sFinishedFoodService.selectOneFood(id);
//        return null;
    }



    @RequestMapping("/ingredients")
    Result selectAllIngredient(){
        return null;
    }

    /*
    * 返回类型 List<Map<String,Object>>
    * */
    @RequestMapping("/get")
    Result getone(@RequestParam String name){
        return sFinishedFoodService.selectOne( name );
    }
}
