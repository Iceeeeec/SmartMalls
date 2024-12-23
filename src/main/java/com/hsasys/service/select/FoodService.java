package com.hsasys.service.select;

import com.hsasys.domain.dto.FoodPageDto;
import com.hsasys.result.PageResult;
import com.hsasys.result.Result;

public interface FoodService
{

    /**
     * 分页查询所有食物
     * @param foodPageDto
     * @return
     */
    Result<PageResult> pageQuery(FoodPageDto foodPageDto);
}
