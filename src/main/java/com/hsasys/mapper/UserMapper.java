package com.hsasys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hsasys.domain.entity.UserType;
import com.hsasys.domain.entity.User;
import com.hsasys.domain.entity.Advice;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User>
{
    @Insert("insert into users(username, password) values(#{username}, #{password})")
    void addUser(User user);

    UserType selectUserTypeById(Long id);


    Integer calculateScore(Long id);

    Advice selectAdviceByUserId(Long userId);

    void deleteAdviceByUserId(Integer userId);
}
