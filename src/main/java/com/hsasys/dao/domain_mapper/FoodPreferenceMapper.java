package com.hsasys.dao.domain_mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hsasys.domain.entity.FoodPreference;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FoodPreferenceMapper extends BaseMapper<FoodPreference>
{

    List<FoodPreference> selectPreferenceById(Integer userId);

    @Delete("delete from user_preference where user_id = #{userId}")
    void deleteByUserId(Integer userId);

    void insertBatch(@Param("preferenceIds") List<Integer> preferenceIds, @Param("id") Integer id);
}
