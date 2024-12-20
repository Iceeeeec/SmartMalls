package com.hsasys.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hsasys.domain.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("users")
public class User
{
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    private String password;
    private Integer age;
    private String sex;
    @TableField("BMI")
    private Double bmi;
    private Double height;
    private Double weight;
    private String allergen;
    @TableField("chronicDisease")
    private String disease;
    @TableField("foodPreference")
    private String preference;
    @TableField("full_name")
    private String name;

    @TableField(exist = false)
    private UserType userType;

    public void setAccount(String root) {
    }
}
