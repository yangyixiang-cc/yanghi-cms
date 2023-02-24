package com.example.sstest.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
class MenuMapperTest {

    @Autowired
    private MenuMapper menuMapper;

    @Test
    void selectPermsByUserId() {
        List<String> strings = menuMapper.selectPermsByUserId(1548991108706627585L);
        System.out.println(strings);

    }

    @Test
    void getMenusAndStatusISNormalByRoleId() {
        List<String> menusAndStatusISNormalByRoleId = menuMapper.getMenusAndStatusISNormalByRoleId(1L);
    }
}