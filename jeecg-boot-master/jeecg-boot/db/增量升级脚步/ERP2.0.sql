
1. 部门
ALTER TABLE `sys_depart` ADD COLUMN `parent_ids` varchar(200) DEFAULT NULL COMMENT '父级ID';

INSERT INTO `jeecg-boot`.`sys_depart`(`id`, `parent_id`, `depart_name`, `depart_name_en`, `depart_name_abbr`, `depart_order`, `description`, `org_category`, `org_type`, `org_code`, `mobile`, `fax`, `address`, `memo`, `status`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `parent_ids`)
VALUES ('0', '', '平台', NULL, NULL, 0, NULL, '0', '0', 'A', NULL, NULL, NULL, NULL, NULL, '0', 'jeecg', '2019-11-02 15:19:33', NULL, NULL, '');

UPDATE sys_depart SET parent_id = '0' WHERE org_type = '1'

SELECT s.id,s.depart_name,s.parent_id,CONCAT('UPDATE `sys_depart` SET `parent_ids` = \'', s1.parent_ids,'/',s.parent_id, '/\' WHERE id = \'', s.id, '\';'),s1.id,  s1.depart_name, s1.parent_ids, s1.parent_id FROM sys_depart s, sys_depart s1 WHERE s.parent_id = s1.id AND s.org_type='1' ORDER BY s.id ASC

//获取数据再处理

2. 用户
ALTER TABLE `sys_user` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';

3. 菜单、权限
ALTER TABLE `sys_permission` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';
ALTER TABLE `sys_permission` ADD COLUMN `pcode` varchar(50) DEFAULT NULL COMMENT '权限标识';
ALTER TABLE `sys_permission_data_rule` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';
UPDATE `sys_permission` SET company_id = '0' WHERE menu_type != 2

4. 销售订单
ALTER TABLE `sl_sale_order` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';

5. 销售订单
ALTER TABLE `sl_sale_order_return` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';

6. 角色菜单权限
ALTER TABLE `sys_role_permission` ADD COLUMN `permission_type` int(1) DEFAULT NULL COMMENT '类型ID';
ALTER TABLE `sys_role` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';

7. 基础数据
ALTER TABLE `sl_customer` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';
ALTER TABLE `sl_customer_delivery_info` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';
ALTER TABLE `sl_customer_source` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';
ALTER TABLE `sl_customer_type` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';
ALTER TABLE `sl_expense` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';
ALTER TABLE `sl_logistics_company` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';
ALTER TABLE `sl_material` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';
ALTER TABLE `sl_material_brand` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';
ALTER TABLE `sl_material_price` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';
ALTER TABLE `sl_material_self_unit` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';
ALTER TABLE `sl_material_type` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';
ALTER TABLE `sl_material_unit` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';
ALTER TABLE `sl_vendor` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';
ALTER TABLE `sl_warehouse` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';
ALTER TABLE `sl_allot` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';
ALTER TABLE `sl_allot_dtl` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';
ALTER TABLE `sl_inventory` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';
ALTER TABLE `sl_inventory_dtl` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';
ALTER TABLE `sl_inventory_in` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';
ALTER TABLE `sl_inventory_in_mtl` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';
ALTER TABLE `sl_inventory_log` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';
ALTER TABLE `sl_inventory_opt_dtl` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';
ALTER TABLE `sl_inventory_out` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';
ALTER TABLE `sl_inventory_out_mtl` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';
ALTER TABLE `sl_purchase` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';
ALTER TABLE `sl_purchase_mtl` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';
ALTER TABLE `sl_purchase_return` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';
ALTER TABLE `sl_purchase_return_mtl` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';
ALTER TABLE `sl_saleorder_delivery_info` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';
ALTER TABLE `sl_sale_order_mtl` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';
ALTER TABLE `sl_sale_order_return_mtl` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';
ALTER TABLE `sl_stocking` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';
ALTER TABLE `sl_bill_code_builder` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';
ALTER TABLE `sl_logistics_order` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';
ALTER TABLE `sys_announcement` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';
ALTER TABLE `sys_depart` ADD COLUMN `avatar` varchar(255) DEFAULT NULL COMMENT 'LOGO';
ALTER TABLE `sl_sale_order_mtl` ADD COLUMN `transaction_price` decimal(10) DEFAULT NULL COMMENT '成交价';

ALTER TABLE `sl_saleorder_delivery_info` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';
ALTER TABLE `sl_sale_order_expense` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';
ALTER TABLE `sl_sale_order_return_expense` ADD COLUMN `company_id` varchar(50) DEFAULT NULL COMMENT '企业ID';

SELECT * FROM `sys_depart` WHERE depart_name LIKE '%文疆%'

