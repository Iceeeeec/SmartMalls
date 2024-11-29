package com.hsasys;

import com.hsasys.domain.dto.UserLoginDto;
import com.hsasys.domain.entity.User;
import com.hsasys.domain.vo.UserLoginVo;
import com.hsasys.result.Result;
import com.hsasys.service.select.SUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HsaSysApplicationTests {
    @Autowired
    private SUserService userService;

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

    }

}
