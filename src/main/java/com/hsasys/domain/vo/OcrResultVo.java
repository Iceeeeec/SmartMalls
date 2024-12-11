package com.hsasys.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OcrResultVo
{
    //项目名字
    private String itemName;

    //值
    private Object content;

    //单位
    private String unit;
}
