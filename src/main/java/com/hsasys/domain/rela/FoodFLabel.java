package com.hsasys.domain.rela;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("rela_ffood_flabels")
public class FoodFLabel {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField("id_fFoods")
    private Integer foodId;
    @TableField("id_fLabels")
    private Integer labelId;
}
