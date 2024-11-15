package com.hsasys.service.insert.impl;

import com.hsasys.controller.tools.Code;
import com.hsasys.controller.tools.Result;
import com.hsasys.dao.domain_mapper.ThreeMealMapper;
import com.hsasys.domain.ThreeMeal;
import com.hsasys.domain.vo.ThreeMealReceiveVo;
import com.hsasys.service.insert.IThreeMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class IThreeMealServiceImpl implements IThreeMealService {
    @Autowired
    private ThreeMealMapper threeMealMapper;

    private final Result err_result = new Result(Code.ERR,null);

    @Override
    public Result insertThreeMeal(ThreeMealReceiveVo vo) {
        ThreeMeal threeMeal = new ThreeMeal();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String format = dateFormat.format(date);
        threeMeal.setDate(format);
        threeMeal.setBreakfast(vo.getBreakfast());
        threeMeal.setLunch(vo.getLunch());
        threeMeal.setDinner(vo.getDinner());
        threeMeal.setUserId(vo.getUserId());

        if(threeMealMapper.insert(threeMeal) <= 0)
            return err_result;

        return new Result(Code.OK, null, "successful");
    }
}
