package com.hsasys.dao.domain_mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hsasys.domain.UserType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserTypeMapper extends BaseMapper<UserType> {

}
