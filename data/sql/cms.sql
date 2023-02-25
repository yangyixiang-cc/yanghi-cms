/*
 Navicat Premium Data Transfer

 Source Server         : sh-cynosdbmysql-grp-c7ar96e0.sql.tencentcdb.com_24057
 Source Server Type    : MySQL
 Source Server Version : 50718
 Source Host           : sh-cynosdbmysql-grp-c7ar96e0.sql.tencentcdb.com:24057
 Source Schema         : cms

 Target Server Type    : MySQL
 Target Server Version : 50718
 File Encoding         : 65001

 Date: 23/02/2023 21:11:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `menu_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'NULL' COMMENT '菜单名',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '#' COMMENT '菜单图标',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由地址',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由名',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件路径',
  `redirect` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '重定向',
  `visible` int(11) NULL DEFAULT 1 COMMENT '菜单状态（1显示 0隐藏）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `cache` int(11) NULL DEFAULT 1 COMMENT '是否缓存 1缓存 0隐藏',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限标识',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `create_by` bigint(20) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_by` bigint(20) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `del_flag` int(11) NULL DEFAULT 0 COMMENT '是否删除（0未删除 1已删除）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `p_id` bigint(20) NULL DEFAULT NULL COMMENT '父菜单id\r\n',
  `auth` int(11) NULL DEFAULT 1 COMMENT '是否需要认证 1 认证',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1628362438238752771 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '概览', 'EyeOutlined', 'overview', 'overview', 'Overview', NULL, 1, '0', 1, 'system:test:list', 1, NULL, '2023-02-07 11:53:31', NULL, '2023-02-23 05:42:21', 0, NULL, NULL, 1);
INSERT INTO `sys_menu` VALUES (2, '系统管理', 'SecurityScanOutlined', 'permission_manager', 'layout', 'Layout', '/layout', 1, '0', 1, 'system:test:list', 4, NULL, '2023-02-08 11:53:34', NULL, NULL, 0, NULL, NULL, 1);
INSERT INTO `sys_menu` VALUES (3, '角色管理', 'UsbOutlined', 'role_manager', 'roleManager', 'RoleManager', NULL, 1, '0', 1, 'system:test:list', 2, NULL, '2023-02-09 11:53:36', NULL, NULL, 0, NULL, 2, 1);
INSERT INTO `sys_menu` VALUES (4, '系统用户', 'TeamOutlined', 'system_user', 'systemUser', 'SystemUser', NULL, 1, '0', 1, 'system:test:list', 1, NULL, '2023-03-03 11:53:38', NULL, NULL, 0, NULL, 2, 1);
INSERT INTO `sys_menu` VALUES (5, '菜单管理', 'MenuOutlined', 'menu_manager', 'menuManager', 'MenuManager', NULL, 1, '0', 1, 'system:test:list', 3, NULL, '2023-02-04 11:53:40', NULL, NULL, 0, NULL, 2, 1);
INSERT INTO `sys_menu` VALUES (6, '内容集合', 'AppstoreOutlined', 'content_collections', 'layout', 'Layout', '/layout', 0, '0', 1, 'system:test:list', 3, NULL, '2023-02-02 11:53:42', NULL, '2023-02-23 05:42:39', 0, NULL, NULL, 1);
INSERT INTO `sys_menu` VALUES (7, '文章管理', '#', 'article_manager', 'articleManager', 'ArticleManager', NULL, 1, '0', 1, 'system:test:list', 1, NULL, '2023-02-21 11:53:44', NULL, NULL, 0, NULL, 6, 1);
INSERT INTO `sys_menu` VALUES (8, '评论管理', '#', 'comments_manager', 'commentsManager', 'CommentsManager', NULL, 1, '0', 1, 'system:test:list', 2, NULL, '2023-02-18 11:53:47', NULL, NULL, 0, NULL, 6, 1);
INSERT INTO `sys_menu` VALUES (9, '系统信息', 'DesktopOutlined', 'system_setting', 'systemSetting', 'SystemSetting', NULL, 1, '0', 1, 'system:test:list', 5, NULL, '2023-02-03 11:53:49', NULL, '2023-02-23 04:06:14', 0, NULL, NULL, 1);
INSERT INTO `sys_menu` VALUES (10, '内容模型', 'AppstoreAddOutlined', 'content_model', 'contentModel', 'Layout', '/layout', 0, '0', 1, 'system:test:list', 2, NULL, '2023-02-24 11:53:51', NULL, '2023-02-23 05:42:31', 0, NULL, NULL, 1);
INSERT INTO `sys_menu` VALUES (1628362438238752770, '系统监控', 'DashboardOutlined', 'system_monitoring', 'systemMonitoring', 'SystemMonitoring', NULL, 1, '0', 1, 'system:test:list', 1, NULL, '2023-02-22 11:53:53', NULL, '2023-02-22 12:00:16', 0, NULL, 9, 1);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `role_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色权限字符串',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '角色状态（0正常 1停用）',
  `del_flag` int(11) NULL DEFAULT 0 COMMENT 'del_flag',
  `create_by` bigint(20) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_by` bigint(20) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '系统管理员', 'manager', '0', 0, NULL, '2023-02-15 14:22:10', NULL, '2023-02-22 13:24:52', '系统管理员');
INSERT INTO `sys_role` VALUES (2, '游客', 'visitor', '0', 0, NULL, '2023-02-17 14:22:12', NULL, '2023-02-21 07:00:45', '浏览用户');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` bigint(20) NOT NULL,
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NULL DEFAULT 0 COMMENT '菜单id',
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `em_role_key`(`role_id`) USING BTREE,
  INDEX `me_menu_key`(`menu_id`) USING BTREE,
  CONSTRAINT `em_role_key` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `me_menu_key` FOREIGN KEY (`menu_id`) REFERENCES `sys_menu` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (1, 1, 1, '2023-02-18 13:09:27');
INSERT INTO `sys_role_menu` VALUES (2, 1, 2, '2023-02-18 13:09:58');
INSERT INTO `sys_role_menu` VALUES (3, 1, 3, '2023-02-18 13:10:21');
INSERT INTO `sys_role_menu` VALUES (4, 1, 4, '2023-02-15 14:55:16');
INSERT INTO `sys_role_menu` VALUES (5, 1, 5, '2023-02-08 14:55:18');
INSERT INTO `sys_role_menu` VALUES (6, 1, 6, '2023-02-21 14:55:20');
INSERT INTO `sys_role_menu` VALUES (7, 1, 7, '2023-02-08 14:55:22');
INSERT INTO `sys_role_menu` VALUES (8, 1, 8, '2023-02-01 14:55:24');
INSERT INTO `sys_role_menu` VALUES (9, 1, 9, '2023-03-04 14:55:27');
INSERT INTO `sys_role_menu` VALUES (10, 2, 1, '2023-02-07 14:55:30');
INSERT INTO `sys_role_menu` VALUES (11, 2, 6, '2023-02-01 14:55:35');
INSERT INTO `sys_role_menu` VALUES (12, 2, 9, '2023-02-07 14:55:33');
INSERT INTO `sys_role_menu` VALUES (13, 1, 10, NULL);
INSERT INTO `sys_role_menu` VALUES (1628385333564301313, 1, 1628362438238752770, '2023-02-22 13:24:52');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'NULL' COMMENT '用户名',
  `nick_name` varchar(64) CHARACTER SET utf16 COLLATE utf16_general_ci NOT NULL DEFAULT 'NULL' COMMENT '昵称',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'NULL' COMMENT '密码',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '账号状态（0正常 1停用）',
  `email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phonenumber` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `sex` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户性别（0男，1女，2未知）',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `user_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '用户类型（0管理员，1普通用户）',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建人的用户id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` int(11) NULL DEFAULT 0 COMMENT '删除标志（0代表未删除，1代表已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1627559506534596610 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1548991108706627585, 'yanghi', 'long', '$2a$10$QNCeUvpIii290FTgBlQzle48OrVE851qQ7jEc1dMVe6L2NlnaYjIq', '0', '346278@163.com', '15539702885', '男', 'http://localhost:8888/img/vue-color-avatar.png', '1', NULL, '2023-02-14 15:20:08', NULL, '2023-02-24 15:20:11', 1);
INSERT INTO `sys_user` VALUES (1591087081907552258, 'test', 'dasd', '$2a$10$DWL0NpZ1yrEufUuFE9FqNOVGiqtqNJe1F6sssl4sBUuakAUHdLZ.S', '0', '346278@163.com', '15539702885', '男', 'http://localhost:8888/img/a1.png', '1', NULL, '2023-02-18 15:20:14', NULL, '2023-03-30 15:20:17', 1);
INSERT INTO `sys_user` VALUES (1627483864552165378, 'yangyihi', 'yanghi', '$2a$10$p/gOWDZCWPV28.be/pjE/uPNF2ZxsvFFmOB.ULYMhg9NjkIWZ8/N6', '0', '19198943439@qq.com', '17274195876', '女', 'https://yanghi-save-1312993591.cos.ap-nanjing.myqcloud.com/img/2023/1/23/bd6e1eb1-e573-4c0f-8eed-dd95f19ef50d.png', '1', NULL, '2023-02-20 01:42:45', NULL, '2023-02-23 05:31:30', 0);
INSERT INTO `sys_user` VALUES (1627486805531000834, 'test12', 'test12', '$2a$10$l5.F5Te.i1kK0Jy1FLoRdekiIvMUGqL3GMZXE45TftIohJOdRZ6Ku', '0', 'workyyx@163.com', NULL, '男', 'https://yanghi-save-1312993591.cos.ap-nanjing.myqcloud.com/img/2023/1/20/d7d553f8-d8c6-449b-8cbe-515d2dd13a16.png', '1', NULL, '2023-02-08 01:54:26', NULL, '2023-02-18 04:29:33', 1);
INSERT INTO `sys_user` VALUES (1627559506534596609, 'yangyihi20', 'test', '$2a$10$P7F.c9mNOxnN0r/JUcBIMuGfijsPFku9k6q6f8UuSFesO/iNlh5e6', '0', 'workyyx@163.com', '17274195876', '女', 'https://yanghi-save-1312993591.cos.ap-nanjing.myqcloud.com/img/2023/1/20/2ab737fd-8a69-492b-be90-5e46b9386529.png', '1', NULL, '2023-02-16 06:43:20', NULL, '2023-02-16 09:13:27', 0);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `role_id` bigint(20) NULL DEFAULT 0 COMMENT '角色id',
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ur_user_key`(`user_id`) USING BTREE,
  INDEX `ur_role_key`(`role_id`) USING BTREE,
  CONSTRAINT `ur_user_key` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `ur_role_key` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1627483864552165378, 1, '2023-01-04 13:11:00');

SET FOREIGN_KEY_CHECKS = 1;
