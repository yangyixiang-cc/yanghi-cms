package com.example.sstest.mapper;

import com.example.sstest.bean.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RoleMapperTest {

    @Autowired
    private  RoleMapper roleMapper;

    @Test
    void getRolesAndStatusIsNormalByUserId() {
        List<Role> rolesAndStatusIsNormalByUserId = roleMapper.getRolesAndStatusIsNormalByUserId(1627483864552165378L);
    }

    @Test
    void testGetRolesAndStatusIsNormalByUserId() {
    }

}