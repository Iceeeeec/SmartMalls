package com.hsasys.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Nutrition
{
    private Integer id;

    private String foodName;

    private String picture;

    private List<NutritionComponent> nutritionComponents;//营养成分
}