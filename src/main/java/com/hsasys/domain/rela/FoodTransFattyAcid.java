package com.hsasys.domain.rela;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("rela_ffood_transfattyacid")
public class FoodTransFattyAcid {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField("id_fFoods")
    private Integer foodId;
    @TableField("id_transFattyAcid")
    private Integer transFattyAcidId;
}
