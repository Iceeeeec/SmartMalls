package com.hsasys.dao.domain_mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hsasys.domain.UserType;
import com.hsasys.domain.entity.User;
import com.hsasys.domain.vo.UserUTypeVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User>
{
    @Insert("insert into users(username, password) values(#{username}, #{password})")
    void addUser(User user);

    List<UserType> selectUserTypesById(Integer id);
}
