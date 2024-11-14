package com.hsasys.domain.rela;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("rela_users_ffood_collections")
public class UserFoodCollection {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField("id_users")
    private Integer userId;
    @TableField("id_fFoods")
    private Integer foodId;
}
