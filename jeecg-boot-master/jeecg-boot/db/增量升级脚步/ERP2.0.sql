
1. 部门
ALTER TABLE `sys_depart` ADD COLUMN `parent_ids` varchar(200) DEFAULT NULL COMMENT '父级ID';

INSERT INTO `jeecg-boot`.`sys_depart`(`id`, `parent_id`, `depart_name`, `depart_name_en`, `depart_name_abbr`, `depart_order`, `description`, `org_category`, `org_type`, `org_code`, `mobile`, `fax`, `address`, `memo`, `status`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `parent_ids`)
VALUES ('0', '', '平台', NULL, NULL, 0, NULL, '0', '0', 'A', NULL, NULL, NULL, NULL, NULL, '0', 'jeecg', '2019-11-02 15:19:33', NULL, NULL, NULL);

UPDATE sys_depart SET parent_id = '0' WHERE org_type = '1'

SELECT s.id,s.depart_name,s.parent_id,CONCAT('UPDATE `sys_depart` SET `parent_ids` = \'', s1.parent_ids,'/',s.parent_id, '\' WHERE id = \'', s.id, '\';'),s1.id,  s1.depart_name, s1.parent_ids, s1.parent_id FROM sys_depart s, sys_depart s1 WHERE s.parent_id = s1.id AND s.org_type='2' ORDER BY s.id ASC
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
