package com.hsasys.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodPageDto
{
    private Integer page;

    private Integer pageSize;

    private Integer typeId;//类型id

    private String search;//搜索内容
}
