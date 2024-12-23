package com.hsasys.controller;

import com.hsasys.domain.dto.FoodPageDto;
import com.hsasys.result.PageResult;
import com.hsasys.result.Result;
import com.hsasys.service.select.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Controller
@Transactional
@ResponseBody
@CrossOrigin
@RequestMapping("/food")
public class FoodController {

    @Autowired
    private FoodService foodService;

    /**
     * 根据参数查询食物
     * @param
     * @return
     */
    @PostMapping("/query")
    public Result<PageResult> selectFoodsByQuery(@RequestBody FoodPageDto foodPageDto)
    {
        return foodService.selectFoodsByQuery(foodPageDto);
    }
}
