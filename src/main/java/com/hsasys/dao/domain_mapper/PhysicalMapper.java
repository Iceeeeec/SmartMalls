package com.hsasys.dao.domain_mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hsasys.domain.entity.PhysicalItem;
import com.hsasys.domain.entity.PhysicalResult;
import com.hsasys.domain.entity.PhysicalType;
import com.hsasys.domain.vo.ReportInfoVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PhysicalMapper extends BaseMapper<PhysicalItem>
{

    @Select("select * from physical_type")
    List<PhysicalType> selectTypes();

    @Select("select * from physical_type where id = #{id}")
    PhysicalType selectTypeById(Integer id);

    @Insert("insert into physical_result(user_id, item_id, content, status) values(#{userId}, #{itemId}, #{content}, #{status})")
    void insertResult(PhysicalResult physicalResult);


}
