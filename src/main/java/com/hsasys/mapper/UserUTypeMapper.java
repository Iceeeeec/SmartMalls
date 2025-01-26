package com.hsasys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hsasys.domain.rela.BmiNutrition;
import com.hsasys.domain.rela.UserUType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserUTypeMapper extends BaseMapper<UserUType>
{

    List<BmiNutrition> selectBmiNutIdsByUserId(@Param("userId") Long userId);
}
