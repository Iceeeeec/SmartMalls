package com.hsasys.domain.vo;

import com.hsasys.domain.*;
import lombok.Data;

import java.util.List;

@Data
public class FinishedFoodAttributeVo {
    private Integer id;
    private String name;
    private String info;
    private String picture;

    public FinishedFoodAttributeVo(FinishedFood food) {
        this.id = food.getId();
        this.name = food.getName();
        this.info = food.getInfo();
        this.picture = food.getPicture();
    }

    private List<FoodAdditive> foodAdditives = null;
    private List<AddedSugar> addedSugars = null;
    private List<TransFattyAcid> transFattyAcids = null;
    private List<FoodLabel> foodLabels = null;
    private List<FoodType> foodTypes = null;
}
