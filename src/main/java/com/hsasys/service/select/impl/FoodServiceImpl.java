package com.hsasys.service.select.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hsasys.domain.entity.Food;
import com.hsasys.domain.entity.FoodType;
import com.hsasys.domain.entity.Nutrition;
import com.hsasys.domain.entity.NutritionComponent;
import com.hsasys.domain.vo.FoodDetailVo;
import com.hsasys.exception.FoodNotFoundException;
import com.hsasys.mapper.FoodMapper;
import com.hsasys.domain.dto.FoodPageDto;
import com.hsasys.domain.vo.FoodVo;
import com.hsasys.result.PageResult;
import com.hsasys.result.Result;
import com.hsasys.service.select.FoodService;
import com.hsasys.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodServiceImpl implements FoodService
{

    @Autowired
    private FoodMapper foodMapper;


    /**
     * 根据条件查询
     * @param foodPageDto
     * @return
     */
    @Override
    public Result<PageResult> selectFoodsByQuery(FoodPageDto foodPageDto)
    {
        //先根据分页来查食物
        PageHelper.startPage(foodPageDto.getPage(), foodPageDto.getPageSize());
        //查找要查询的食物
        Page<Integer> foodIds = foodMapper.selectFoodsByQuery(foodPageDto);
        if(foodIds.isEmpty())
        {
            return Result.success(new PageResult(0, null));
        }
        //总页数
        int pageNum = foodIds.getPageNum();
        //根据ids查询营养成分
        List<Integer> ids = foodIds.getResult();
        List<FoodVo> foods = foodMapper.selectFoodsByIds(ids);

        return Result.success(new PageResult(pageNum, foods));
    }

    @Override
    public Result<FoodDetailVo> selectFoodById(Integer id)
    {
        //根据id查询食物
        LambdaQueryWrapper<Food> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Food::getId, id);
        Food food = foodMapper.selectOne(wrapper);
        if(food == null)
        {
            throw new FoodNotFoundException("未找到该食物");
        }
        FoodDetailVo foodDetail = BeanCopyUtils.copyBean(food, FoodDetailVo.class);
        //查询食物所属类型
        FoodType foodType = foodMapper.selectTypeByFoodId(id);
        foodDetail.setFoodType(foodType);
        //查询所有的营养成分
        List<NutritionComponent> nutritionComponents = foodMapper.selectFoodById(id);
        foodDetail.setNutritionComponents(nutritionComponents);
        return Result.success(foodDetail);
    }

    @Override
    public Result selectNutrion(List<Integer> ids) {
        if(ids == null || ids.isEmpty()) {
            return Result.error("食品ID列表不能为空");
        }
        List<Nutrition> nutritionVos = foodMapper.selectNutrionByIds(ids);

        return Result.success(nutritionVos);
    }


}
