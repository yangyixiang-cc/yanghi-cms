package com.example.sstest.mapper;

import com.example.sstest.bean.Menu;
import com.example.sstest.bean.User;
import com.example.sstest.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void deleteUseOneById() {
    }

    @Test
    void selectUserPermissionByUserId() {
    }
}