package com.hsasys.controller;

import com.hsasys.result.Result;
import com.hsasys.service.select.SFoodTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Transactional
@ResponseBody
@CrossOrigin
@RequestMapping("/type")
public class FoodTypeController {
    @Autowired
    private SFoodTypeService sFoodTypeService;

    @GetMapping("/all")
    Result selectAllFoodType()
    {
        return sFoodTypeService.selectAllFoodType();
    }

}
