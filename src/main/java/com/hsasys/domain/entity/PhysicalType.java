package com.hsasys.domain.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//体检的类型
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhysicalType
{
    private Integer id;

    private String physicalName;
}
