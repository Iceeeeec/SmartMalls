package com.hsasys.controller;

import com.hsasys.domain.dto.PhysicalItemDto;
import com.hsasys.domain.entity.PhysicalItem;
import com.hsasys.domain.entity.PhysicalType;
import com.hsasys.domain.vo.PhysicalItemVo;
import com.hsasys.result.Result;
import com.hsasys.service.select.PhysicalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/physical")
public class PhysicalController
{


    @Autowired
    private PhysicalService physicalService;

    /**
     * 查询体检所有类型
     */
    @GetMapping("/type")
    public Result<List<PhysicalType>> selectTypes()
    {
        return physicalService.selectTypes();
    }

    /**
     * 根据类型查询体检项目
     * @param typeId
     * @return
     */
    @GetMapping("/{typeId}")
    public Result<List<PhysicalItemVo>> selectItemsByTypeId(@PathVariable Integer typeId)
    {
        return physicalService.selectItemByid(typeId);
    }

    /**
     * 获取所有的项目
     * @return
     */
    @GetMapping("/items")
    public Result<List<PhysicalItem>> selectItems()
    {
        return physicalService.selectItems();
    }

    /**
     * 生成体检报告
     */
    @PostMapping("/report")
    public Result createReport(@RequestBody PhysicalItemDto physicalItemDto)
    {
        return physicalService.createReport(physicalItemDto);
    }
}
