package com.example.sstest.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sstest.bean.User;
import com.example.sstest.service.UserService;
import com.example.sstest.utils.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Test
    void getUserSearchPage() {
        Result<Page<User>> t = userService.getUserSearchPage(1, 5, "", "男", "", "", "", "", "");
        System.out.println(t.getData().getRecords());
    }
}