package com.hsasys.service.select.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hsasys.context.BaseContext;
import com.hsasys.domain.dto.FoodRecommendPageDto;
import com.hsasys.domain.entity.*;
import com.hsasys.domain.rela.BmiNutrition;
import com.hsasys.domain.rela.FoodNutrition;
import com.hsasys.domain.rela.PhysicalItemNut;
import com.hsasys.domain.vo.FoodDetailVo;
import com.hsasys.domain.vo.FoodSearchVo;
import com.hsasys.exception.FoodNotFoundException;
import com.hsasys.mapper.*;
import com.hsasys.domain.dto.FoodPageDto;
import com.hsasys.domain.vo.FoodVo;
import com.hsasys.result.PageResult;
import com.hsasys.result.Result;
import com.hsasys.service.select.FoodService;
import com.hsasys.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FoodServiceImpl implements FoodService
{

    @Autowired
    private FoodMapper foodMapper;


    @Autowired
    private AllergenMapper allergenMapper;

    @Autowired
    private ChronicDiseaseMapper diseaseMapper;

    @Autowired
    private PhysicalResultMapper physicalResultMapper;

    @Autowired
    private PhysicalMapper physicalMapper;

    @Autowired
    private UserUTypeMapper userUTypeMapper;
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
            //TODO: 抛出异常,定义常量
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
    public Result<List<FoodVo>> selectNutrition(List<Integer> ids)
    {
        if(ids == null || ids.isEmpty())
        {
            return Result.error("食品ID列表不能为空");
        }
        List<FoodVo> nutritionVos = foodMapper.selectNutritionByIds(ids);

        return Result.success(nutritionVos);
    }

    /**
     * 根据关键词搜索食物
     * @param keyword
     * @return
     */
    @Override
    public Result<Set<FoodSearchVo>> selectBySearch(String keyword)
    {
        Set<FoodSearchVo> foods = foodMapper.selectBySearch(keyword);
        return Result.success(foods);
    }

    /**
     * 推荐食物
     * @return
     */
    @Override
    @Transactional
    public Result<PageResult> recommendFood(FoodRecommendPageDto foodRecommendPageDto)
    {
        Long userId = BaseContext.getCurrentId();
        foodRecommendPageDto.setUserId(userId);
        PageHelper.startPage(foodRecommendPageDto.getPage(), foodRecommendPageDto.getPageSize());
        //1.先从推荐表中拿取
        Page<Integer> ids = foodMapper.recommendFoodIdsByUserId(foodRecommendPageDto);
        //1.1如果推荐表中存在，则直接返回
        if(ids != null && !ids.isEmpty())
        {
            List<FoodVo> foods = foodMapper.selectFoodsByIds(ids);
            int pageNum = ids.getPageNum();
            return Result.success(new PageResult(pageNum, foods));
        }
        //2.如果没有，则根据过敏原、慢性疾病、体检结果来推荐
        addRecommendFood(userId);
        ids = foodMapper.recommendFoodIdsByUserId(foodRecommendPageDto);
        if(ids != null && !ids.isEmpty())
        {
            List<FoodVo> foods = foodMapper.selectFoodsByIds(ids);
            int pageNum = ids.getPageNum();
            return Result.success(new PageResult(pageNum, foods));
        }
        //自定义推荐食物，我选择的是推荐低能量的食物
        ids = foodMapper.selectFoodIdsByCustom(foodRecommendPageDto);
        List<FoodVo> foods = foodMapper.selectFoodsByIds(ids);
        int pageNum = ids.getPageNum();
        return Result.success(new PageResult(pageNum, foods));
    }

    /**
     * 添加推荐食物表
     */
    @Transactional
    public void addRecommendFood(Long userId)
    {
        //通过用户id查询包含该过敏原的食物
        List<Integer> allergenFoodIds = allergenMapper.selectFoodIdsByUserId(userId);
        //慢性疾病
        //  查该多吃的营养
        List<Integer> goodNutIds = diseaseMapper.selectNutIdsByUserIdAndStatus(userId, 1);
        //  查该少吃的营养
        List<Integer> badNutIds = diseaseMapper.selectNutIdsByUserIdAndStatus(userId, 0);
        //      根据营养id和status来查询食物
        Map<String, RecommendFood> recommendFoodMap = new HashMap<>();
        if(goodNutIds != null && !goodNutIds.isEmpty())
        {
            //通过查询营养成分 查询到食物的id并赋值，
            List<FoodNutrition> goodFoodNutrition = foodMapper.selectFoodIdsByNutIds(goodNutIds);
            for(FoodNutrition foodNutrition : goodFoodNutrition)
            {
                Double score = (foodNutrition.getStatus() - 1) * 100 * 0.5;
                addOrUpdateRecommend(recommendFoodMap, userId, foodNutrition.getFoodId(), score);
            }
        }
        if(badNutIds != null && !badNutIds.isEmpty())
        {
            //      根据营养id和status来查询食物
            List<FoodNutrition> badFoodNutrition = foodMapper.selectFoodIdsByNutIds(badNutIds);
            for(FoodNutrition foodNutrition : badFoodNutrition)
            {
                //少吃的营养成分，应该推荐status=0的食物，1，2的为负数
                Double score =  - ((foodNutrition.getStatus() - 1) * 100 * 0.5);
                addOrUpdateRecommend(recommendFoodMap, userId, foodNutrition.getFoodId(), score);
            }
        }

        //查用户异常的体检项目id集合
        //项目偏低的
        List<Integer> lowPhysicalItemIds = physicalResultMapper.selectItemIdsByUserIdAndStatus(userId, 0);
        if(lowPhysicalItemIds != null && !lowPhysicalItemIds.isEmpty())
        {
            List<PhysicalItemNut> physicalItemNuts = physicalMapper.selectItemNutByItemIdsAndStatus(lowPhysicalItemIds, 0);
            if(physicalItemNuts != null && !physicalItemNuts.isEmpty())
            {
                //根据recommendation来推荐，0为减少，1为增加,过滤后获取nutIds集合
                //recommendation为1,多吃的集合
                List<Integer> moreEatNutIds = physicalItemNuts.stream()
                        .filter(s -> s.getRecommendation() == 1)
                        .map(PhysicalItemNut::getNutId)
                        .collect(Collectors.toList());
                if(!moreEatNutIds.isEmpty())
                {
                    List<FoodNutrition> goodFoodNutrition = foodMapper.selectFoodIdsByNutIds(moreEatNutIds);
                    for(FoodNutrition foodNutrition : goodFoodNutrition)
                    {
                        Double score = (foodNutrition.getStatus() - 1) * 100 * 0.3;
                        addOrUpdateRecommend(recommendFoodMap, userId, foodNutrition.getFoodId(), score);
                    }
                }

                //少吃的集合
                List<Integer> lessEatNutIds = physicalItemNuts.stream()
                        .filter(s -> s.getRecommendation() == 0)
                        .map(PhysicalItemNut::getNutId)
                        .collect(Collectors.toList());
                if(!lessEatNutIds.isEmpty())
                {
                    List<FoodNutrition> badFoodNutrition = foodMapper.selectFoodIdsByNutIds(lessEatNutIds);
                    for(FoodNutrition foodNutrition : badFoodNutrition)
                    {
                        Double score =  - ((foodNutrition.getStatus() - 1) * 100 * 0.3);
                        addOrUpdateRecommend(recommendFoodMap, userId, foodNutrition.getFoodId(), score);
                    }
                }
            }

        }
        //项目偏高的
        List<Integer> highPhysicalItemIds = physicalResultMapper.selectItemIdsByUserIdAndStatus(userId, 2);
        if(highPhysicalItemIds != null && !highPhysicalItemIds.isEmpty())
        {
            List<PhysicalItemNut> physicalItemNuts = physicalMapper.selectItemNutByItemIdsAndStatus(highPhysicalItemIds, 1);
            if(physicalItemNuts != null && !physicalItemNuts.isEmpty())
            {
                //根据recommendation来推荐，0为减少，1为增加,过滤后获取nutIds集合
                //recommendation为1,多吃的集合
                List<Integer> moreEatNutIds = physicalItemNuts.stream()
                        .filter(s -> s.getRecommendation() == 1)
                        .map(PhysicalItemNut::getNutId)
                        .collect(Collectors.toList());
                List<FoodNutrition> goodFoodNutrition = foodMapper.selectFoodIdsByNutIds(moreEatNutIds);
                for(FoodNutrition foodNutrition : goodFoodNutrition)
                {
                    Double score = (foodNutrition.getStatus() - 1) * 100 * 0.3;
                    addOrUpdateRecommend(recommendFoodMap, userId, foodNutrition.getFoodId(), score);
                }
                //少吃的集合
                List<Integer> lessEatNutIds = physicalItemNuts.stream()
                            .filter(s -> s.getRecommendation() == 0)
                            .map(PhysicalItemNut::getNutId)
                            .collect(Collectors.toList());
                List<FoodNutrition> badFoodNutrition = foodMapper.selectFoodIdsByNutIds(lessEatNutIds);
                for(FoodNutrition foodNutrition : badFoodNutrition)
                {
                    Double score =  - ((foodNutrition.getStatus() - 1) * 100 * 0.3);
                    addOrUpdateRecommend(recommendFoodMap, userId, foodNutrition.getFoodId(), score);
                }
            }
        }
        //bmi处理
        List<BmiNutrition> bmiNuts = userUTypeMapper.selectBmiNutIdsByUserId(userId);
        if(bmiNuts != null && !bmiNuts.isEmpty())
        {
            //根据recommend推荐
            //该多吃的集合
            List<Integer> moreEatNutIds = bmiNuts.stream()
                    .filter(br -> br.getRecommendation() == 1)
                    .map(BmiNutrition::getNutId)
                    .collect(Collectors.toList());
            if(!moreEatNutIds.isEmpty())
            {
                List<FoodNutrition> goodFoodNutrition = foodMapper.selectFoodIdsByNutIds(moreEatNutIds);
                for(FoodNutrition foodNutrition : goodFoodNutrition)
                {
                    Double score = (foodNutrition.getStatus() - 1) * 100 * 0.2;
                    addOrUpdateRecommend(recommendFoodMap, userId, foodNutrition.getFoodId(), score);
                }
            }
            //该少吃的集合
            List<Integer> lessEatNutIds = bmiNuts.stream()
                    .filter(br -> br.getRecommendation() == 0)
                    .map(BmiNutrition::getNutId)
                    .collect(Collectors.toList());
            if(!lessEatNutIds.isEmpty())
            {
                List<FoodNutrition> badFoodNutrition = foodMapper.selectFoodIdsByNutIds(lessEatNutIds);
                for(FoodNutrition foodNutrition : badFoodNutrition)
                {
                    Double score =  - ((foodNutrition.getStatus() - 1) * 100 * 0.2);
                    addOrUpdateRecommend(recommendFoodMap, userId, foodNutrition.getFoodId(), score);
                }
            }
        }

        //过敏原处理
        allergenFoodIds.forEach(foodId ->
        {
            Double score = -10000.0;
            addOrUpdateRecommend(recommendFoodMap, userId, foodId, score);
        });
        List<RecommendFood> recommendFoodList = new ArrayList<>(recommendFoodMap.values());
        foodMapper.insertRecommendFoodList(recommendFoodList);
    }

    // 添加或更新推荐
    public void addOrUpdateRecommend(Map<String, RecommendFood> recommendFoodMap, Long userId, Integer foodId, Double score) {
        String key = generateKey(userId, foodId);
        if(recommendFoodMap.containsKey(key))
        {
            RecommendFood recommendFood = recommendFoodMap.get(key);
            recommendFood.setScore(recommendFood.getScore() + score);
        }
        else
        {
            RecommendFood recommendFood = RecommendFood.builder()
                .foodId(foodId)
                .userId(userId)
                .score(score)
                .build();
            recommendFoodMap.put(key, recommendFood);
        }
    }

    // 生成唯一键
    private String generateKey(Long userId, Integer foodId)
    {
        return userId + "_" + foodId;
    }
}
