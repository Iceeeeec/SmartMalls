package com.hsasys.controller;

import com.hsasys.result.Result;
import com.hsasys.service.select.SFinishedFoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
    public Result selectAllFoods(){
        return sFinishedFoodService.selectAllFoods();
    }

    /*
    * 返回类型 List<Map<String,Object>>
    * */
    @RequestMapping("/get")
    public Result getByName(@RequestParam String name)
    {
        return sFinishedFoodService.selectOne( name );
    }

    @GetMapping("/getById")
    public Result getById(@RequestParam Integer id){
        return sFinishedFoodService.selectById( id );
    }
}
