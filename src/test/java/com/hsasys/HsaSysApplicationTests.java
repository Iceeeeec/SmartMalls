package com.hsasys;

import com.hsasys.controller.tools.Result;
import com.hsasys.domain.User;
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
        User user = new User();
        user.setAccount("root");
        user.setPassword("123456");
        Result login = userService.login(user);
        System.out.println(login);
    }

    @Test
    void test_rel(){

    }

}
