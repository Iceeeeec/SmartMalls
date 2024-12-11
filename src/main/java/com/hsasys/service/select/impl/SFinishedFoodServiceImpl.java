package com.hsasys.service.select.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hsasys.result.Result;
import com.hsasys.dao.domain_mapper.*;
import com.hsasys.dao.rela_mapper.*;
import com.hsasys.domain.*;
import com.hsasys.domain.rela.*;

import com.hsasys.domain.vo.FinishedFoodVo;
import com.hsasys.service.select.SFinishedFoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SFinishedFoodServiceImpl implements SFinishedFoodService {
    @Autowired
    private FinishedFoodMapper finishedFoodMapper;
    @Autowired
    private FoodFTypeMapper foodFTypeMapper;
    @Autowired
    private FoodTypeMapper foodTypeMapper;


    @Override
    public Result selectAllFoods() {
        LambdaQueryWrapper<FinishedFood> wrapper = new LambdaQueryWrapper<>();
        wrapper.select();
        List<FinishedFood> finishedFoods = finishedFoodMapper.selectList(wrapper);
        if(finishedFoods == null)
            return Result.error( "没有食物" );
        List<FinishedFoodVo> finishedFoodVos = new ArrayList<>();
        for(FinishedFood finishedFood:finishedFoods){
            FinishedFoodVo finishedFoodVo = new FinishedFoodVo(finishedFood);
            LambdaQueryWrapper<FoodFType> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(FoodFType::getFoodId,finishedFood.getId());
            FoodFType foodFType = foodFTypeMapper.selectOne(wrapper1);
            if(foodFType == null)
                return Result.error( "没有食物类型" );

            LambdaQueryWrapper<FoodType> wrapper2 = new LambdaQueryWrapper<>();
            wrapper2.eq(FoodType::getId, foodFType.getTypeId());
            FoodType foodType = foodTypeMapper.selectOne(wrapper2);
            if(foodType == null)
                return Result.error( "没有食物类型"  );

            finishedFoodVo.setType(foodType.getType());
            finishedFoodVos.add(finishedFoodVo);
        }


        return Result.success(finishedFoodVos);
    }


    @Override
    public Result selectOne(String name) {
        LambdaQueryWrapper<FinishedFood> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(FinishedFood::getName,name);
        FinishedFood food=finishedFoodMapper.selectOne( wrapper );
        List<Map<String,Object>> mapList=new ArrayList<>();
        double protein= food.getProtein();
        double fat=food.getFat();
        double carbohydrate=food.getCarbohydrate();
        double heat=food.getHeat();
        Map<String,Object> map=new HashMap<>();
        map.put( "name","protein" );
        map.put( "value",protein );
        mapList.add( map );
        Map<String,Object> map1=new HashMap<>();
        map1.put( "name","fat" );
        map1.put( "value",fat );
        mapList.add( map1 );
        Map<String,Object> map2=new HashMap<>();
        map2.put( "name","carbohydrate" );
        map2.put( "value",carbohydrate );
        mapList.add( map2 );
        Map<String,Object> map3=new HashMap<>();
        map3.put( "name","heat" );
        map3.put( "value",heat );
        mapList.add( map3 );
        return Result.success(mapList);
    }

    @Override
    public Result selectById(Integer id) {
        LambdaQueryWrapper<FinishedFood> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq( FinishedFood::getId,id );
        FinishedFood food=finishedFoodMapper.selectOne(wrapper);
        return Result.success(food);
    }

}
