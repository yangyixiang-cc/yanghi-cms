package com.example.sstest.bean;

import lombok.Data;

import java.util.List;
@Data
public class MenuTreeIncludeChildren extends Menu{

    private List<Menu> children;

    public MenuTreeIncludeChildren(){

    }

    public MenuTreeIncludeChildren(Menu menu){
        super(
                menu.getId(),
                menu.getMenuName(),
                menu.getPath(),
                menu.getName(),
                menu.getComponent(),
                menu.getVisible(),
                menu.getStatus(),
                menu.getCache(),
                menu.getAuth(),
                menu.getPerms(),
                menu.getIcon(),
                menu.getRedirect(),
                menu.getRemark(),
                menu.getPId(),
                menu.getCreateBy(),
                menu.getCreateTime(),
                menu.getUpdateBy(),
                menu.getUpdateTime(),
                menu.getDelFlag(),
                menu.getSort()
        );
    }

    public MenuTreeIncludeChildren(Menu menu, List<Menu> children){
        super(
                menu.getId(),
                menu.getMenuName(),
                menu.getPath(),
                menu.getName(),
                menu.getComponent(),
                menu.getVisible(),
                menu.getStatus(),
                menu.getCache(),
                menu.getAuth(),
                menu.getPerms(),
                menu.getIcon(),
                menu.getRedirect(),
                menu.getRemark(),
                menu.getPId(),
                menu.getCreateBy(),
                menu.getCreateTime(),
                menu.getUpdateBy(),
                menu.getUpdateTime(),
                menu.getDelFlag(),
                menu.getSort()
        );
        this.setChildren(children);
    }


}
