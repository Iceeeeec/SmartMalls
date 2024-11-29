package com.hsasys.service.select.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hsasys.result.Result;
import com.hsasys.dao.domain_mapper.*;
import com.hsasys.dao.rela_mapper.*;
import com.hsasys.domain.*;
import com.hsasys.domain.rela.*;
import com.hsasys.domain.vo.FinishedFoodAttributeVo;
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
    private FoodFAdditiveMapper foodFAdditiveMapper;
    @Autowired
    private FoodAdditiveMapper foodAdditiveMapper;
    @Autowired
    private AddedSugarMapper addedSugarMapper;
    @Autowired
    private FoodAddedSugarMapper foodAddedSugarMapper;
    @Autowired
    private TransFattyAcidMapper transFattyAcidMapper;
    @Autowired
    private FoodTransFattyAcidMapper foodTransFattyAcidMapper;
    @Autowired
    private FoodFLabelMapper foodFLabelMapper;
    @Autowired
    private FoodLabelMapper foodLabelMapper;
    @Autowired
    private FoodFTypeMapper foodFTypeMapper;
    @Autowired
    private FoodTypeMapper foodTypeMapper;

//    private final Result err_result = new Result(Code.ERR,null);

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

//        for(FinishedFood finishedFood:finishedFoods){
//            // foodAddictive
//            LambdaQueryWrapper<FoodFAdditive> wrapper1 = new LambdaQueryWrapper<>();
//            wrapper1.eq(FoodFAdditive::getFood_id, finishedFood.getId());
//            List<FoodFAdditive> foodFAdditives = foodFAdditiveMapper.selectList(wrapper1);
//            List<FoodAdditive> foodAdditives = new ArrayList<>();
//            for(FoodFAdditive foodFAdditive:foodFAdditives){
//                LambdaQueryWrapper<FoodAdditive> wrapper2 = new LambdaQueryWrapper<>();
//                wrapper2.select();
//            }
//        }
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
    public Result selectOneFood(String id) {
        LambdaQueryWrapper<FinishedFood> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FinishedFood::getId, id);
        FinishedFood food = finishedFoodMapper.selectOne(wrapper);
        if(food == null)
            return Result.error( "未找到该食物" );

        FinishedFoodAttributeVo finishedFood = new FinishedFoodAttributeVo(food);

        System.out.println(finishedFood+"\n\n");

        // foodAddictive
        LambdaQueryWrapper<FoodFAdditive> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(FoodFAdditive::getFoodId, finishedFood.getId());
//        System.out.println(foodFAdditive+"\n\n");
        List<FoodFAdditive> foodFAdditives = foodFAdditiveMapper.selectList(wrapper1);
        System.out.println(foodFAdditives+"\n\n");
        if(foodFAdditives != null){
            List<FoodAdditive> foodAdditives = new ArrayList<>();
            for(FoodFAdditive foodFAdditive:foodFAdditives){
                LambdaQueryWrapper<FoodAdditive> wrapper2 = new LambdaQueryWrapper<>();
                wrapper2.eq(FoodAdditive::getId, foodFAdditive.getAddictiveId());
                FoodAdditive foodAdditive = foodAdditiveMapper.selectOne(wrapper2);
                foodAdditives.add(foodAdditive);
            }
            finishedFood.setFoodAdditives(foodAdditives);
        }

        // addedSugar
        LambdaQueryWrapper<FoodAddedSugar> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(FoodAddedSugar::getFoodId, finishedFood.getId());
        List<FoodAddedSugar> foodAddedSugars = foodAddedSugarMapper.selectList(wrapper2);
        System.out.println(foodAddedSugars+"\n\n");
        if(foodAddedSugars != null){
            List<AddedSugar> addedSugars = new ArrayList<>();
            for(FoodAddedSugar foodAddedSugar:foodAddedSugars){
                LambdaQueryWrapper<AddedSugar> wrapper3 = new LambdaQueryWrapper<>();
                wrapper3.eq(AddedSugar::getId, foodAddedSugar.getSugarId());
                AddedSugar addedSugar = addedSugarMapper.selectOne(wrapper3);
                addedSugars.add(addedSugar);
            }
            finishedFood.setAddedSugars(addedSugars);
        }

        // transFattyAcid
        LambdaQueryWrapper<FoodTransFattyAcid> wrapper3 = new LambdaQueryWrapper<>();
        wrapper3.eq(FoodTransFattyAcid::getFoodId, finishedFood.getId());
        List<FoodTransFattyAcid> foodTransFattyAcids = foodTransFattyAcidMapper.selectList(wrapper3);
        System.out.println(foodTransFattyAcids+"\n\n");
        if(foodTransFattyAcids != null){
            List<TransFattyAcid> transFattyAcids = new ArrayList<>();
            for(FoodTransFattyAcid foodTransFattyAcid:foodTransFattyAcids){
                LambdaQueryWrapper<TransFattyAcid> wrapper4 = new LambdaQueryWrapper<>();
                wrapper4.eq(TransFattyAcid::getId, foodTransFattyAcid.getTransFattyAcidId());
                TransFattyAcid transFattyAcid = transFattyAcidMapper.selectOne(wrapper4);
                transFattyAcids.add(transFattyAcid);
            }
            finishedFood.setTransFattyAcids(transFattyAcids);
        }

        // foodLabel
        LambdaQueryWrapper<FoodFLabel> wrapper4 = new LambdaQueryWrapper<>();
        wrapper4.eq(FoodFLabel::getId, finishedFood.getId());
        List<FoodFLabel> foodFLabels = foodFLabelMapper.selectList(wrapper4);
        if(foodFLabels != null){
            List<FoodLabel> foodLabels = new ArrayList<>();
            for(FoodFLabel foodFLabel:foodFLabels){
                LambdaQueryWrapper<FoodLabel> wrapper5 = new LambdaQueryWrapper<>();
                wrapper5.eq(FoodLabel::getId, foodFLabel.getLabelId());
                FoodLabel foodLabel = foodLabelMapper.selectOne(wrapper5);
                foodLabels.add(foodLabel);
            }
            finishedFood.setFoodLabels(foodLabels);
        }

        // foodType
        LambdaQueryWrapper<FoodFType> wrapper5 = new LambdaQueryWrapper<>();
        wrapper5.eq(FoodFType::getId, finishedFood.getId());
        List<FoodFType> foodFTypes = foodFTypeMapper.selectList(wrapper5);
//        System.out.println(foodFTypes+"\n\n");
        if(foodFTypes != null){
            List<FoodType> foodTypes = new ArrayList<>();
            for(FoodFType foodFType:foodFTypes){
                LambdaQueryWrapper<FoodType> wrapper6 = new LambdaQueryWrapper<>();
                wrapper6.eq(FoodType::getId, foodFType.getTypeId());
                FoodType foodType = foodTypeMapper.selectOne(wrapper6);
                foodTypes.add(foodType);
            }
            finishedFood.setFoodTypes(foodTypes);
        }

        return Result.success(finishedFood);
    }
}
