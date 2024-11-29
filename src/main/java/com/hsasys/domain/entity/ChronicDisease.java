package com.hsasys.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 慢性疾病
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChronicDisease
{
    private Integer id;

    private String chronicDiseaseName;
}
