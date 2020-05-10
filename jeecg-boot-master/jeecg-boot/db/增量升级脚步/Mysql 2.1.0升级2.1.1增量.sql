

UPDATE `sys_dict_item` SET  `item_text` = '可见/可访问(授权后可见/可访问)'  WHERE `id` = '83250269359855501ec4e9c0b7e21596';


-- ----------------------------
-- Table structure for oss_file
-- ----------------------------
DROP TABLE IF EXISTS `oss_file`;
CREATE TABLE `oss_file` (
  `id` varchar(32) NOT NULL COMMENT '主键id',
  `file_name` varchar(255) default NULL COMMENT '文件名称',
  `url` varchar(255) default NULL COMMENT '文件地址',
  `create_by` varchar(32) default NULL COMMENT '创建人登录名称',
  `create_time` datetime default NULL COMMENT '创建日期',
  `update_by` varchar(32) default NULL COMMENT '更新人登录名称',
  `update_time` datetime default NULL COMMENT '更新日期',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Oss File';
INSERT INTO sys_permission(id, parent_id, name, url, component, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_route, is_leaf, keep_alive, hidden, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status) VALUES ('1166535831146504193', '2a470fc0c3954d9dbb61de6d80846549', '对象存储', '/oss/file', 'modules/oss/OSSFileList', null, null, 1, null, '1', 1, 0, '', 1, 1, 0, 0, null, 'admin', '2019-08-28 02:19:50', 'admin', '2019-08-28 02:20:57', 0, 0, '1');



ALTER TABLE sys_permission
  add COLUMN  internal_or_external  tinyint(1)
  comment '外链菜单打开方式 0/内部打开 1/外部打开';

INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1170592628746878978', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '菜单管理', '/isystem/newPermissionList', 'system/NewPermissionList', NULL, NULL, '1', NULL, '1', '100', '0', NULL, '1', '1', '0', '0', NULL, 'admin', '2019-09-08 15:00:05', 'admin', '2019-09-08 15:02:57', '0', '0', '1', '0');

ALTER TABLE `sys_permission`
ADD INDEX `index_menu_type`(`menu_type`),
ADD INDEX `index_menu_hidden`(`hidden`),
ADD INDEX `index_menu_status`(`status`);

ALTER TABLE `sys_depart`
ADD COLUMN `org_category`  varchar(10) NOT NULL DEFAULT 1 COMMENT '机构类别 1组织机构，2岗位' AFTER `description`;

INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `type`) VALUES ('1174511106530525185', '机构类型', 'org_category', '机构类型 1组织机构，2岗位', '0', 'admin', '2019-09-19 10:30:43', NULL, NULL, '0');
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1174511197735665665', '1174511106530525185', '组织机构', '1', '组织机构', '1', '1', 'admin', '2019-09-19 10:31:05', NULL, NULL);
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1174511244036587521', '1174511106530525185', '岗位', '2', '岗位', '1', '1', 'admin', '2019-09-19 10:31:16', NULL, NULL);

ALTER TABLE `sys_user`
  ADD COLUMN `work_no` varchar(100) NULL COMMENT '工号，唯一键' AFTER `activiti_sync`,
  ADD COLUMN `post` varchar(100) NULL COMMENT '职务，关联职务表' AFTER `work_no`,
  ADD COLUMN `telephone` varchar(45) NULL COMMENT '座机号' AFTER `post`,
  ADD UNIQUE INDEX `uniq_sys_user_work_no` (`work_no`);


