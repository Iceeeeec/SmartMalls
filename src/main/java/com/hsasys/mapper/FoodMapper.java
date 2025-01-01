package com.hsasys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import com.hsasys.domain.dto.FoodPageDto;
import com.hsasys.domain.entity.Food;
import com.hsasys.domain.entity.FoodType;
import com.hsasys.domain.entity.Nutrition;
import com.hsasys.domain.entity.NutritionComponent;
import com.hsasys.domain.vo.FoodSearchVo;
import com.hsasys.domain.vo.FoodVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface FoodMapper extends BaseMapper<Food>
{

    /**
     * 根据条件查询食物ids
     * @param foodPageDto
     * @return
     */
    Page<Integer> selectFoodsByQuery(FoodPageDto foodPageDto);

    /**
     * 根据ids查询食物的营养成分
     * @param ids
     * @return
     */
    List<FoodVo> selectFoodsByIds(@Param("ids") List<Integer> ids);

    FoodType selectTypeByFoodId(Integer id);

    List<NutritionComponent> selectFoodById(Integer id);

    List<FoodVo> selectNutritionByIds(@Param("ids") List<Integer> ids);

    /**
     * 根据关键词查找食物
     * @param keyword
     * @return
     */
    Set<FoodSearchVo> selectBySearch(String keyword);
}
