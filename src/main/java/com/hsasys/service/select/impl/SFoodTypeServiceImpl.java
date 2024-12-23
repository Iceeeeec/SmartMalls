package com.hsasys.service.select.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hsasys.mapper.FoodTypeMapper;
import com.hsasys.domain.entity.FoodType;
import com.hsasys.result.Result;
import com.hsasys.service.select.SFoodTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SFoodTypeServiceImpl implements SFoodTypeService {
    @Autowired
    private FoodTypeMapper foodTypeMapper;

    @Override
    public Result selectAllFoodType()
    {
        LambdaQueryWrapper<FoodType> wrapper = new LambdaQueryWrapper<>();
        List<FoodType> foodTypes = foodTypeMapper.selectList(wrapper);
        return Result.success(foodTypes);
    }
}
