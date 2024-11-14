package com.hsasys.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("ingre_transfattyacid")
public class TransFattyAcid {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    @TableField("information")
    private String info;
}
