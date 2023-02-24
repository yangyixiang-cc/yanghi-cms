package com.example.sstest.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sstest.bean.User;
import com.example.sstest.utils.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void deleteUserOneById() {
        userService.deleteUserOneById(1548991108706627585L);
    }

    @Test
    void getUserPage() {
        Result<Page<User>> userPage = userService.getUserPage(1, 3);
        System.out.println(userPage.getData().getRecords());
    }

    @Test
    void addUser(){
        User user = new User();
        user.setUserName("yangyihi");
        user.setPassword("YANGyihi2001");
        userService.addUser(user);
    }
}