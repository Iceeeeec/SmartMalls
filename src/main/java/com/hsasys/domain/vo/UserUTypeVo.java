package com.hsasys.domain.vo;


import com.hsasys.domain.entity.UserType;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUTypeVo
{
    private List<UserType> type;
}
