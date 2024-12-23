package com.hsasys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hsasys.domain.entity.Allergen;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AllergenMapper extends BaseMapper<Allergen>
{

    List<Allergen> selectAllergensById(Integer userId);

    @Delete("delete from user_allergen where user_id = #{userId}")
    void deleteByUserId(Integer userId);

    void insertBatch(@Param("allergenIds") List<Integer> allergenIds, @Param("id") Integer id);
}
