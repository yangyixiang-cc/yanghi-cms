package com.example.sstest.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sstest.bean.Role;
import com.example.sstest.bean.RoleIncludeMenus;
import com.example.sstest.utils.Result;

import java.util.List;


public interface RoleService extends IService<Role> {
    /**
     * 获取所有角色信息 并且角色状态正常
     * @return
     */
    Result getRolesAndStatusIsNormal();

    Result getRolesAndStatusIsNormalByUserId(Long id);

    Result getAllRoles();

    /**
     * 角色分页
     * @param current 当前页
     * @param pageSize 每页条数
     * @return
     */
    Result<Page<Role>> getRolePage(int current, int pageSize);

    /**
     * 角色分页 搜索
     *
     * @param current  当前页
     * @param pageSize 每页条数
     * @return
     */
    Result<Page<Role>> getRoleSearchPage(int current, int pageSize,
                                         String name,
                                         String roleKey,
                                         String status,
                                         String beginTime,
                                         String lastTime);


    Result deleteRoleOneById(Long id);


    Result addRole(Role role);

    Result updateRoleOne(RoleIncludeMenus roleIncludeMenus);

    Result betchDeleteRole(List<Long> ids);


}
