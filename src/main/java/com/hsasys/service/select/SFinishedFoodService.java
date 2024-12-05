package com.hsasys.service.select;

import com.hsasys.result.Result;
import java.util.List;
import java.util.Map;

public interface SFinishedFoodService {
    Result selectAllFoods();
    Result selectOne(String name);
    Result selectById(Integer id);
}