DROP TABLE IF EXISTS `sys_position`;
CREATE TABLE `sys_position` (
  `id` varchar(32) NOT NULL,
  `code` varchar(100) DEFAULT NULL COMMENT '职务编码',
  `name` varchar(100) DEFAULT NULL COMMENT '职务名称',
  `rank` varchar(2) DEFAULT NULL COMMENT '职级',
  `company_id` varchar(255) DEFAULT NULL COMMENT '公司id',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `sys_org_code` varchar(50) DEFAULT NULL COMMENT '组织机构编码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 职务菜单
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1174506953255182338', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '职务管理', '/isystem/position', 'system/SysPositionList', '1', NULL, NULL, '1', NULL, '1', '2', '0', NULL, '1', '0', '0', NULL, 'admin', '2019-09-19 10:14:13', 'admin', '2019-09-19 10:15:22', '0', '0', '1', '0');

-- 职务职级字典
INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `type`) VALUES ('1174509082208395266', '职务职级', 'position_rank', '职务表职级字典', '0', 'admin', '2019-09-19 10:22:41', NULL, NULL, '0');
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1174509601047994369', '1174509082208395266', '员工', '1', '', '1', '1', 'admin', '2019-09-19 10:24:45', NULL, NULL);
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1174509667297026049', '1174509082208395266', '小组长', '2', '', '2', '1', 'admin', '2019-09-19 10:25:01', NULL, NULL);
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1174509713568587777', '1174509082208395266', '部门经理', '3', '', '3', '1', 'admin', '2019-09-19 10:25:12', NULL, NULL);
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1174509788361416705', '1174509082208395266', '副总经理', '4', '', '4', '1', 'admin', '2019-09-19 10:25:30', NULL, NULL);
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1174509835803189250', '1174509082208395266', '总经理', '5', '', '5', '1', 'admin', '2019-09-19 10:25:41', NULL, NULL);
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1174509861333917697', '1174509082208395266', '董事长', '6', '', '6', '1', 'admin', '2019-09-19 10:25:47', NULL, NULL);

-- 通讯录菜单
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1174590283938041857', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '通讯录', '/isystem/addressList', 'system/AddressList', '1', NULL, NULL, '1', NULL, '1', '3', '0', NULL, '1', '0', '0', NULL, 'admin', '2019-09-19 15:45:21', NULL, NULL, '0', '0', '1', '0');


DELETE FROM sys_dict_item WHERE dict_id = "1174509082208395266";
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1174509601047994369', '1174509082208395266', '员级', '1', '', '1', '1', 'admin', '2019-09-19 10:24:45', 'admin', '2019-09-23 11:46:39');
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1174509667297026049', '1174509082208395266', '助级', '2', '', '2', '1', 'admin', '2019-09-19 10:25:01', 'admin', '2019-09-23 11:46:47');
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1174509713568587777', '1174509082208395266', '中级', '3', '', '3', '1', 'admin', '2019-09-19 10:25:12', 'admin', '2019-09-23 11:46:56');
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1174509788361416705', '1174509082208395266', '副高级', '4', '', '4', '1', 'admin', '2019-09-19 10:25:30', 'admin', '2019-09-23 11:47:06');
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1174509835803189250', '1174509082208395266', '正高级', '5', '', '5', '1', 'admin', '2019-09-19 10:25:41', 'admin', '2019-09-23 11:47:12');

ALTER TABLE  sys_user
MODIFY COLUMN `sex` tinyint(1) NULL COMMENT '性别(0-默认未知,1-男,2-女)' AFTER `birthday`;

ALTER TABLE  sys_permission
MODIFY COLUMN `sort_no` double(8, 2) NULL DEFAULT NULL COMMENT '菜单排序' AFTER `perms_type`;

INSERT INTO `sys_dict`(`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `type`) VALUES ('1178295274528845826', '表单权限策略', 'form_perms_type', '', 0, 'admin', '2019-09-29 21:07:39', 'admin', '2019-09-29 21:08:26', NULL);
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1178295553450061826', '1178295274528845826', '可编辑(未授权禁用)', '2', '', 2, 1, 'admin', '2019-09-29 21:08:46', 'admin', '2019-09-29 21:09:18');
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1178295639554928641', '1178295274528845826', '可见(未授权不可见)', '1', '', 1, 1, 'admin', '2019-09-29 21:09:06', 'admin', '2019-09-29 21:09:24');

ALTER TABLE `sys_role`
  ADD UNIQUE INDEX `uniq_sys_role_role_code` (`role_code`) USING BTREE ;

INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`) VALUES ('f2849d3814fc97993bfc519ae6bbf049', 'e41b69c57a941a3bbcce45032fe57605', 'AUTO复制表单', '/online/copyform/:code', 'modules/online/cgform/OnlCgformCopyList', NULL, NULL, '1', NULL, '1', '1', '0', NULL, '1', '1', '0', '1', NULL, 'admin', '2019-08-29 16:05:37', NULL, NULL, '0', '0', '1');

ALTER TABLE `onl_cgform_head`
ADD COLUMN `copy_version`  int(11) NULL COMMENT '复制版本号' AFTER `form_template_mobile`,
ADD COLUMN `copy_type`  int(3) NULL DEFAULT 0 COMMENT '复制表类型1为复制表 0为原始表' AFTER `copy_version`,
ADD COLUMN `physic_id`  varchar(32) NULL COMMENT '原始表ID' AFTER `copy_type`;

