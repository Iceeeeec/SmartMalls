package com.hsasys.domain.vo;

import com.hsasys.domain.FinishedFood;
import lombok.Data;

@Data
public class FinishedFoodVo {
    private Integer id;
    private String name;
    private String info;
    private String picture;
    private Double protein;
    private Double fat;
    private Double carbohydrate;
    private Double heat;

    public FinishedFoodVo(FinishedFood food) {
        this.id = food.getId();
        this.name = food.getName();
        this.info = food.getInfo();
        this.picture = food.getPicture();
        this.protein = food.getProtein();
        this.fat = food.getFat();
        this.carbohydrate = food.getCarbohydrate();
        this.heat = food.getHeat();
    }

    private String type;
}
