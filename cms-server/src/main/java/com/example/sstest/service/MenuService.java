package com.example.sstest.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sstest.bean.Menu;
import com.example.sstest.utils.Result;

import java.util.List;

public interface MenuService extends IService<Menu> {

    Result getAllMenusAndStatusISNormal();

    //2.通过角色ID获取该角色的菜单项（状态正常）再查看是否有子项，进行整合返回树型结构

    Result getMenusAndStatusISNormalByRoleId(Long roleId);

    Result getFatherMenus();

    Result getSubMenus();

    Result getAllMenus();

    /**
     * 角色分页
     *
     * @param current  当前页
     * @param pageSize 每页条数
     * @return
     */
    Result getMenuPage(int current, int pageSize);

    /**
     * 菜单分页 搜索
     *
     * @param current  当前页
     * @param pageSize 每页条数
     * @return
     */
    Result getMenuSearchPage(int current, int pageSize,
                             String menuName,
                             String name,
                             String path,
                             String redirect,
                             String component,
                             String perms,
                             String status,
                             String visible,
                             String beginTime,
                             String lastTime);


    Result deleteMenuOneById(Long id);


    Result addMenu(Menu menu);

    Result updateMenuOne(Menu menu);

    Result betchDeleteMenu(List<Long> ids);

    Result getFatherMenuIdByMenuId(Long id);


}
