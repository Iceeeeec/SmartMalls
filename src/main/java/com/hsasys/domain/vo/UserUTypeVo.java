package com.hsasys.domain.vo;


import com.hsasys.domain.entity.User;
import com.hsasys.domain.UserType;
import lombok.*;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUTypeVo
{
    private List<UserType> type;
}
