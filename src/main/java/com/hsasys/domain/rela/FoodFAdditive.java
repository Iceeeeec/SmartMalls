package com.hsasys.domain.rela;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@TableName("rela_ffood_fadditives")
public class FoodFAdditive {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField("id_fFoods")
    private Integer foodId;
    @TableField("id_fAdditives")
    private Integer addictiveId;
}
