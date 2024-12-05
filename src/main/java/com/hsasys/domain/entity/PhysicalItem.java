package com.hsasys.domain.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//体检具体项目
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhysicalItem
{
    private Integer id;

    private String physicalItemName;

    //所属类型id
    private Integer typeId;

    //正常范围
    private String normalRange;

    //单位
    private String unit;
}
