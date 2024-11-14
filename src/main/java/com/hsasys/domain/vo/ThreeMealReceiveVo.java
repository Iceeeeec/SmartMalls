package com.hsasys.domain.vo;

import lombok.Data;

@Data
public class ThreeMealReceiveVo {
    private Integer userId;
    private String breakfast;
    private String lunch;
    private String dinner;
}
