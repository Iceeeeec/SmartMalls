package com.hsasys.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 饮食习惯
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodPreference
{
    private Integer id;

    private String preferenceName;
}
