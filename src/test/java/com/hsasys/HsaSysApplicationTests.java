package com.hsasys;

import com.hsasys.dao.domain_mapper.AllergenMapper;
import com.hsasys.domain.dto.UserLoginDto;
import com.hsasys.domain.entity.Allergen;
import com.hsasys.domain.entity.User;
import com.hsasys.domain.vo.UserLoginVo;
import com.hsasys.result.Result;
import com.hsasys.service.select.SUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
class HsaSysApplicationTests {
    @Autowired
    private SUserService userService;


    @Autowired
    private AllergenMapper allergenMapper;
    @Test
    void test_login(){
        UserLoginDto user = new UserLoginDto();
        user.setUsername("root");
        user.setPassword("123456");
        Result<UserLoginVo> login = userService.login(user);
        System.out.println(login);
    }

    @Test
    void test_rel(){
        List<Allergen> allergens = allergenMapper.selectList(null);
        System.out.println(allergens);
    }

    @Test
    void test_list()
    {
        System.out.println(Arrays.asList(4, 3, 2));
    }

}
