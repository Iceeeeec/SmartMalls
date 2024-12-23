package com.hsasys.service.select.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hsasys.dao.domain_mapper.FoodMapper;
import com.hsasys.domain.dto.FoodPageDto;
import com.hsasys.domain.vo.FoodVo;
import com.hsasys.result.PageResult;
import com.hsasys.result.Result;
import com.hsasys.service.select.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FoodServiceImpl implements FoodService
{

    @Autowired
    private FoodMapper foodMapper;

    /**
     * 分页查询所有食物
     * @param foodPageDto
     * @return
     */
    @Override
    public Result<PageResult> pageQuery(FoodPageDto foodPageDto)
    {
        int offset = (foodPageDto.getPage() - 1) * foodPageDto.getPageSize();
        int pageSize = foodPageDto.getPageSize();
        Page<FoodVo> page = foodMapper.pageQuery(offset, pageSize);
        PageResult pageResult = new PageResult(page.getTotal(), page.getResult());
        return Result.success(pageResult);
    }
}
