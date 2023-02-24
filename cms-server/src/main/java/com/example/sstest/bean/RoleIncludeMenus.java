package com.example.sstest.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleIncludeMenus extends Role {

    private String[] chanceMenus;


}
