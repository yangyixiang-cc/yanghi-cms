package com.example.sstest.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sstest.bean.Role;
import com.example.sstest.bean.RoleIncludeMenus;
import com.example.sstest.service.RoleService;
import com.example.sstest.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/status_normal")
    public Result getRolesAndStatusIsNormal(){
        return roleService.getRolesAndStatusIsNormal();
    }

    @GetMapping("/user_roles")
    public Result getUserRolesById(@RequestParam("userId") Long userId){
        return roleService.getRolesAndStatusIsNormalByUserId(userId);
    }

    @GetMapping("/allroles")
    public Result gerAllRoles(){
        return roleService.getAllRoles();
    }

    @GetMapping("/page")
    public Result getRolePage(@RequestParam("current") int current, @RequestParam("pageSize") int pageSize){
        return roleService.getRolePage(current,pageSize);
    }

    @PostMapping("/return_search_page")
    public Result<Page<Role>> getRoleSearchPage(@RequestParam("current") int current, @RequestParam("pageSize") int pageSize,
                                                @RequestParam("name") String name,
                                                @RequestParam("roleKey") String roleKey,
                                                @RequestParam("status") String status,
                                                @RequestParam("beginTime") String beginTime,
                                                @RequestParam("lastTime") String lastTime) {
        return roleService.getRoleSearchPage(current, pageSize,
                name,
                roleKey,
                status,
                beginTime,
                lastTime);
    }

    @DeleteMapping("/{id}")
    public  Result deleteRoleOneById(@PathVariable("id") Long id){
        return roleService.deleteRoleOneById(id);
    }


    @PostMapping("/add")
    public Result addRoleOne(Role role){
        return roleService.addRole(role);
    }

    @PostMapping("/update")
    public Result updateRoleOne(RoleIncludeMenus roleIncludeMenus){
        return roleService.updateRoleOne(roleIncludeMenus);
    }

    @PostMapping("/betch_delete")
    public Result betchDelterRole(@RequestParam("ids[]") Long[] ids){
        List<Long> longs = Arrays.asList(ids);
        return roleService.betchDeleteRole(longs);
    }

}
