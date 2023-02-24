package com.example.sstest.service.impl;

import com.example.sstest.service.MenuService;
import com.example.sstest.utils.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MeunServiceImplTest {

    @Autowired
    private MenuService menuService;

    @Test
    void gerAllMenusAndStatusISNormal() {
        Result result = menuService.getAllMenusAndStatusISNormal();
        System.out.println(result);
    }
}