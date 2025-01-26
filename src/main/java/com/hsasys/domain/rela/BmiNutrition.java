package com.hsasys.domain.rela;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BmiNutrition
{
    private Long id;

    private Integer bmiId;

    private Integer nutId;

    private Integer recommendation;
}
