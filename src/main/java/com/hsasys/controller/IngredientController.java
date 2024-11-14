package com.hsasys.controller;

import com.hsasys.controller.tools.Result;
import com.hsasys.service.select.SIngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Transactional
@ResponseBody
@CrossOrigin
@RequestMapping("/ingredient")
public class IngredientController {
    @Autowired
    private SIngredientService  sIngredientService;

    @RequestMapping("/all")
    Result selectAllIngredient(){
        return sIngredientService.selectAllIngredient();
    }
}
