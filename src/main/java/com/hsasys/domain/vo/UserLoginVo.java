package com.hsasys.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginVo
{
    private Integer id;

    private String username;

    private Double bmi;

    private String sex;

    private Integer age;

    private Double height;

    private Double weight;

    private String allergen;//过敏源

    private String disease;//慢性病

    private String preference;//食物喜好

    private String name;

    private String token;


    //用户属于的类型
    private UserUTypeVo userUTypeVo;

}
