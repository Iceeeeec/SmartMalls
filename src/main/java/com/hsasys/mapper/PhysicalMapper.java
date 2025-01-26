package com.hsasys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hsasys.domain.entity.PhysicalItem;
import com.hsasys.domain.entity.PhysicalResult;
import com.hsasys.domain.entity.PhysicalType;
import com.hsasys.domain.rela.PhysicalItemNut;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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

    @Select( "select unit from physical_item where physical_item_name = #{itemName}" )
    String selectUnitByItemName(String itemName);

    @Select("select physical_item_name from physical_item")
    List<String> selectAllItem();

    List<PhysicalItemNut> selectItemNutByItemIdsAndStatus(@Param("itemIds") List<Integer> itemIds, @Param("status") int status);
}
