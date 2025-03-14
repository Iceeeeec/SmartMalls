package com.hsasys.domain.vo;

import com.hsasys.domain.entity.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    private List<Allergen> allergen;//过敏源

    private List<ChronicDisease> disease;//慢性病

    private List<FoodPreference> preference;//食物喜好

    private FamilyRole familyRole;

    private String name;

    private String token;

    //用户属于的类型
    private UserType userType;

}
