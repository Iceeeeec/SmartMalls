package com.hsasys.dao.domain_mapper;

import com.github.pagehelper.Page;
import com.hsasys.domain.dto.FoodPageDto;
import com.hsasys.domain.vo.FoodVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FoodMapper
{

    /**
     * 查询所有食物
     * @return
     */
    Page<FoodVo> pageQuery(@Param("offset") int offset,@Param("pageSize") int pageSize);
}
