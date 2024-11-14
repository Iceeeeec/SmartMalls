package com.hsasys.domain;

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
@TableName("threemeals")
public class ThreeMeal {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String breakfast;
    private String lunch;
    private String dinner;
    @TableField("id_users")
    private Integer userId;
    private String date;
}
