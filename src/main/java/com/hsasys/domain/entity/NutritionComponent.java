package com.hsasys.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NutritionComponent
{
    private String nameEn;

    private String nameCn;

    private String content;

    private Integer status;
}