UPDATE `sys_user` SET company_id = '0' WHERE username = 'admin'
UPDATE `sys_user` SET platform_flag = 1 WHERE username = 'admin'


UPDATE `sys_user` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE platform_flag IS null;
UPDATE `sys_user` SET platform_flag = 0 WHERE platform_flag IS null;

UPDATE `sl_bill_code_builder` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;


UPDATE `sys_role` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;


----------------------------------------

SELECT * FROM `sys_user` WHERE username = 'admin'

 SELECT * FROM sys_user_role t WHERE user_id = 'e9ca23d68d884d4ebb19d07889727dae'

 SELECT * FROM sys_role WHERE id = '1266720058411782146'

 SELECT * FROM sys_role_permission WHERE role_id = '1266720058411782146'

 SELECT CONCAT('INSERT INTO sys_role_permission (id,role_id,permission_id,permission_type) VALUES (\'',p.id,'\',\'1266720058411782146\',\'',p.id,'\',',0,');')
		   FROM  sys_permission p
		   WHERE NOT exists(
		   		select a.id from sys_role_permission a
		   		join sys_role b on a.role_id = b.id
		   		join sys_user_role c on c.role_id = b.id
		   		join sys_user d on d.id = c.user_id
		   		where p.id = a.permission_id AND d.username = 'admin'
		   )
		   and p.del_flag = 0
		   order by p.sort_no ASC
------------------------------------------

UPDATE `sys_role` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;

UPDATE `sl_logistics_order` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;
UPDATE `sl_logistics_order_dtl` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;

UPDATE `sl_assemble` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;
UPDATE `sl_assemble_dtl` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;
UPDATE `sl_teardown` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;
UPDATE `sl_teardown_dtl` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;
UPDATE `sl_repair_order` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;
UPDATE `sl_change_order` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;
UPDATE `sl_change_order_dtl` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;



UPDATE `sl_work_order` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;
UPDATE `sl_work_order_dtl` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;
UPDATE `sl_stocking` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;
UPDATE `sl_inventory` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;
UPDATE `sl_inventory_out` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;
UPDATE `sl_inventory_out_mtl` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;
UPDATE `sl_inventory_in` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;
UPDATE `sl_inventory_in_mtl` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;
UPDATE `sl_allot` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;
UPDATE `sl_allot_dtl` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;



UPDATE `sl_material_price` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;
UPDATE `sl_purchase_cost` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;
UPDATE `sl_purchase` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;
UPDATE `sl_purchase_mtl` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;
UPDATE `sl_purchase_return` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;
UPDATE `sl_purchase_return_mtl` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;
UPDATE `sl_sale_order` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;
UPDATE `sl_sale_order_mtl` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;
UPDATE `sl_sale_order_return` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;
UPDATE `sl_sale_order_return_mtl` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;
UPDATE `sl_sale_order_expense` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;
UPDATE `sl_sale_order_return_expense` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;
UPDATE `sl_saleorder_delivery_info` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;




UPDATE `sl_logistics_company` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;
UPDATE `sl_expense` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;
UPDATE `sl_purchase_batch` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;
UPDATE `sl_warehouse` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;
UPDATE `sl_material` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;
UPDATE `sl_material_type` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;
UPDATE `sl_material_unit` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;
UPDATE `sl_material_brand` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;
UPDATE `sl_material_self_unit` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;
UPDATE `sl_vendor` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;
UPDATE `sl_customer` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;
UPDATE `sl_customer_type` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;
UPDATE `sl_customer_source` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;
UPDATE `sl_customer_delivery_info` SET company_id = '2d9c18e2454e4708a23e1a80f089332d' WHERE company_id IS null;


-- ----------------------------
-- 新增统计表
-- ----------------------------
DROP TABLE IF EXISTS `sl_report`;
CREATE TABLE `sl_report`  (
  `id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `content` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `create_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `row_sts` int(11) NULL DEFAULT NULL,
  `sort` int(11) NULL DEFAULT NULL,
  `pageType` int(11) NULL DEFAULT NULL COMMENT '归属页面',
  `showType` int(11) NULL DEFAULT NULL COMMENT '展示方式',
  `querySql` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '查询sql语句',
  `resultBean` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接收查询结果的模型'
)
-- ----------------------------
-- 新增公司配置统计表
-- ----------------------------
DROP TABLE IF EXISTS `sl_report_for_company`;
CREATE TABLE `sl_report_for_company`  (
  `id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `content` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `create_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `row_sts` int(11) NULL DEFAULT NULL,
  `sort` int(11) NULL DEFAULT NULL,
  `report_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报表ID',
  `company_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公司ID'
)