package com.hsasys.baiduocr.service.impl;

import com.hsasys.baiduocr.mapper.foodaddMapper;
import com.hsasys.baiduocr.service.foodaddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class foodaddServiceImpl implements foodaddService {
    @Autowired
    private foodaddMapper foodaddMapper;
    @Override
    public List<String> listadd() {
        List<String> list=foodaddMapper.listadd();
        return list;
    }

    @Override
    public List<String> listsugar() {
        List<String> list = foodaddMapper.listsugar();
        return list;
    }

    @Override
    public List<String> listtran() {
        List<String> list = foodaddMapper.listtran();
        return list;
    }
}