ALTER TABLE `onl_cgform_head`
ADD COLUMN `scroll`  int(3) NULL DEFAULT 0 COMMENT '是否有横向滚动条' AFTER `form_template_mobile`;
/*
Navicat MySQL Data Transfer

Source Server         : test
Source Server Version : 50529
Source Host           : localhost:3306
Source Database       : jeecg-boot

Target Server Type    : MYSQL
Target Server Version : 50529
File Encoding         : 65001

Date: 2019-11-25 18:07:57
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sl_customer_source
-- ----------------------------
DROP TABLE IF EXISTS `sl_customer_source`;
CREATE TABLE `sl_customer_source` (
  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `content` varchar(1000) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
  `row_sts` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sl_customer_source
-- ----------------------------
INSERT INTO `sl_customer_source` VALUES ('1198854930209599489', '网络推广', 'WLTG', '网络推广test', '2019-11-25 14:44:23', '2019-11-25 14:46:30', 'admin', 'admin', null);


DROP TABLE IF EXISTS `sl_material_brand`;
CREATE TABLE `sl_material_brand` (
  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `web_site` varchar(500) DEFAULT NULL,
  `content` varchar(1000) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
  `row_sts` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `sl_material_type`;
CREATE TABLE `sl_material_type` (
  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `content` varchar(1000) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
  `row_sts` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `sl_material_unit`;
CREATE TABLE `sl_material_unit` (
  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `content` varchar(1000) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
  `row_sts` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `sl_warehouse`;
CREATE TABLE `sl_warehouse` (
  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `content` varchar(1000) DEFAULT NULL,
  `principal_id` varchar(50) DEFAULT NULL,
  `belongs_to_id` varchar(1000) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
  `row_sts` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `sl_area`;
CREATE TABLE `sl_area` (

  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `content` varchar(100) DEFAULT NULL,
  `parent_id` varchar(50) DEFAULT NULL,
  `short_name` varchar(30) DEFAULT NULL,
  `level_type` varchar(30) DEFAULT NULL,
  `city_code` varchar(30) DEFAULT NULL,
  `zip_code` varchar(30) DEFAULT NULL,
  `merge_name` varchar(30) DEFAULT NULL,
  `lng` decimal(15,4) DEFAULT NULL,
  `lat`decimal(15,4) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
  `row_sts` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `sl_customer`;
CREATE TABLE `sl_customer` (

  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `content` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
  `row_sts` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,

  `customer_type_id` varchar(50) DEFAULT NULL,
  `phone` varchar(30) DEFAULT NULL,
  `wechat_id` varchar(30) DEFAULT NULL,
  `wechat_nick_name` varchar(30) DEFAULT NULL,
  `customer_source_id` varchar(30) DEFAULT NULL,
  `member_no` varchar(30) DEFAULT NULL,
  `member_phone` varchar(30) DEFAULT NULL,
  `member_nick_name`varchar(30) DEFAULT NULL,
  `nick_name`varchar(30) DEFAULT NULL,
  `gender` int(2) DEFAULT NULL,
  `birthday` datetime DEFAULT NULL,
  `linkman` varchar(30) DEFAULT NULL,
  `tel` varchar(30) DEFAULT NULL,
  `email` varchar(30) DEFAULT NULL,
  `fax` varchar(30) DEFAULT NULL,
  `bankaccount` varchar(30) DEFAULT NULL,
  `bankacct_name` varchar(30) DEFAULT NULL,
  `bank_name` varchar(30) DEFAULT NULL,,
  `discount` decimal(10,0) DEFAULT NULL
  `discount_type_id` varchar(30) DEFAULT NULL,
  `province` varchar(30) DEFAULT NULL,
  `city` varchar(30) DEFAULT NULL,
  `district` varchar(30) DEFAULT NULL,
  `address` varchar(80) DEFAULT NULL,
  `billing_info` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `sl_customer_delivery_info`;
CREATE TABLE `sl_customer_delivery_info` (

  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `content` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
  `row_sts` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,

  `source_id` varchar(50) DEFAULT NULL,
  `cdi_source_id` varchar(50) DEFAULT NULL,
  `cdi_default_type` varchar(10) DEFAULT NULL,
  `cdi_description` varchar(30) DEFAULT NULL,
  `cdi_linkman` varchar(30) DEFAULT NULL,
  `cdi_phone` varchar(30) DEFAULT NULL,
  `cdi_car_license` varchar(30) DEFAULT NULL,
  `cdi_delivery_address` varchar(30) DEFAULT NULL,
  `cdi_province` varchar(30) DEFAULT NULL,
  `cdi_city` varchar(30) DEFAULT NULL,
  `cdi_district` varchar(30) DEFAULT NULL,
  `cdi_address` varchar(50) DEFAULT NULL,
  `cdi_logistics` varchar(30) DEFAULT NULL,
  `cdi_branch` varchar(30) DEFAULT NULL,
  `cdi_tel` varchar(30) DEFAULT NULL,
  `cdi_recipients_phone` varchar(30) DEFAULT NULL,
  `cdi_recipients` varchar(30) DEFAULT NULL,
  `bill_status` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `sl_vendor`;
CREATE TABLE `sl_vendor` (

  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `content` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
  `row_sts` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,

  `linkman` varchar(30) DEFAULT NULL,
  `tel` varchar(30) DEFAULT NULL,
  `phone` varchar(30) DEFAULT NULL,
  `email` varchar(30) DEFAULT NULL,
  `fax` varchar(30) DEFAULT NULL,
  `bankaccount` varchar(30) DEFAULT NULL,
  `bankacct_name` varchar(30) DEFAULT NULL,
  `bank_name` varchar(30) DEFAULT NULL,,
  `discount` decimal(10,0) DEFAULT NULL
  `discount_type_id` varchar(30) DEFAULT NULL,
  `billing_info` varchar(50) DEFAULT NULL,
  `web` varchar(30) DEFAULT NULL,
  `province` varchar(30) DEFAULT NULL,
  `city` varchar(30) DEFAULT NULL,
  `district` varchar(30) DEFAULT NULL,
  `address` varchar(80) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `sl_material`;
CREATE TABLE `sl_material` (

  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `content` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
  `row_sts` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,

  `specification` varchar(50) DEFAULT NULL,
  `brand_id` varchar(30) DEFAULT NULL,
  `type_id` varchar(30) DEFAULT NULL,
  `unit_id` varchar(30) DEFAULT NULL,
  `uplimit` decimal(10,0) DEFAULT NULL,
  `downlimit` decimal(10,0) DEFAULT NULL,
  `pictures` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `sl_material_price`;
CREATE TABLE `sl_material_price` (

  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `content` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
  `row_sts` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,

  `material_id` varchar(30) DEFAULT NULL,
  `customer_type_id` varchar(30) DEFAULT NULL,
  `unit_id` varchar(30) DEFAULT NULL,
  `price` decimal(10,0) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `sl_material_self_unit`;
CREATE TABLE `sl_material_self_unit` (

  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `content` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
  `row_sts` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,

  `source_id` varchar(30) DEFAULT NULL,
  `unit_id` varchar(30) DEFAULT NULL,
  `unit_type` varchar(30) DEFAULT NULL,
  `qty` decimal(10,0) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `sl_sale_order`;
CREATE TABLE `sl_sale_order` (

  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `content` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
  `row_sts` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,

  `customer_id` varchar(30) DEFAULT NULL,
  `warehouse_id` varchar(30) DEFAULT NULL,
  `channel_id` varchar(30) DEFAULT NULL,
  `qty` decimal(10,0) DEFAULT NULL,
  `delivery_time` datetime DEFAULT NULL,
  `install_time` datetime DEFAULT NULL,
  `bill_date` datetime DEFAULT NULL,
  `receipt_type` varchar(5) DEFAULT NULL,
  `billing_info` varchar(30) DEFAULT NULL,
  `bill_status` int(2) DEFAULT NULL,
  `mtlamount` decimal(10,0) DEFAULT NULL,
  `otheramount` decimal(10,0) DEFAULT NULL,
  `totalamount` decimal(10,0) DEFAULT NULL,
  `payamount` decimal(10,0) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `sl_sale_order_mtl`;
CREATE TABLE `sl_sale_order_mtl` (

  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `content` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
  `row_sts` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,

  `source_id` varchar(30) DEFAULT NULL,
  `mtl_id` varchar(30) DEFAULT NULL,
  `unit_id` varchar(30) DEFAULT NULL,
  `quantity` decimal(10,0) DEFAULT NULL,
  `price` decimal(10,0) DEFAULT NULL,
  `discount` decimal(10,0) DEFAULT NULL,
  `bill_status` int(2) DEFAULT NULL,
  `amount` decimal(10,0) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `sl_expense`;
CREATE TABLE `sl_expense` (
  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `content` varchar(1000) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
  `row_sts` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `sl_sale_order_expense`;
CREATE TABLE `sl_sale_order_expense` (

  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `content` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
  `row_sts` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,

  `source_id` varchar(30) DEFAULT NULL,
  `expense_id` varchar(30) DEFAULT NULL,
  `amount` decimal(10,0) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `sl_saleorder_delivery_info`;
CREATE TABLE `sl_saleorder_delivery_info` (

  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `content` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
  `row_sts` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,

  `cdi_source_id` varchar(50) DEFAULT NULL,
  `warehouse_id` varchar(50) DEFAULT NULL,
  `cdi_source_bill_code` varchar(50) DEFAULT NULL,
  `cdi_default_type` varchar(10) DEFAULT NULL,
  `cdi_description` varchar(30) DEFAULT NULL,
  `cdi_linkman` varchar(30) DEFAULT NULL,
  `cdi_phone` varchar(30) DEFAULT NULL,
  `cdi_car_license` varchar(30) DEFAULT NULL,
  `cdi_delivery_address` varchar(30) DEFAULT NULL,
  `cdi_province` varchar(30) DEFAULT NULL,
  `cdi_city` varchar(30) DEFAULT NULL,
  `cdi_district` varchar(30) DEFAULT NULL,
  `cdi_address` varchar(50) DEFAULT NULL,
  `cdi_logistics` varchar(30) DEFAULT NULL,
  `cdi_branch` varchar(30) DEFAULT NULL,
  `cdi_tel` varchar(30) DEFAULT NULL,
  `cdi_recipients_phone` varchar(30) DEFAULT NULL,
  `cdi_recipients` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `sl_inventory`;
CREATE TABLE `sl_inventory` (

  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `content` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
  `row_sts` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,

  `mtl_id` varchar(30) DEFAULT NULL,
  `warehouse_id` varchar(30) DEFAULT NULL,
  `stock_amount` decimal(10,0) DEFAULT NULL,
  `warningstart` decimal(10,0) DEFAULT NULL,
  `warningend` decimal(10,0) DEFAULT NULL,
  `unit_id` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `sl_stocking`;
CREATE TABLE `sl_stocking` (

  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `content` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
  `row_sts` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,

  `mtl_id` varchar(30) DEFAULT NULL,
  `warehouse_id` varchar(30) DEFAULT NULL,
  `stock_amount` decimal(10,0) DEFAULT NULL,
  `before_amount` decimal(10,0) DEFAULT NULL,
  `unit_id` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `sl_inventory_log`;
CREATE TABLE `sl_inventory_log` (

  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `content` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
  `row_sts` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,

  `source_bill_type` int(10) DEFAULT NULL,
  `source_id` varchar(30) DEFAULT NULL,
  `mtl_id` varchar(30) DEFAULT NULL,
  `warehouse_id` varchar(30) DEFAULT NULL,
  `stock_amount` decimal(10,0) DEFAULT NULL,
  `opt_amount` decimal(10,0) DEFAULT NULL,
  `before_amount` decimal(10,0) DEFAULT NULL,
  `unit_id` varchar(30) DEFAULT NULL,
  `operation_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `sl_bill_code_builder`;
CREATE TABLE `sl_bill_code_builder` (

  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `content` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
  `row_sts` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,

  `bill_type` int(11) DEFAULT NULL,
  `prefix` varchar(30) DEFAULT NULL,
  `zero_count` int(11) DEFAULT NULL,
  `date_fmt_id` int(11) DEFAULT NULL,
  `has_date` int(1) DEFAULT NULL,
  `current_level` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `sl_inventory_in`;
CREATE TABLE `sl_inventory_in` (

  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `content` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
  `row_sts` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,

  `bill_status` int(10) DEFAULT NULL,
  `bill_type` int(10) DEFAULT NULL,
  `source_code` varchar(30) DEFAULT NULL,
  `source_bill_type` int(10) DEFAULT NULL,
  `source_id` varchar(30) DEFAULT NULL,
  `warehouse_id` varchar(30) DEFAULT NULL,
  `put_in_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `sl_inventory_in_mtl`;
CREATE TABLE `sl_inventory_in_mtl` (

  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `content` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
  `row_sts` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,

  `mtl_id` varchar(30) DEFAULT NULL,
  `source_id` varchar(30) DEFAULT NULL,
  `quantity` decimal(10,0) DEFAULT NULL,
  `unit_id` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `sl_inventory_out`;
CREATE TABLE `sl_inventory_out` (

  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `content` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
  `row_sts` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,

  `bill_status` int(10) DEFAULT NULL,
  `bill_type` int(10) DEFAULT NULL,
  `source_bill_type` int(10) DEFAULT NULL,
  `source_id` varchar(30) DEFAULT NULL,
  `source_code` varchar(30) DEFAULT NULL,
  `warehouse_id` varchar(30) DEFAULT NULL,
  `put_out_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `sl_inventory_out_mtl`;
CREATE TABLE `sl_inventory_out_mtl` (

  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `content` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
  `row_sts` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,

  `mtl_id` varchar(30) DEFAULT NULL,
  `source_id` varchar(30) DEFAULT NULL,
  `quantity` decimal(10,0) DEFAULT NULL,
  `unit_id` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `sl_logistics_company`;
CREATE TABLE `sl_logistics_company` (
  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `content` varchar(1000) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
  `row_sts` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

//*************************************************202004*****************//

