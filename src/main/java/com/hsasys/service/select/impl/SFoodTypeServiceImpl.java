package com.hsasys.service.select.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hsasys.dao.domain_mapper.FinishedFoodMapper;
import com.hsasys.dao.domain_mapper.FoodTypeMapper;
import com.hsasys.dao.rela_mapper.FoodFTypeMapper;
import com.hsasys.domain.FinishedFood;
import com.hsasys.domain.FoodType;
import com.hsasys.domain.rela.FoodFType;
import com.hsasys.domain.vo.FoodTypeVo;
import com.hsasys.result.Result;
import com.hsasys.service.select.SFoodTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SFoodTypeServiceImpl implements SFoodTypeService {
    @Autowired
    private FoodTypeMapper foodTypeMapper;
    @Autowired
    private FoodFTypeMapper foodFTypeMapper;
    @Autowired
    private FinishedFoodMapper finishedFoodMapper;


    @Override
    public Result selectAllFoodType()
    {
        LambdaQueryWrapper<FoodType> wrapper = new LambdaQueryWrapper<>();
        List<FoodType> foodTypes = foodTypeMapper.selectList(wrapper);
        if(foodTypes == null)
            return Result.error( "查询失败");

        List<FoodTypeVo> foodTypeVos = new ArrayList<>();

        for(FoodType foodType:foodTypes){
            FoodTypeVo vo = new FoodTypeVo(foodType);
            LambdaQueryWrapper<FoodFType> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(FoodFType::getTypeId,foodType.getId());
            List<FoodFType> foodFTypes = foodFTypeMapper.selectList(wrapper1);
            if(foodFTypes == null)
                return Result.error( "查询失败");

            List<FinishedFood> finishedFoods = new ArrayList<>();
            for(FoodFType foodFType:foodFTypes){
                LambdaQueryWrapper<FinishedFood> wrapper2 = new LambdaQueryWrapper<>();
                wrapper2.eq(FinishedFood::getId,foodFType.getFoodId());
                FinishedFood finishedFood = finishedFoodMapper.selectOne(wrapper2);
                if(finishedFood == null)
                    return Result.error( "查询失败") ;
                finishedFoods.add(finishedFood);
            }
            vo.setFinishedFood(finishedFoods);
            foodTypeVos.add(vo);
        }

        return Result.success(foodTypeVos);
    }
}
