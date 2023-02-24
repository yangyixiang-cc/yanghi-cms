package com.example.sstest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.sstest.bean.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface UserMapper extends BaseMapper<User> {

    Integer deleteUseOneById(Long id);


    List<String> getUserRolesById(Long id);

}
