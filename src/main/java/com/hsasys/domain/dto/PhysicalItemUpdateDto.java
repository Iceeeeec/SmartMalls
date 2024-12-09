package com.hsasys.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhysicalItemUpdateDto
{
    private Integer item_id;

    private Double content;
}
