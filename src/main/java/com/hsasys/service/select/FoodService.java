package com.hsasys.service.select;

import com.hsasys.domain.dto.FoodPageDto;
import com.hsasys.result.PageResult;
import com.hsasys.result.Result;

public interface FoodService
{


    /**
     * 根据条件查询食物
     * @param foodPageDto
     * @return
     */
    Result<PageResult> selectFoodsByQuery(FoodPageDto foodPageDto);
}
