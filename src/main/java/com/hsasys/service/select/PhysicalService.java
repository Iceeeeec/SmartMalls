package com.hsasys.service.select;

import com.hsasys.domain.dto.PhysicalItemDto;
import com.hsasys.domain.dto.PhysicalItemUpdateDto;
import com.hsasys.domain.entity.PhysicalItem;
import com.hsasys.domain.entity.PhysicalType;
import com.hsasys.domain.vo.PhysicalItemVo;
import com.hsasys.result.Result;

import java.io.IOException;
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

    /**
     * 保存体检项目
     * @param physicalItemDto
     * @return
     */
    Result saveReport(PhysicalItemDto physicalItemDto);

    /**
     * 删除体检报告
     * @return
     */
    Result cancelReport();

    /**
     * 获取体检报告
     * @return
     */
    Result getReportInfo(Integer memberId);

    /**
     * 更新体检报告
     * @param physicalItemUpdateDto
     * @return
     */
    Result updateReport(PhysicalItemUpdateDto physicalItemUpdateDto);


    /**
     * 保存体检报告到数据库
     */
    void savePhysicalReport(Long userId, String filePath);
}
