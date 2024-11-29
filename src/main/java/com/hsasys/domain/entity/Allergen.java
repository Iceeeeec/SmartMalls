package com.hsasys.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;




/**
 * 过敏源
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Allergen
{
    private Integer id;

    private String allergenName;
}
