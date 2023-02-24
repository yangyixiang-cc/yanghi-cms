package com.example.sstest.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sstest.bean.Menu;
import com.example.sstest.bean.Role;
import com.example.sstest.service.MenuService;
import com.example.sstest.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;


    @GetMapping("/menu_tree")
    public Result getMenuTree() {
        return menuService.getAllMenusAndStatusISNormal();
    }

    @GetMapping("chance_menus")
    public Result getMenusAndStatusISNormalByRoleId(@RequestParam("roleId") Long roleId) {
        return menuService.getMenusAndStatusISNormalByRoleId(roleId);
    }


    @GetMapping("/allmenus")
    public Result getAllMenu() {
        return menuService.getAllMenus();
    }

    @GetMapping("/page")
    public Result getMenuPage(@RequestParam("current") int current, @RequestParam("pageSize") int pageSize) {
        return menuService.getMenuPage(current, pageSize);
    }

    @PostMapping("/return_search_page")
    public Result<Page<Role>> getMenuSearchPage(@RequestParam("current") int current,
                                                @RequestParam("pageSize") int pageSize,
                                                @RequestParam("menuName") String menuName,
                                                @RequestParam("name") String name,
                                                @RequestParam("path") String path,
                                                @RequestParam("redirect") String redirect,
                                                @RequestParam("component") String component,
                                                @RequestParam("perms") String perms,
                                                @RequestParam("status") String status,
                                                @RequestParam("visible") String visible,
                                                @RequestParam("beginTime") String beginTime,
                                                @RequestParam("lastTime") String lastTime) {
        return menuService.getMenuSearchPage(
                current,
                pageSize,
                menuName,
                name,
                path,
                redirect,
                component,
                perms,
                status,
                visible,
                beginTime,
                lastTime);
    }

    @DeleteMapping("/{id}")
    public Result deleteMenuOneById(@PathVariable("id") Long id) {
        return menuService.deleteMenuOneById(id);
    }


    @PostMapping("/add")
    public Result addMenuOne(Menu menu) {
        return menuService.addMenu(menu);
    }

    @PutMapping("/update")
    public Result updateMenuOne(Menu menu
    ) {
        return menuService.updateMenuOne(menu);
    }

    @PostMapping("/betch_delete")
    public Result betchDelterMenu(@RequestParam("ids[]") Long[] ids) {
        List<Long> longs = Arrays.asList(ids);
        return menuService.betchDeleteMenu(longs);
    }

    @GetMapping("/father_menus")
    Result getFatherMenus() {
        return menuService.getFatherMenus();
    }


    @GetMapping("/sub_menus")
    Result getSubMenus() {
        return menuService.getSubMenus();
    }

    @GetMapping("/father_id")
    public Result getFatherMenuIdByMenuId(@RequestParam("id") Long id) {
        return menuService.getFatherMenuIdByMenuId(id);
    }
}
