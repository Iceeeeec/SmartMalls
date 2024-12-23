package com.hsasys.domain.vo;

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
public class FoodVo
{
    private Integer foodId;

    private String foodName;

    private String description;

    private String picture;

    private List<NutritionComponent> nutritionComponents;
}
