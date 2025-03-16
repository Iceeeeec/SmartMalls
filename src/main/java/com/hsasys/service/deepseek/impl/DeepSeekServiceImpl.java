package com.hsasys.service.deepseek.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hsasys.context.BaseContext;
import com.hsasys.domain.dto.FoodRecommendPageDto;
import com.hsasys.domain.entity.DeepSeekResult;
import com.hsasys.domain.entity.Advice;
import com.hsasys.domain.vo.FoodVo;
import com.hsasys.mapper.FoodMapper;
import com.hsasys.mapper.UserMapper;
import com.hsasys.result.PageResult;
import com.hsasys.result.Result;
import com.hsasys.service.deepseek.DeepSeekService;
import com.hsasys.service.select.impl.FoodServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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


        if (deepSeekResult.getExitCode() == 0) {
            // 解析JSON输出
            ids = foodMapper.recommendFoodIdsByUserId(foodRecommendPageDto);
            //1.1如果推荐表中存在，则直接返回
            if (ids != null && !ids.isEmpty()) {
                List<FoodVo> foods = foodMapper.selectFoodsByIds(ids);
                int pageNum = ids.getPageNum();
                return Result.success(new PageResult(pageNum, foods));
            }
        }
        else
        {
            //如果调用失败，则按照我的推荐来
            FoodServiceImpl foodService = new FoodServiceImpl();
            foodService.addRecommendFood(userId);
            ids = foodMapper.recommendFoodIdsByUserId(foodRecommendPageDto);
            if (ids != null && !ids.isEmpty()) {
                List<FoodVo> foods = foodMapper.selectFoodsByIds(ids);
                int pageNum = ids.getPageNum();
                return Result.success(new PageResult(pageNum, foods));
            }
        }
        return Result.error("服务器繁忙，请稍后再试!");
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
        advice.setAdvice("暂无推荐哦～");
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

    // 获取资源文件路径
    private String getPythonScriptPath(String scriptName) {
        URL url = getClass().getClassLoader().getResource("static/" + scriptName);
        if (url == null) {
            throw new RuntimeException("Resource not found: " + scriptName);
        }
        try {
            // 使用 URI 正确解析路径，避免 URL 前缀干扰
            Path path = Paths.get(url.toURI());
            // 处理 Windows 路径前导斜杠问题
            if (path.toString().startsWith("/")) {
                return path.toString().substring(1);
            }
            return path.toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid URI for resource: " + scriptName, e);
        }
    }

    private DeepSeekResult runPythonScript(String scriptName, Long userId)
    {
        //如果没有则需要调用大模型api
        try {
            // 获取 Python 脚本的路径
            String scriptPath = getPythonScriptPath(scriptName);
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "python",  // 或者 "python" 取决于您的环境
                    scriptPath,
                    String.valueOf(userId)
            );
            Process process = processBuilder.start();
            // 读取Python脚本的输出
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }
            int exitCode = process.waitFor();

            return DeepSeekResult.builder()
                    .exitCode(exitCode)
                    .output(output.toString())
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
