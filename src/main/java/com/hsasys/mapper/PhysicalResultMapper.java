package com.hsasys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hsasys.domain.entity.PhysicalResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PhysicalResultMapper extends BaseMapper<PhysicalResult>
{

    @Select("select item_id from physical_result where user_id = #{userId} and status = #{status}")
    List<Integer> selectItemIdsByUserIdAndStatus(@Param("userId") Long userId, @Param("status") int status);
}
