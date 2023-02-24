package com.example.sstest.bean;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 菜单表(Menu)实体类 权限
 *
 * @author makejava
 * @since 2021-11-24 15:30:08
 */
@TableName(value="sys_menu")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Menu implements Serializable {
    private static final long serialVersionUID = -54979041104113736L;
    
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
    * 菜单名
    */
    private String menuName;
    /**
    * 路由地址
    */
    private String path;

    /**
     * 路由名
     */
    private String name;

    /**
    * 组件路径
    */
    private String component;

    /**
    * 菜单是否可见（1显示 0隐藏）
    */
    private Boolean visible;
    /**
     * 菜单状态（0正常 1停用）
     */
    private String status;
    /**
     * 是否缓存 1 缓存 0 不缓存
     */
    private Boolean cache;

    private Boolean auth;
    /**
    * 权限标识
    */
    private String perms;

    /**
    * 菜单图标
    */
    private String icon;

    /**
     * 重定向
     */
    private String redirect;

    /**
    * 备注
    */
    private String remark;

    /**
     * 父id
     */
    private Long pId;


    /*
     * 创建人的用户id
     */
    private Long createBy;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="Asia/Shanghai")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新人
     */
    private Long updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="Asia/Shanghai")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 删除标志（0代表未删除，1代表已删除）
     */
    @TableLogic
    private Integer delFlag;

    private Integer sort;

}