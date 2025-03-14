package com.hsasys.service.deepseek;

import com.hsasys.domain.dto.FoodRecommendPageDto;
import com.hsasys.domain.entity.Advice;
import com.hsasys.domain.entity.DeepSeekResult;
import com.hsasys.result.PageResult;
import com.hsasys.result.Result;

public interface DeepSeekService
{
    /**
     * 根据用户喜好推荐菜品
     * @param foodRecommendPageDto
     * @return
     */
    Result<PageResult> recommendFoodByDeepSeek(FoodRecommendPageDto foodRecommendPageDto);

    /**
     * 根据用户推荐饮食建议
     * @return
     */
    Result<Advice> recommendAdviceByDeepSeek();

    /**
     * 根据用户喜好推荐饮食建议
     * @param userId
     * @return
     */
    Result<Advice> recommendAdviceByDeepSeek(Long userId);

    /**
     * 根据大模型计算健康指数
     * @param userId
     * @return
     */
    DeepSeekResult calculateScoreByDeepSeek(Long userId);
}

