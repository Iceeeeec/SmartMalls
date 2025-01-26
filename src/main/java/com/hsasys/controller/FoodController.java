package com.hsasys.controller;

import com.hsasys.domain.dto.FoodPageDto;
import com.hsasys.domain.dto.FoodRecommendPageDto;
import com.hsasys.domain.entity.Food;
import com.hsasys.domain.entity.Nutrition;
import com.hsasys.domain.vo.FoodDetailVo;
import com.hsasys.domain.vo.FoodSearchVo;
import com.hsasys.domain.vo.FoodVo;
import com.hsasys.result.PageResult;
import com.hsasys.result.Result;
import com.hsasys.service.select.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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

    /**
     * 根据id查询食物
     */
    @GetMapping("/{id}")
    public Result<FoodDetailVo> selectFoodById(@PathVariable Integer id)
    {
        return foodService.selectFoodById(id);
    }

    /**
     * 查询所有营养成分，完成食品对比
     */
    @PostMapping("/nutrition")
    public Result<List<FoodVo>> getNutrition(@RequestBody List<Integer> ids)
    {
        return foodService.selectNutrition(ids);
    }

    /**
     * 根据关健字查找食物
     */
    @GetMapping("/searchFood")
    public Result<Set<FoodSearchVo>> selectBySearch(@RequestParam("keyword") String keyword)
    {
        return foodService.selectBySearch(keyword);
    }

    /**
     * 推荐食物
     */
    @PostMapping("/recommend")
    public Result<PageResult> recommendFood(@RequestBody FoodRecommendPageDto foodRecommendPageDto)
    {
        return foodService.recommendFood(foodRecommendPageDto);
    }
}
