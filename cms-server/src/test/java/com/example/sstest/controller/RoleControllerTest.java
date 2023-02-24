package com.example.sstest.controller;

import com.example.sstest.utils.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RoleControllerTest {

    @Autowired
    private RoleController roleController;

    @Test
    void getRolesAndStatusIsNormal() {
        Result rolesAndStatusIsNormal = roleController.getRolesAndStatusIsNormal();
        System.out.println(rolesAndStatusIsNormal);
    }
}