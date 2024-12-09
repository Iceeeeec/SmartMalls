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
public class ReportInfoVo {
    //项目id
    private Integer itemId;
    //含量
    private Double content;
    //状态
    private Integer status;
    //类型id
    private Integer typeId;
    //名字
    private String physicalItemName;
    //正常范围
    private String normalRange;
    //单位
    private String unit;

}
