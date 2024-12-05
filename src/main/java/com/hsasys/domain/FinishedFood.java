package com.hsasys.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("finishedfoods")
public class FinishedFood {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    @TableField("ingredient_info")
    private String info;
    private String picture;
    @TableField("protein_g")
    private Double protein;
    @TableField("fat_g")
    private Double fat;
    @TableField("carbohydrate_g")
    private Double carbohydrate;
    private Double heat;

//    @TableField(exist = false)
//    private List<FoodAdditive> foodAdditives = null;
//    @TableField(exist = false)
//    private List<AddedSugar> addedSugars = null;
//    @TableField(exist = false)
//    private List<TransFattyAcid> transFattyAcids = null;
//    @TableField(exist = false)
//    private List<FoodLabel> foodLabels = null;
//    @TableField(exist = false)
//    private List<FoodType> foodTypes = null;
}
