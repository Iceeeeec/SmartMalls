package com.hsasys.domain.vo;

import com.hsasys.domain.entity.PhysicalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhysicalItemVo
{
    private Integer id;

    private String physicalItemName;

    private String unit;

    private PhysicalType physicalType;
}
