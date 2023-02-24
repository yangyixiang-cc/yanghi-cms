package com.example.sstest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sstest.bean.Role;
import com.example.sstest.bean.RoleAndMenu;
import com.example.sstest.bean.RoleIncludeMenus;
import com.example.sstest.mapper.RoleAndMenuMapper;
import com.example.sstest.mapper.RoleMapper;
import com.example.sstest.service.RoleService;
import com.example.sstest.utils.Constant;
import com.example.sstest.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;


@Service
@Transactional
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleAndMenuMapper roleAndMenuMapper;


    @Override
    public Result getRolesAndStatusIsNormal() {
        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
        roleQueryWrapper.select("id", "name", "role_key").eq("status", Constant.ROLE_NORMAL);
        List<Role> list = this.list(roleQueryWrapper);
        return Result.ok(list);
    }

    @Override
    public Result getRolesAndStatusIsNormalByUserId(Long id) {
        List<Role> rolesAndStatusIsNormalByUserId = roleMapper.getRolesAndStatusIsNormalByUserId(id);
        return Result.ok(rolesAndStatusIsNormalByUserId);
    }

    @Override
    public Result getAllRoles() {
        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
        List<Role> list = this.list(roleQueryWrapper.select("id", "name", "role_key", "status","remark", "create_time"));
        return Result.ok(list, "获取全部角色信息成功");
    }

    @Override
    public Result<Page<Role>> getRolePage(int current, int pageSize) {
        Page<Role> rolePage = new Page<>(current, pageSize);
        Page<Role> page = this.page(rolePage);
        return Result.ok(page);
    }

    @Override
    public Result<Page<Role>> getRoleSearchPage(int current, int pageSize, String name, String roleKey, String status, String beginTime, String lastTime) {
        boolean isTime = true;
        if("".equals(beginTime) && "".equals(lastTime)){
            isTime = false;
        }
        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
        roleQueryWrapper
                .like(!"".equals(name),"name", name)
                .like(!"".equals(roleKey),"role_key",roleKey)
                .like(!"".equals(status),"status", status)
                .between(isTime,"create_time",beginTime, lastTime);
        Page<Role> rolePage = new Page<>(current, pageSize);
        Page<Role> page = this.page(rolePage,roleQueryWrapper);
        return Result.ok(page);
    }

    @Override
    public Result deleteRoleOneById(Long id) {
        boolean b = this.removeById(id);
        if(!b){
            return Result.failed("数据删除失败");
        }
        return Result.ok("数据删除成功");
    }

    @Override
    public Result addRole(Role role) {
        String name = role.getName();
        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
        roleQueryWrapper.eq("name", name);
        List<Role> list = this.list(roleQueryWrapper);
        if(list.size() > 0){
            return Result.failed("角色名已存在");
        }
        boolean save = this.save(role);
        if(save){
            return Result.ok("添加角色成功");
        }
        return Result.failed("添加角色失败");
    }

    @Override
    public Result updateRoleOne(RoleIncludeMenus roleIncludeMenus) {
        //首先通过角色ID更新sys_role_menu 设置其选中的菜单项
        String[] chanceMenusIds = roleIncludeMenus.getChanceMenus();
        List<Long> chanceMenus = Arrays.stream(chanceMenusIds).map(s -> Long.valueOf(s)).collect(Collectors.toList());
        QueryWrapper<RoleAndMenu> roleAndMenuQueryWrapper = new QueryWrapper<>();
        roleAndMenuQueryWrapper.in("menu_id", chanceMenus).eq("role_id", roleIncludeMenus.getId());
        List<RoleAndMenu> roleAndMenus = roleAndMenuMapper.selectList(roleAndMenuQueryWrapper);
        List<Long> existedMenus = roleAndMenus.stream()
                .map(roleAndMenu -> roleAndMenu.getMenuId())
                .collect(Collectors.toList());
        boolean b1 = chanceMenus.removeAll(existedMenus);
        if(b1){
            chanceMenus.stream().forEach(aLong -> {
                roleAndMenuMapper.insert(new RoleAndMenu(null, roleIncludeMenus.getId(), aLong, null));
            });
        }
        boolean b = this.updateById(roleIncludeMenus);
        if(b){
            return Result.ok("更新角色成功");
        }
        return Result.failed("更新角色失败");
    }

    @Override
    public Result betchDeleteRole(List<Long> ids) {
        boolean b = this.removeBatchByIds(ids);
        if(b){
            return Result.ok("批量删除角色成功");
        }
        return Result.failed("批量删除角色失败");
    }

}
