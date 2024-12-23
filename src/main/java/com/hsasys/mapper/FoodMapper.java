package com.hsasys.mapper;

import com.github.pagehelper.Page;
import com.hsasys.domain.dto.FoodPageDto;
import com.hsasys.domain.vo.FoodVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FoodMapper
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
}
