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
        List<Role> list = this.list(roleQueryWrapper.select("id", "name", "role_key", "status", "remark", "create_time"));
        return Result.ok(list, "??????????????????????????????");
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
        if ("".equals(beginTime) && "".equals(lastTime)) {
            isTime = false;
        }
        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
        roleQueryWrapper
                .like(!"".equals(name), "name", name)
                .like(!"".equals(roleKey), "role_key", roleKey)
                .like(!"".equals(status), "status", status)
                .between(isTime, "create_time", beginTime, lastTime);
        Page<Role> rolePage = new Page<>(current, pageSize);
        Page<Role> page = this.page(rolePage, roleQueryWrapper);
        return Result.ok(page);
    }

    @Override
    public Result deleteRoleOneById(Long id) {
        boolean b = this.removeById(id);
        if (!b) {
            return Result.failed("??????????????????");
        }
        return Result.ok("??????????????????");
    }

    @Override
    public Result addRole(Role role) {
        String name = role.getName();
        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
        roleQueryWrapper.eq("name", name);
        List<Role> list = this.list(roleQueryWrapper);
        if (list.size() > 0) {
            return Result.failed("??????????????????");
        }
        boolean save = this.save(role);
        if (save) {
            return Result.ok("??????????????????");
        }
        return Result.failed("??????????????????");
    }

    @Override
    public Result updateRoleOne(RoleIncludeMenus roleIncludeMenus) {
        //??????????????????ID??????sys_role_menu ???????????????????????????
        String[] chanceMenusIds = roleIncludeMenus.getChanceMenus();
        List<Long> chanceMenus = Arrays.stream(chanceMenusIds).map(s -> Long.valueOf(s)).collect(Collectors.toList());
        QueryWrapper<RoleAndMenu> roleAndMenuQueryWrapper = new QueryWrapper<>();
        roleAndMenuQueryWrapper.in("menu_id", chanceMenus).eq("role_id", roleIncludeMenus.getId());
        List<RoleAndMenu> roleAndMenus = roleAndMenuMapper.selectList(roleAndMenuQueryWrapper);
        List<Long> existedMenus = roleAndMenus.stream()
                .map(roleAndMenu -> roleAndMenu.getMenuId())
                .collect(Collectors.toList());
        boolean b1 = chanceMenus.removeAll(existedMenus);
        if (b1) {
            chanceMenus.stream().forEach(aLong -> {
                roleAndMenuMapper.insert(new RoleAndMenu(null, roleIncludeMenus.getId(), aLong, null));
            });
        }
        boolean b = this.updateById(roleIncludeMenus);
        if (b) {
            return Result.ok("??????????????????");
        }
        return Result.failed("??????????????????");
    }

    @Override
    public Result betchDeleteRole(List<Long> ids) {
        boolean b = this.removeBatchByIds(ids);
        if (b) {
            return Result.ok("????????????????????????");
        }
        return Result.failed("????????????????????????");
    }

}
