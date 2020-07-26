ALTER TABLE `sl_customer`
	ADD COLUMN `bill_title` varchar(30) DEFAULT NULL	COMMENT '抬头' AFTER `discount_type_id`,
	ADD COLUMN `tax_no` varchar(30) DEFAULT NULL COMMENT '税号' AFTER `bill_title`,
	DROP COLUMN `bankacct_name`/**,
	DROP COLUMN `province`,
	DROP COLUMN `city`,
	DROP COLUMN `district`,
	DROP COLUMN `bankacct_name`,
	DROP COLUMN `billing_Info`**/;


DROP TABLE IF EXISTS `sl_address`;
CREATE TABLE `sl_address` (
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
	`company_id` varchar(50) DEFAULT NULL,
	`source_id` varchar(50) DEFAULT NULL,
  `type_id` int(2) DEFAULT NULL,
	`province` varchar(50) DEFAULT NULL,
	`city` varchar(50) DEFAULT NULL,
	`district` varchar(50) DEFAULT NULL,
	`tel` varchar(50) DEFAULT NULL,
	`recipients` varchar(50) DEFAULT NULL,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `sl_sale_order`
	ADD COLUMN `delivery_type` int(2) DEFAULT NULL,
	ADD COLUMN `receipt_status` int(2) DEFAULT NULL,
	ADD COLUMN `bill_type` int(2) DEFAULT NULL,
  ADD CoLUMN `measuring_time` datetime DEFAULT NULL,
	ADD COLUMN `saleman_id` varchar(50) DEFAULT NULL COMMENT '销售员',
	DROP COLUMN `billing_info`/**,
	DROP COLUMN `warehouse_id`,
	DROP COLUMN `receipt_type`,
	DROP COLUMN `billing_info`**/;

DROP TABLE IF EXISTS `sl_receipt_order`;
CREATE TABLE `sl_receipt_order` (
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
	`company_id` varchar(50) DEFAULT NULL,
	`source_id` varchar(50) DEFAULT NULL,

  `source_bill_type` int(2) DEFAULT NULL,
	`source_bill_code` varchar(50) DEFAULT NULL,
	`payer_id` varchar(50) DEFAULT NULL,
	`amount` decimal(10,4) DEFAULT NULL,
	`bill_status_id` int(2) DEFAULT NULL,
	`saleman_id` varchar(50) DEFAULT NULL,
  `bill_date` datetime DEFAULT NULL,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `sl_receipt_order_dtl`;
CREATE TABLE `sl_receipt_order_dtl` (
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
	`company_id` varchar(50) DEFAULT NULL,
	`source_id` varchar(50) DEFAULT NULL,

	`source_bill_id` varchar(50) DEFAULT NULL,
  `source_bill_type` int(2) DEFAULT NULL,
	`source_bill_code` varchar(50) DEFAULT NULL,
	`pay_amount` decimal(10,4) DEFAULT NULL,
	`pay_type` int(2) DEFAULT NULL,
	`expense_id` varchar(50) DEFAULT NULL,
  `pay_date` datetime DEFAULT NULL,
	`receiver_id` varchar(50) DEFAULT NULL,
	`source_code` varchar(50) DEFAULT NULL,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `sl_sale_order_address`;
CREATE TABLE `sl_sale_order_address` (
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
	`company_id` varchar(50) DEFAULT NULL,
	`source_id` varchar(50) DEFAULT NULL,
	`source_add_id` varchar(50) DEFAULT NULL,
  `type_id` int(2) DEFAULT NULL,
	`province` varchar(50) DEFAULT NULL,
	`city` varchar(50) DEFAULT NULL,
	`district` varchar(50) DEFAULT NULL,
	`tel` varchar(50) DEFAULT NULL,
	`recipients` varchar(50) DEFAULT NULL,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


ALTER TABLE `sl_inventory_log`
  ADD CoLUMN `operate_time` datetime DEFAULT NULL;

ALTER TABLE `sl_work_order`
	DROP COLUMN `warehouse_id`,
  ADD CoLUMN `work_type_id` int(2) DEFAULT NULL;



UPDATE sl_logistics_order SET cdi_default_type = 3 WHERE cdi_default_type = 'WULIU';
ALTER TABLE  sl_logistics_order
CHANGE COLUMN `cdi_default_type` `delivery_type_id` int(2) NULL COMMENT '发货方式' AFTER `bill_date`,
CHANGE COLUMN `cdi_delivery_address` `delivery_address` VARCHAR(30) NULL,
CHANGE COLUMN `cdi_logistics_id` `logistics_id` VARCHAR(50) NULL,
CHANGE COLUMN `cdi_logistics_no` `logistics_no` VARCHAR(30) NULL,
CHANGE COLUMN `cdi_recipients_phone` `recipients_phone` VARCHAR(30) NULL,
CHANGE COLUMN `cdi_recipients` `recipients` VARCHAR(30) NULL,
CHANGE COLUMN `cdi_province` `province` VARCHAR(30) NULL,
CHANGE COLUMN `cdi_city` `city` VARCHAR(30) NULL,
CHANGE COLUMN `cdi_district` `district` VARCHAR(30) NULL,
CHANGE COLUMN `cdi_address` `address` VARCHAR(50) NULL,
CHANGE COLUMN `delivery_type` `delivery_type_id` int(2) NULL
