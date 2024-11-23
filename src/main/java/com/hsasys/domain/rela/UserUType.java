package com.hsasys.domain.rela;

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
@TableName("rela_users_utypes")
public class UserUType {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField("id_users")
    private Integer userId;
    @TableField("id_uTypes")
    private Integer utype;
}
