package com.hsasys.domain.vo;

import com.hsasys.domain.FinishedFood;
import com.hsasys.domain.FoodType;
import lombok.Data;

import java.util.List;

@Data
public class FoodTypeVo {
    private Integer id;
    private String type;

    public FoodTypeVo(FoodType foodType) {
        this.id = foodType.getId();
        this.type = foodType.getType();
    }

    private List<FinishedFood> finishedFood = null;
}
