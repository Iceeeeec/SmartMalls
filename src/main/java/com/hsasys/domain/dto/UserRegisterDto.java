package com.hsasys.domain.dto;

import lombok.Data;

@Data
public class UserRegisterDto
{
    private String username;

    private String password;

    private String rePassword;

    private String fullName;

    private String sex;

    private Integer age;

    private Double height;

    private Double weight;

}