ALTER TABLE sl_inventory_log ADD COLUMN source_bill_id VARCHAR(30);
UPDATE sl_inventory_log SET source_bill_id = source_id;

UPDATE sl_inventory_log SET source_id=(
SELECT sin.id FROM sl_inventory_in sin WHERE sin.source_id = source_bill_id AND sin.source_bill_type = source_bill_type AND sin.row_sts=1
);
UPDATE sl_inventory_log SET source_id=(
SELECT sou.id FROM sl_inventory_out sou WHERE sou.source_id = source_bill_id AND sou.source_bill_type = source_bill_type AND sou.row_sts=1
)

//组装单

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS `sl_assemble`;
CREATE TABLE `sl_assemble` (
  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `content` varchar(1000) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
  `row_sts` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  `bill_date` datetime DEFAULT NULL,
	`mtl_id` varchar(30) DEFAULT NULL,
  `unit_id` varchar(30) DEFAULT NULL,
  `warehouse_id` varchar(30) DEFAULT NULL,
  `quantity` decimal(10,4) DEFAULT NULL,
  `price` decimal(10,4) DEFAULT NULL,
  `bill_status` int(11) DEFAULT NULL,
  `company_id` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
SET FOREIGN_KEY_CHECKS = 1;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS `sl_assemble_dtl`;
CREATE TABLE `sl_assemble_dtl` (
  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `content` varchar(1000) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
  `row_sts` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
	`source_id` varchar(30) DEFAULT NULL,
	`mtl_id` varchar(30) DEFAULT NULL,
  `unit_id` varchar(30) DEFAULT NULL,
  `quantity` decimal(10,4) DEFAULT NULL,
  `company_id` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
SET FOREIGN_KEY_CHECKS = 1;

//拆卸单

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS `sl_teardown`;
CREATE TABLE `sl_teardown` (
  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `content` varchar(1000) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
  `row_sts` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  `bill_date` datetime DEFAULT NULL,
	`mtl_id` varchar(30) DEFAULT NULL,
  `unit_id` varchar(30) DEFAULT NULL,
  `warehouse_id` varchar(30) DEFAULT NULL,
  `quantity` decimal(10,4) DEFAULT NULL,
  `bill_status` int(11) DEFAULT NULL,
  `company_id` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
SET FOREIGN_KEY_CHECKS = 1;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS `sl_teardown_dtl`;
CREATE TABLE `sl_teardown_dtl` (
  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `content` varchar(1000) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
  `row_sts` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
	`source_id` varchar(30) DEFAULT NULL,
	`mtl_id` varchar(30) DEFAULT NULL,
  `unit_id` varchar(30) DEFAULT NULL,
  `quantity` decimal(10,4) DEFAULT NULL,
  `price` decimal(10,4) DEFAULT NULL,
  `company_id` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
SET FOREIGN_KEY_CHECKS = 1;

//工单

DROP TABLE IF EXISTS `sl_work_order`;
CREATE TABLE `sl_work_order` (

  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `content` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
  `row_sts` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,

  `company_id` varchar(50) DEFAULT NULL,
  `source_id` varchar(30) DEFAULT NULL,
  `source_code` varchar(30) DEFAULT NULL,
  `source_bill_type` int(2) DEFAULT NULL,
  `bill_type` int(2) DEFAULT NULL,
  `bill_status` int(2) DEFAULT NULL,
  `warehouse_id` varchar(30) DEFAULT NULL,
  `operate_user_id` varchar(50) DEFAULT NULL,
  `operate_date` datetime DEFAULT NULL,
  `bill_date` datetime DEFAULT NULL,
  `finished_date` datetime DEFAULT NULL,
  `operate_log` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `sl_work_order_dtl`;
CREATE TABLE `sl_work_order_dtl` (

  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `content` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
  `row_sts` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,

  `company_id` varchar(50) DEFAULT NULL,
  `source_id` varchar(30) DEFAULT NULL,
  `mtl_id` varchar(30) DEFAULT NULL,
  `unit_id` varchar(30) DEFAULT NULL,
  `quantity` decimal(10,0) DEFAULT NULL,
  `price` decimal(10,0) DEFAULT NULL,
  `amount` decimal(10,0) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

// 维修单

DROP TABLE IF EXISTS `sl_repair_order`;
CREATE TABLE `sl_repair_order` (

  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `content` varchar(200) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
  `row_sts` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,

  `company_id` varchar(50) DEFAULT NULL,
  `source_id` varchar(30) DEFAULT NULL,
  `source_code` varchar(30) DEFAULT NULL,
  `source_bill_type` int(2) DEFAULT NULL,
  `bill_type` int(2) DEFAULT NULL,
  `bill_status` int(2) DEFAULT NULL,
  `customer_id` varchar(30) DEFAULT NULL,
  `operate_date` datetime DEFAULT NULL,
  `bill_date` datetime DEFAULT NULL,
  `finished_date` datetime DEFAULT NULL,
  `operate_log` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

// 换货单


DROP TABLE IF EXISTS `sl_change_order`;
CREATE TABLE `sl_change_order` (

  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `content` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
  `row_sts` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,

  `company_id` varchar(50) DEFAULT NULL,
  `source_id` varchar(30) DEFAULT NULL,
  `source_code` varchar(30) DEFAULT NULL,
  `source_bill_type` int(2) DEFAULT NULL,
  `bill_type` int(2) DEFAULT NULL,
  `bill_status` int(2) DEFAULT NULL,
  `customer_id` varchar(30) DEFAULT NULL,
  `bill_date` datetime DEFAULT NULL,
  `warehouse_id` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `sl_change_order_dtl`;
CREATE TABLE `sl_change_order_dtl` (

  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `content` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
  `row_sts` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,

  `company_id` varchar(50) DEFAULT NULL,
  `source_id` varchar(30) DEFAULT NULL,
  `mtl_id` varchar(30) DEFAULT NULL,
  `new_mtl_id` varchar(30) DEFAULT NULL,
  `unit_id` varchar(30) DEFAULT NULL,
  `quantity` decimal(10,0) DEFAULT NULL,
  `new_unit_id` varchar(30) DEFAULT NULL,
  `new_quantity` decimal(10,0) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

// 物流单据
DROP TABLE IF EXISTS `sl_logistics_order`;
CREATE TABLE `sl_logistics_order` (

  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `content` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
  `row_sts` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  `company_id` varchar(50) DEFAULT NULL,

  `source_id` varchar(30) DEFAULT NULL,
  `source_code` varchar(30) DEFAULT NULL,
  `source_bill_type` int(2) DEFAULT NULL,
  `bill_type` int(2) DEFAULT NULL,
  `bill_status` int(2) DEFAULT NULL,
  `bill_date` datetime DEFAULT NULL,
  `cdi_default_type` varchar(30) DEFAULT NULL,
  `cdi_delivery_address` varchar(30) DEFAULT NULL,
  `cdi_logistics_id` varchar(30) DEFAULT NULL,
  `cdi_logistics_no` varchar(30) DEFAULT NULL,
  `cdi_recipients_phone` varchar(30) DEFAULT NULL,
  `cdi_recipients` varchar(30) DEFAULT NULL,
  `cdi_province` varchar(30) DEFAULT NULL,
  `cdi_city` varchar(30) DEFAULT NULL,
  `cdi_district` varchar(30) DEFAULT NULL,
  `cdi_address` varchar(30) DEFAULT NULL,
  `post_code` varchar(30) DEFAULT NULL,
  `number` decimal(10,0) DEFAULT NULL,
  `total_weight` decimal(10,0) DEFAULT NULL,
  `insurance` decimal(10,0) DEFAULT NULL,
  `total_charge` decimal(10,0) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `sl_logistics_order_dtl`;
CREATE TABLE `sl_logistics_order_dtl` (

  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `content` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
  `row_sts` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,

  `company_id` varchar(50) DEFAULT NULL,
  `source_id` varchar(30) DEFAULT NULL,
  `mtl_id` varchar(30) DEFAULT NULL,
  `unit_id` varchar(30) DEFAULT NULL,
  `quantity` decimal(10,0) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `sl_purchase_batch`;
CREATE TABLE `sl_purchase_batch` (

  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `content` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
  `row_sts` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,

  `company_id` varchar(50) DEFAULT NULL

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `sl_purchase_cost`;
CREATE TABLE `sl_purchase_cost` (
  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `content` varchar(1000) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
  `mtl_id` varchar(30) DEFAULT NULL,
  `average_price` decimal(10,4) DEFAULT NULL,
  `unit_id` varchar(30) DEFAULT NULL,
  `batch_no` varchar(30)  DEFAULT NULL,
  `row_sts` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
	`company_id` varchar(30) DEFAULT NULL
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `sl_allot_dtl`;
CREATE TABLE `sl_allot_dtl` (
  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `content` varchar(1000) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
  `row_sts` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  `mtl_id` varchar(30) NOT NULL,
  `source_id` varchar(30) NOT NULL,
  `allot_amount` decimal(10,2) NOT NULL,
  `unit_id` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `sl_allot`;
CREATE TABLE `sl_allot` (
  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `content` varchar(1000) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
  `row_sts` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  `billdate` datetime DEFAULT NULL,
  `from_warehouse_id` varchar(30) DEFAULT NULL,
  `to_warehouse_id` varchar(30) DEFAULT NULL,
  `unit_id` varchar(30) DEFAULT NULL,
  `bill_status` int(3) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `sl_inventory_dtl`;
CREATE TABLE `sl_inventory_dtl` (
  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `content` varchar(1000) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
  `source_id` varchar(30) DEFAULT NULL,
  `warehouse_id` varchar(30) DEFAULT NULL,
  `batch_no` varchar(30) DEFAULT NULL,
  `unit_id` varchar(30) DEFAULT NULL,
  `mtl_id` varchar(30) DEFAULT NULL,
  `stock_amount` decimal(10,0) DEFAULT NULL,
  `available_amount` decimal(10,0) DEFAULT NULL,
  `row_sts` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `sl_inventory_opt_dtl`;
CREATE TABLE `sl_inventory_opt_dtl` (
  `id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `content` varchar(1000) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
  `source_id` varchar(30) DEFAULT NULL,
  `dtl_id` varchar(30) DEFAULT NULL,
  `opt_amount` decimal(10,0) DEFAULT NULL,
  `source_bill_type` int(11) DEFAULT NULL,
  `unit_id` varchar(30) DEFAULT NULL,
  `operation_id` int(11) DEFAULT NULL,
  `row_sts` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

alter  table sl_customer_delivery_info CHANGE cdi_logistics cdi_logistics_id  VARCHAR(30) DEFAULT NULL COMMENT '物流公司';

alter  table sl_saleorder_delivery_info CHANGE cdi_logistics cdi_logistics_id  VARCHAR(30) DEFAULT NULL COMMENT '物流公司';

ALTER TABLE `sl_purchase` ADD COLUMN `batch_no` varchar(20) DEFAULT NULL COMMENT '批次号';
ALTER TABLE `sl_inventory` ADD COLUMN `batch_no` varchar(20) DEFAULT NULL COMMENT '批次号';

alter  table sl_inventory_log  ADD COLUMN `source_bill_id` varchar(20) DEFAULT NULL COMMENT '原单ID';
ALTER TABLE `sl_inventory_log` ADD COLUMN `batch_no` varchar(20) DEFAULT NULL COMMENT '批次号';
