package com.hsasys.controller;

import com.hsasys.controller.tools.Result;
import com.hsasys.domain.vo.ThreeMealReceiveVo;
import com.hsasys.service.insert.IThreeMealService;
import com.hsasys.service.select.SThreeMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Controller
@Transactional
@ResponseBody
@CrossOrigin
@RequestMapping("/meal")
public class ThreeMealController {
    @Autowired
    private IThreeMealService iThreeMealService;
    @Autowired
    private SThreeMealService sThreeMealService;

    @RequestMapping("/insert")
    public Result insertThreeMeal(@RequestBody ThreeMealReceiveVo vo){
        return iThreeMealService.insertThreeMeal(vo);
    }

    @RequestMapping("/get")
    public Result getAllMeals(@RequestParam("userId") Integer userId){
        return sThreeMealService.getAllMeals(userId);
    }

}
