package com.hsasys.domain.vo;


import com.hsasys.domain.User;
import com.hsasys.domain.UserType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserUTypeVo extends User {
    private List<UserType> type;
}
