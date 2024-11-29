package com.hsasys.dao.domain_mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hsasys.domain.entity.ChronicDisease;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChronicDiseaseMapper extends BaseMapper<ChronicDisease>
{

    List<ChronicDisease> selectChronicDiseaseById(Integer userId);

    @Delete("delete from user_disease where user_id = #{userId}")
    void deleteByUserId(Integer userId);

    void insertBatch(@Param("diseaseIds") List<Integer> diseaseIds, @Param("id") Integer id);
}
