package com.hsasys.controller.deepseek;

import com.hsasys.domain.dto.FoodRecommendPageDto;
import com.hsasys.domain.entity.Advice;
import com.hsasys.result.PageResult;
import com.hsasys.result.Result;
import com.hsasys.service.deepseek.DeepSeekService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/deepseek")
public class DeepSeekApiController
{

    @Autowired
    private DeepSeekService deepSeekService;

    /**
     * 大模型推荐食品
     * @param foodRecommendPageDto
     * @return
     */
    @PostMapping("/recommendFood")
    Result<PageResult> recommendFoodByDeepSeek(@RequestBody FoodRecommendPageDto foodRecommendPageDto)
    {
        return deepSeekService.recommendFoodByDeepSeek(foodRecommendPageDto);
    }

    /**
     * 大模型饮食推荐
     * @return
     */
    @GetMapping("/advice")
    Result<Advice> recommendAdviceByDeepSeek()
    {
        return deepSeekService.recommendAdviceByDeepSeek();
    }

    /**
     * 大模型饮食推荐
     */
    @GetMapping("/advice/{id}")
    Result<Advice> recommendAdviceByDeepSeek(@PathVariable Long id)
    {
        return deepSeekService.recommendAdviceByDeepSeek(id);
    }
}
