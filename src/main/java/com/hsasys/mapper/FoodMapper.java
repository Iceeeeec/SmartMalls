package com.hsasys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import com.hsasys.domain.dto.FoodPageDto;
import com.hsasys.domain.dto.FoodRecommendPageDto;
import com.hsasys.domain.entity.*;
import com.hsasys.domain.rela.FoodNutrition;
import com.hsasys.domain.vo.FoodSearchVo;
import com.hsasys.domain.vo.FoodVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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

    /**
     * 根据用户id推荐食物
     * @param foodRecommendPageDto
     * @return
     */
    @Select("select food_id from user_food where user_id = #{userId} and score >= 0 order by score desc, food_id")
    Page<Integer> recommendFoodIdsByUserId(FoodRecommendPageDto foodRecommendPageDto);

    List<FoodNutrition> selectFoodIdsByNutIds(@Param("nutIds") List<Integer> nutIds);

    /**
     * 插入推荐食物列表
     * @param recommendFoodList
     */
    void insertRecommendFoodList(@Param("recommendFoodList") List<RecommendFood> recommendFoodList);

    @Select("select food_id from food_nut where nut_id = 2 and status = 0")
    Page<Integer> selectFoodIdsByCustom(FoodRecommendPageDto foodRecommendPageDto);

    @Delete("delete from user_food where user_id = #{userId}")
    void deleteRecommendFood(@Param("userId") Integer userId);

    @Select("select id from foods where food_name = #{foodName}")
    Integer selectFoodByName(String foodName);
}
