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
