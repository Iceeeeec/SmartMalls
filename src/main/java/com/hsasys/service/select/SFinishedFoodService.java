package com.hsasys.service.select;

import com.hsasys.controller.tools.Result;

import java.util.List;
import java.util.Map;

public interface SFinishedFoodService {
    Result selectOneFood(String name);
    Result selectAllFoods();
    public List<Map<String,Object>> selectOne(String name);

}
