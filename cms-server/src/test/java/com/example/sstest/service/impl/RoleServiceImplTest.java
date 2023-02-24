package com.example.sstest.service.impl;

import com.example.sstest.service.RoleService;
import com.example.sstest.utils.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RoleServiceImplTest {

    @Autowired
    private RoleService roleService;

    @Test
    void getRolesAndStatusIsNormal() {
        Result rolesAndStatusIsNormal = roleService.getRolesAndStatusIsNormal();
        System.out.println(rolesAndStatusIsNormal);
    }
}