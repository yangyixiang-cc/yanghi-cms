package com.example.sstest.bean;

import lombok.Data;

import java.util.List;

@Data
public class MenuIncludeChildren extends MenuItem {

    private List<MenuItem> children;

    MenuIncludeChildren() {

    }

    public MenuIncludeChildren(Menu menu) {
        super(menu.getId(), menu.getName(), menu.getMenuName(), menu.getPId(), menu.getRedirect());
    }


}
