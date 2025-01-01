package com.hsasys.service.select;

import com.hsasys.domain.dto.FoodPageDto;
import com.hsasys.domain.vo.FoodDetailVo;
import com.hsasys.result.PageResult;
import com.hsasys.result.Result;

import java.util.List;

public interface FoodService
{


    /**
     * 根据条件查询食物
     * @param foodPageDto
     * @return
     */
    Result<PageResult> selectFoodsByQuery(FoodPageDto foodPageDto);

    Result<FoodDetailVo> selectFoodById(Integer id);

    Result selectNutrion(List<Integer> ids);
}
