package com.example.sstest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.sstest.bean.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> getRolesAndStatusIsNormalByUserId(Long id);


}
