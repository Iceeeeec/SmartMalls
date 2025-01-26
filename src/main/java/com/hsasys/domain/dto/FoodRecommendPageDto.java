package com.hsasys.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodRecommendPageDto
{
    private Integer page;

    private Integer pageSize;

    private Long userId;
}
