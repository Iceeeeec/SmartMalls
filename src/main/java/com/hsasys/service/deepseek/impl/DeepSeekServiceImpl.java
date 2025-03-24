package com.hsasys.service.deepseek.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hsasys.context.BaseContext;
import com.hsasys.domain.dto.FoodRecommendPageDto;
import com.hsasys.domain.entity.DeepSeekResult;
import com.hsasys.domain.entity.Advice;
import com.hsasys.domain.entity.RecommendFood;
import com.hsasys.domain.vo.FoodVo;
import com.hsasys.exception.RecommendationFailedException;
import com.hsasys.mapper.FoodMapper;
import com.hsasys.mapper.UserMapper;
import com.hsasys.result.PageResult;
import com.hsasys.result.Result;
import com.hsasys.service.deepseek.DeepSeekService;
import com.hsasys.service.select.impl.FoodServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class DeepSeekServiceImpl implements DeepSeekService {

    @Autowired
    private FoodMapper foodMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 调用python脚本推荐食物
     * @param foodRecommendPageDto
     * @return
     */
    @Override
    @Transactional
    public Result<PageResult> recommendFoodByDeepSeek(FoodRecommendPageDto foodRecommendPageDto) {
        Long userId = foodRecommendPageDto.getUserId();
        PageHelper.startPage(foodRecommendPageDto.getPage(), foodRecommendPageDto.getPageSize());
        //1.先从推荐表中拿取
        Page<Integer> ids = foodMapper.recommendFoodIdsByUserId(foodRecommendPageDto);
        System.out.println("返回的id：" + ids);
        //1.1如果推荐表中存在，则直接返回
        if (ids != null && !ids.isEmpty()) {
            List<FoodVo> foods = foodMapper.selectFoodsByIds(ids);
            int pageNum = ids.getPageNum();
            return Result.success(new PageResult(pageNum, foods));
        }
        //如果没有则需要调用大模型api
        DeepSeekResult deepSeekResult = runPythonScript("DeepSeek-api.py", userId);

        if (deepSeekResult.getExitCode() == 0)
        {
            //先删除推荐表的数据
            foodMapper.deleteRecommendFood(userId.intValue());
            //处理返回的数据
            String foodScoreJson = deepSeekResult.getOutput();
            // 解析 JSON
            ObjectMapper mapper = new ObjectMapper();
            try {
                Map<String, Integer> foodScoreMap = mapper.readValue(foodScoreJson, Map.class);
                //创建推荐食物实体类
                List<RecommendFood> recommendFoods = new ArrayList<>();
                // 处理解析后的数据
                for (Map.Entry<String, Integer> entry : foodScoreMap.entrySet())
                {
                    String food_name = entry.getKey();
                    Integer value = entry.getValue();
                    Integer foodId = foodMapper.selectFoodByName(food_name);
                    if(foodId != null)
                    {
                        recommendFoods.add(new RecommendFood(null, userId, foodId, value.doubleValue()));
                    }
                }
                //批量插入推荐表
                foodMapper.insertRecommendFoodList(recommendFoods);
            } catch (JsonProcessingException e)
            {
                throw new RuntimeException(e);
            }
        }
        else
        {
            //如果调用失败，则按照我的推荐来
            FoodServiceImpl foodService = new FoodServiceImpl();
            foodService.addRecommendFood(userId);
        }
        ids = foodMapper.recommendFoodIdsByUserId(foodRecommendPageDto);
        if(ids == null || ids.isEmpty())
        {
            throw new RecommendationFailedException("网络出小差，请稍后试试");
        }

        List<FoodVo> foods = foodMapper.selectFoodsByIds(ids);
        int pageNum = ids.getPageNum();
        return Result.success(new PageResult(pageNum, foods));
    }

    /**
     * 从线程中获取Id
     * @return
     */
    @Override
    @Transactional
    public Result<Advice> recommendAdviceByDeepSeek()
    {
        Long userId = BaseContext.getCurrentId();
        Advice advice = userMapper.selectAdviceByUserId(userId);
        if (advice != null)
        {
            return Result.success(advice);
        }
        //如果没有，则调用大模型
        DeepSeekResult deepSeekResult = runPythonScript("DeepSeek-api-advice.py", userId);
        if (deepSeekResult.getExitCode() == 0)
        {
            advice = userMapper.selectAdviceByUserId(userId);
            if (advice != null)
            {
                return Result.success(advice);
            }
        }
        advice.setAdvice("暂无推荐哦～");
        return Result.success(advice);
    }

    /*
     * 根据用户id推荐饮食建议
     */
    @Override
    public Result<Advice> recommendAdviceByDeepSeek(Long userId)
    {
        Advice advice = userMapper.selectAdviceByUserId(userId);
        if (advice != null)
        {
            return Result.success(advice);
        }
        //如果没有，则调用大模型
        DeepSeekResult deepSeekResult = runPythonScript("DeepSeek-api-advice.py", userId);
        if (deepSeekResult.getExitCode() == 0)
        {
            advice = userMapper.selectAdviceByUserId(userId);
            if (advice != null)
            {
                return Result.success(advice);
            }
        }
        advice = Advice.builder()
                .advice("暂无推荐哦～").build();
        return Result.success(advice);
    }

    /**
     * 根据大模型计算得分
     * @param userId
     * @return
     */
    @Override
    public DeepSeekResult calculateScoreByDeepSeek(Long userId)
    {
        return runPythonScript("DeepSeek-api-score.py", userId);
    }

    // 获取资源文件流
    private InputStream getPythonScriptAsStream(String scriptName) {
        InputStream resourceStream = getClass().getClassLoader().getResourceAsStream("static/" + scriptName);
        if (resourceStream == null) {
            throw new RuntimeException("Resource not found: " + scriptName);
        }
        return resourceStream;
    }

    private DeepSeekResult runPythonScript(String scriptName, Long userId)
    {
        try {
            // 获取资源文件流
            InputStream scriptInputStream = getPythonScriptAsStream(scriptName);

            // 构建 ProcessBuilder，使用 Python 解释器
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "python3",  // 或者 "python"，根据环境配置
                    "-",// 从标准输入读取脚本内容
                    String.valueOf(userId)
            );

            Process process = processBuilder.start();

            // 将脚本内容通过输入流写入 Python 解释器的标准输入
            try (BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(process.getOutputStream()));
                 BufferedReader scriptReader = new BufferedReader(
                         new InputStreamReader(scriptInputStream))) {

                String line;
                while ((line = scriptReader.readLine()) != null) {
                    writer.write(line);
                    writer.newLine();
                }
                writer.flush();
            }

            // 读取 Python 脚本的输出
            BufferedReader outputReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            StringBuilder output = new StringBuilder();
            String line;
            while ((line = outputReader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // 等待进程结束并获取退出码
            int exitCode = process.waitFor();

            // 构建结果对象
            return DeepSeekResult.builder()
                    .exitCode(exitCode)
                    .output(output.toString())
                    .build();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to execute Python script.", e);
        }
    }
}
