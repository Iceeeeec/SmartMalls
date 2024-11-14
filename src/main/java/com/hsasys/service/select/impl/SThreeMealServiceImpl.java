package com.hsasys.service.select.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hsasys.controller.tools.Code;
import com.hsasys.controller.tools.Result;
import com.hsasys.dao.domain_mapper.ThreeMealMapper;
import com.hsasys.domain.ThreeMeal;
import com.hsasys.service.select.SThreeMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SThreeMealServiceImpl implements SThreeMealService {
    @Autowired
    private ThreeMealMapper threeMealMapper;

    private static final Result err_result = new Result(Code.ERR,null);

    @Override
    public Result getAllMeals(Integer userId) {
        LambdaQueryWrapper<ThreeMeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ThreeMeal::getUserId, userId);
        List<ThreeMeal> threeMeals = threeMealMapper.selectList(wrapper);
        if(threeMeals == null)
            return err_result;

        return new Result(Code.OK, threeMeals, "successful");
    }
}
