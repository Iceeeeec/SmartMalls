package com.hsasys.service.select.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hsasys.dao.domain_mapper.PhysicalMapper;
import com.hsasys.domain.dto.PhysicalItemDto;
import com.hsasys.domain.entity.PhysicalItem;
import com.hsasys.domain.entity.PhysicalType;
import com.hsasys.domain.vo.PhysicalItemVo;
import com.hsasys.result.Result;
import com.hsasys.service.select.PhysicalService;
import com.hsasys.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhysicalServiceImpl implements PhysicalService
{

    @Autowired
    private PhysicalMapper physicalMapper;

    @Override
    public Result<List<PhysicalType>> selectTypes()
    {
        List<PhysicalType> types= physicalMapper.selectTypes();
        return Result.success(types);
    }

    @Override
    public Result<List<PhysicalItemVo>> selectItemByid(Integer id)
    {
        LambdaQueryWrapper<PhysicalItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PhysicalItem::getTypeId, id);
        List<PhysicalItem> physicalItems = physicalMapper.selectList(wrapper);
        PhysicalType type = physicalMapper.selectTypeById(id);
        List<PhysicalItemVo> physicalItemVos = BeanCopyUtils.copyBeanList(physicalItems, PhysicalItemVo.class);
        physicalItemVos.forEach(item -> item.setPhysicalType(type));
        return Result.success(physicalItemVos);
    }

    @Override
    public Result<List<PhysicalItem>> selectItems()
    {
        return Result.success(physicalMapper.selectList(null));
    }

    /**
     * 生成体检报告
     * @param physicalItemDto
     * @return
     */
    @Override
    public Result createReport(PhysicalItemDto physicalItemDto)
    {
        return null;
    }
}
