package com.hsasys.service.select.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.hsasys.dao.domain_mapper.AddedSugarMapper;
import com.hsasys.dao.domain_mapper.FoodAdditiveMapper;
import com.hsasys.dao.domain_mapper.TransFattyAcidMapper;
import com.hsasys.domain.AddedSugar;
import com.hsasys.domain.FoodAdditive;
import com.hsasys.domain.TransFattyAcid;
import com.hsasys.domain.vo.IngredientVo;
import com.hsasys.result.Result;
import com.hsasys.service.select.SIngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SIngredientServiceImpl implements SIngredientService {
    @Autowired
    private FoodAdditiveMapper foodAdditiveMapper;
    @Autowired
    private AddedSugarMapper addedSugarMapper;
    @Autowired
    private TransFattyAcidMapper transFattyAcidMapper;


    @Override
    public Result selectAllIngredient() {
        IngredientVo ingredientVo = new IngredientVo();

        // foodAdditive
        LambdaQueryWrapper<FoodAdditive> wrapper = new LambdaQueryWrapper<>();
        List<FoodAdditive> foodAdditives = foodAdditiveMapper.selectList(wrapper);
        if(foodAdditives == null)
            return Result.error( "查询失败" );
        ingredientVo.setFoodAdditives(foodAdditives);

        // addedSugar
        LambdaQueryWrapper<AddedSugar> wrapper1 = new LambdaQueryWrapper<>();
        List<AddedSugar> addedSugars = addedSugarMapper.selectList(wrapper1);
        if(addedSugars == null)
            return Result.error( "查询失败" );
        ingredientVo.setAddedSugars(addedSugars);

        // transFattyAcid
        LambdaQueryWrapper<TransFattyAcid> wrapper2 = new LambdaQueryWrapper<>();
        List<TransFattyAcid> transFattyAcids = transFattyAcidMapper.selectList(wrapper2);
        if(transFattyAcids == null)
            return Result.error( "查询失败" );
        ingredientVo.setTransFattyAcids(transFattyAcids);

        return Result.success(ingredientVo);
    }
}
