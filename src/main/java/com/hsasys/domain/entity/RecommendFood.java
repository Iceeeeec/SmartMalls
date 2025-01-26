package com.hsasys.domain.entity;

import com.mysql.cj.log.Log;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//推荐食物
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RecommendFood
{
    private Long id;
    private Long userId;
    private Integer foodId;
    private Double score;
}
