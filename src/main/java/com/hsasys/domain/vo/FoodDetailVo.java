package com.hsasys.domain.vo;

import com.hsasys.domain.entity.FoodType;
import com.hsasys.domain.entity.NutritionComponent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodDetailVo
{
    private Integer id;

    private String foodName;

    private String description;

    private String picture;

    private FoodType foodType;//食物所属种类

    private List<NutritionComponent> nutritionComponents;//营养成分
}
