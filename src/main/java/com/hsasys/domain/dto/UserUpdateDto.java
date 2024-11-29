package com.hsasys.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateDto
{

    private Integer id;

    private String oldPassword;

    private String password;

    private String name;

    private Integer age;

    private Double height;

    private Double weight;

    private List<Integer> allergenIds;

    private List<Integer> diseaseIds;

    private List<Integer> preferenceIds;
}
