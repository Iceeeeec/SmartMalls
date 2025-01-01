package com.hsasys.service.select;

import com.hsasys.domain.dto.FoodPageDto;
import com.hsasys.domain.entity.Nutrition;
import com.hsasys.domain.vo.FoodDetailVo;
import com.hsasys.domain.vo.FoodSearchVo;
import com.hsasys.domain.vo.FoodVo;
import com.hsasys.result.PageResult;
import com.hsasys.result.Result;

import java.util.List;
import java.util.Set;

public interface FoodService
{


    /**
     * 根据条件查询食物
     * @param foodPageDto
     * @return
     */
    Result<PageResult> selectFoodsByQuery(FoodPageDto foodPageDto);

    /**
     * 根据id查询食品营养成分
     * @param id
     * @return
     */
    Result<FoodDetailVo> selectFoodById(Integer id);

    /**
     * 根据id列表查询营养成分
     * @param ids
     * @return
     */
    Result<List<FoodVo>> selectNutrition(List<Integer> ids);

    /**
     * 根据关键词查询食物
     * @param keyword
     * @return
     */
    Result<Set<FoodSearchVo>> selectBySearch(String keyword);
}
