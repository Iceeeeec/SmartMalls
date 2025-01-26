package com.hsasys.domain.rela;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhysicalItemNut
{
    private Long id;

    private Integer itemId;

    private Integer nutId;

    private Integer status;

    private Integer recommendation;
}
