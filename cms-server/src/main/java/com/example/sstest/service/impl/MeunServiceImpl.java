package com.example.sstest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sstest.bean.Menu;
import com.example.sstest.bean.MenuIncludeChildren;
import com.example.sstest.bean.MenuItem;
import com.example.sstest.bean.MenuTreeIncludeChildren;
import com.example.sstest.mapper.MenuMapper;
import com.example.sstest.service.MenuService;
import com.example.sstest.utils.Constant;
import com.example.sstest.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 泗安
 */
@Service
@Transactional
public class MeunServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    //1.获取所有的菜单项（状态正常）检查是否有子项 生成树型结构
    @Override
    public Result getAllMenusAndStatusISNormal() {
        QueryWrapper<Menu> menuQueryWrapper = new QueryWrapper<>();
        menuQueryWrapper.eq("status", Constant.ROLE_NORMAL);
        List<Menu> list = this.list(menuQueryWrapper);
        if(list.size() <= 0){
         return Result.failed("没有查询出菜单信息");
        }
        List<MenuIncludeChildren> menuTree = getMenuTree(list);
        return Result.ok(menuTree);
    }

    //返回ids
    @Override
    public Result getMenusAndStatusISNormalByRoleId(Long roleId) {
        List<String> menusAndStatusISNormalByRoleId = menuMapper.getMenusAndStatusISNormalByRoleId(roleId);
        return Result.ok(menusAndStatusISNormalByRoleId);
    }




    @Override
    public Result getAllMenus() {
        List<Menu> list = this.list();
        List<MenuTreeIncludeChildren> menuTree1 = getMenuTree1(list);
        return Result.ok(menuTree1);
    }

    @Override
    public Result getMenuPage(int current, int pageSize) {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        Page<Menu> menuPage = new Page<>(current, pageSize);
        QueryWrapper<Menu> menuQueryWrapper = new QueryWrapper<>();
        menuQueryWrapper.isNull("p_id");
        Page<Menu> fatherPage = this.page(menuPage, menuQueryWrapper);
        List<Menu> records = fatherPage.getRecords();
        List<Long> fatherIds = records.stream()
                .map(menu -> menu.getId())
                .collect(Collectors.toList());
        QueryWrapper<Menu> menuQueryWrapper1 = new QueryWrapper<>();
        menuQueryWrapper1.in("p_id",fatherIds).isNotNull("p_id");
        List<Menu> subMenus = this.list(menuQueryWrapper1);
        stringObjectHashMap.put("current", menuPage.getCurrent());
        stringObjectHashMap.put("size", menuPage.getSize());
        stringObjectHashMap.put("total", menuPage.getTotal());
        records.addAll(subMenus);
        List<MenuTreeIncludeChildren> menuTree1 = getMenuTree1(records);
        stringObjectHashMap.put("records",menuTree1);
        return Result.ok(stringObjectHashMap);
    }

    @Override
    public Result getMenuSearchPage(int current, int pageSize,
                                    String menuName,
                                    String name,
                                    String path,
                                    String redirect,
                                    String component,
                                    String perms,
                                    String status,
                                    String visible,
                                    String beginTime,
                                    String lastTime) {
        boolean isTime = true;
        if("".equals(beginTime) && "".equals(lastTime)){
            isTime = false;
        }
        QueryWrapper<Menu> menuQueryWrapper = new QueryWrapper<>();
        menuQueryWrapper
                .like(!"".equals(menuName),"menu_name", menuName)
                .like(!"".equals(name),"name", name)
                .like(!"".equals(path),"path",path)
                .like(!"".equals(redirect),"redirect", redirect)
                .like(!"".equals(component),"component", component)
                .like(!"".equals(perms),"perms",perms)
                .like(!"".equals(status),"redirect", status)
                .like(!"".equals(visible),"redirect",visible)
                .between(isTime,"create_time",beginTime, lastTime);
        List<Menu> list = this.list(menuQueryWrapper);
        List<MenuTreeIncludeChildren> menuTree1 = getMenuTree1(list);
        int total = menuTree1.size();
        int page = total%pageSize == 0 ? total/pageSize : total/pageSize + 1;
        int begin = current == 1 ? current-1:(current-1)*pageSize-1;
        int over = current < page ? begin+pageSize : total;
        List<MenuTreeIncludeChildren> records = menuTree1.subList(begin, over);
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("current",current);
        stringObjectHashMap.put("size", pageSize);
        stringObjectHashMap.put("total", total);
        stringObjectHashMap.put("records",records);
        return Result.ok(stringObjectHashMap);
    }

    @Override
    public Result deleteMenuOneById(Long id) {
        Menu byId = this.getById(id);
        if (!Objects.isNull(byId)){
            if(Objects.isNull(byId.getPId())){
                //1.查询是否是父级菜单，如果是删除父级菜单和子菜单
                QueryWrapper<Menu> menuQueryWrapper = new QueryWrapper<>();
                menuQueryWrapper.eq("p_id", byId.getId()).select("id");
                List<Menu> list = this.list(menuQueryWrapper);
                List<Long> collect = list.stream().map(new Function<Menu, Long>() {
                    @Override
                    public Long apply(Menu menu) {
                        return menu.getId();
                    }
                }).collect(Collectors.toList());
                boolean b = this.removeBatchByIds(collect);
                if(b){
                    return Result.ok("删除菜单及子菜单成功");
                }else{
                    return Result.failed("删除菜单项及子菜单失败");
                }
            }else{
                //2.否则删除自身即可
                boolean b = this.removeById(id);
                if(b){
                    return Result.ok("删除菜单项成功");
                }else{
                    return Result.failed("删除菜单项失败");
                }
            }
        }else{
            return Result.failed("不存在该菜单项");
        }
    }

    @Override
    public Result addMenu(Menu menu) {
        String menuName = menu.getMenuName();
        QueryWrapper<Menu> menuQueryWrapper = new QueryWrapper<>();
        menuQueryWrapper.eq("menu_name", menuName);
        List<Menu> list = this.list(menuQueryWrapper);
        if(list.size() > 0){
            return Result.failed("菜单名称已存在");
        }
        boolean save = this.save(menu);
        if(save){
            return Result.ok("添加菜单项成功");
        }
        return Result.failed("添加菜单项失败");
    }

    @Override
    public Result updateMenuOne(Menu menu) {
        boolean b = this.updateById(menu);
        if(b){
            return Result.ok("更新菜单项成功");
        }
        return Result.failed("更新菜单项失败");
    }

    @Override
    public Result betchDeleteMenu(List<Long> ids) {
        //1.通过ids查询所有菜单项
        //2.判断是否有子菜单，如果有则删除父菜单和子菜单
        //3.如果没有则删除本身即可
        List<Menu> menus = this.listByIds(ids);
        List<Long> subMenusId = menus.stream()
                .filter(menu -> !Objects.isNull(menu.getPId()))
                .map(menu -> menu.getId())
                .collect(Collectors.toList());
        List<Long> fatherMenusId = menus.stream()
                .filter(menu -> Objects.isNull(menu.getPId()))
                .map(menu -> menu.getId()).collect(Collectors.toList());
        QueryWrapper<Menu> menuQueryWrapper = new QueryWrapper<>();
        menuQueryWrapper.in("p_id", fatherMenusId);
        List<Menu> fatherSubMenus = this.list(menuQueryWrapper);
        List<Long> fatherSubMenusId = fatherSubMenus.stream()
                .map(menu -> menu.getId())
                .collect(Collectors.toList());
        ArrayList<Long> longs = new ArrayList<>();
        longs.addAll(subMenusId);
        longs.addAll(fatherMenusId);
        longs.addAll(fatherSubMenusId);
        List<Long> deleteIds = longs.stream().distinct().collect(Collectors.toList());
        boolean b = this.removeByIds(deleteIds);
        if(b){
            return Result.ok("删除菜单成功");
        }else{
            return Result.ok("删除菜单失败");
        }
    }

    @Override
    public Result getFatherMenuIdByMenuId(Long id) {
        Menu byId = this.getById(id);
        QueryWrapper<Menu> menuQueryWrapper = new QueryWrapper<>();
        menuQueryWrapper.eq("id", byId.getPId());
        Menu one = this.getOne(menuQueryWrapper);
        return Result.ok(one);
    }

    @Override
    public Result getFatherMenus() {
        QueryWrapper<Menu> menuQueryWrapper = new QueryWrapper<>();
        menuQueryWrapper.isNull("p_id");
        List<Menu> list = this.list(menuQueryWrapper);
        List<MenuItem> collect = list.stream().map(menu -> new MenuItem(menu.getId(),
                menu.getName(),
                menu.getMenuName(),
                menu.getPId(),
                menu.getRedirect())).collect(Collectors.toList());
        return Result.ok(collect);
    }

    @Override
    public Result getSubMenus() {
        QueryWrapper<Menu> menuQueryWrapper = new QueryWrapper<>();
        menuQueryWrapper.isNotNull("p_id");
        List<Menu> list = this.list(menuQueryWrapper);
        List<MenuItem> collect = list.stream().map(menu -> new MenuItem(menu.getId(),
                menu.getName(),
                menu.getMenuName(),
                menu.getPId(),
                menu.getRedirect())).collect(Collectors.toList());
        return Result.ok(collect);
    }


    private List<MenuIncludeChildren> getMenuTree(List<Menu> t){
        ArrayList<MenuIncludeChildren> objects = new ArrayList<>();
        List<Menu> layout = t.stream()
                .filter(menu -> Objects.isNull(menu.getPId()))
                .sorted((o1, o2) -> o1.getSort() - o2.getSort())
                .collect(Collectors.toList());
        List<Menu> collect = t.stream().filter(menu -> menu.getPId() != null).collect(Collectors.toList());
        layout.stream().forEach(menu -> {
            MenuIncludeChildren menuIncludeChildren = new MenuIncludeChildren(menu);
            List<MenuItem> collect1 = collect
                    .stream()
                    .filter(item -> item.getPId().equals(menu.getId()))
                    .sorted((o1, o2) -> o1.getSort() - o2.getSort())
                    .map((Function<Menu, MenuItem>) menu1 -> new MenuItem(menu1.getId(), menu1.getName(),menu1.getMenuName(), menu1.getPId(), menu1.getRedirect()))
                    .collect(Collectors.toList());
            menuIncludeChildren.setChildren(collect1);
            objects.add(menuIncludeChildren);
        });
        return objects;
    }

    private List<MenuTreeIncludeChildren> getMenuTree1(List<Menu> s){
        ArrayList<MenuTreeIncludeChildren> objects = new ArrayList<>();
        List<Menu> layout = s.stream()
                .filter(menu -> Objects.isNull(menu.getPId()))
                .sorted((o1, o2) -> o1.getSort() - o2.getSort())
                .collect(Collectors.toList());
        List<Menu> collect = s.stream().filter(menu -> menu.getPId() != null).collect(Collectors.toList());
        layout.stream().forEach(menu -> {
            MenuTreeIncludeChildren menuTreeIncludeChildren = new MenuTreeIncludeChildren(menu);
            List<Menu> collect1 = collect
                    .stream()
                    .filter(item -> item.getPId().equals(menu.getId()))
                    .sorted((o1, o2) -> o1.getSort() - o2.getSort())
                    .collect(Collectors.toList());
            menuTreeIncludeChildren.setChildren(collect1);
            objects.add(menuTreeIncludeChildren);
        });
        return objects;
    }


}
