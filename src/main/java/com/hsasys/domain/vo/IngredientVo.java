package com.hsasys.domain.vo;

import com.hsasys.domain.*;
import lombok.Data;

import java.util.List;

@Data
public class IngredientVo {
    private List<FoodAdditive> foodAdditives = null;
    private List<AddedSugar> addedSugars = null;
    private List<TransFattyAcid> transFattyAcids = null;
}
