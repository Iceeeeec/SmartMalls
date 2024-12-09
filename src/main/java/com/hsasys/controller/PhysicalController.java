package com.hsasys.controller;

import com.hsasys.domain.dto.PhysicalItemDto;
import com.hsasys.domain.entity.PhysicalItem;
import com.hsasys.domain.entity.PhysicalType;
import com.hsasys.domain.vo.PhysicalItemVo;
import com.hsasys.result.Result;
import com.hsasys.service.select.PhysicalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
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

    /**
     * 保存体检项目到数据库
     */
    @PostMapping("/save")
    public Result selectReportByStatus(@RequestBody PhysicalItemDto physicalItemDto)
    {
        return physicalService.saveReport(physicalItemDto);
    }

    /**
     * 取消接收结果
     */
    @DeleteMapping("/cancel")
    public Result cancelReport()
    {
        return physicalService.cancelReport();
    }

    @GetMapping("/reportInfo")
    public Result getReportInfo()
    {
        return physicalService.getReportInfo();
    }
}
