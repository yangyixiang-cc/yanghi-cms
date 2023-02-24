package com.example.sstest.bean;

import com.example.sstest.utils.Constant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItem {

    private Long id;

    private String name;

    private String color;

    public MenuItem(Long id, String name, String menuName, Long pid, String redirect){
        this.id =  id;
        this.name = menuName;
        if(Objects.isNull(pid)){
            this.color =Constant.MENU_COLOR_PINK;
        }else{
            this.color=Constant.MENU_COLOR_GREEN;
        }
    }

}
