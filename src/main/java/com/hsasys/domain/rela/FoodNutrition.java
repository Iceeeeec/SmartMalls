package com.hsasys.domain.rela;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodNutrition
{
    private Long id;

    private Integer foodId;

    private Integer nutId;

    private Integer status;
}
