package com.hsasys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hsasys.domain.entity.UserType;
import com.hsasys.domain.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User>
{
    @Insert("insert into users(username, password) values(#{username}, #{password})")
    void addUser(User user);

    UserType selectUserTypeById(Long id);


    Integer calculateScore(Long id);
}
