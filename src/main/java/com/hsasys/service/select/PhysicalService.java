package com.hsasys.service.select;

import com.hsasys.domain.dto.PhysicalItemDto;
import com.hsasys.domain.entity.PhysicalItem;
import com.hsasys.domain.entity.PhysicalType;
import com.hsasys.domain.vo.PhysicalItemVo;
import com.hsasys.result.Result;

import java.util.List;

public interface PhysicalService
{

    /**
     * 查询体检类型
     * @return
     */
    Result<List<PhysicalType>> selectTypes();

    /**
     * 根据体检类型id查询体检项目
     * @param id
     * @return
     */
    Result<List<PhysicalItemVo>> selectItemByid(Integer id);

    /**
     * 查询所有体检项目
     * @return
     */
    Result<List<PhysicalItem>> selectItems();

    /**
     * 生成体检报告
     * @param physicalItemDto
     * @return
     */
    Result createReport(PhysicalItemDto physicalItemDto);
}
