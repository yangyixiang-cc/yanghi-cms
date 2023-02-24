package com.example.sstest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.sstest.bean.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper extends BaseMapper<Menu> {


    List<String> selectPermsByUserId(Long userid);

    List<Menu> selectUserPermissionByUserId(Long id);

    List<String> getMenusAndStatusISNormalByRoleId(Long roleId);
}
