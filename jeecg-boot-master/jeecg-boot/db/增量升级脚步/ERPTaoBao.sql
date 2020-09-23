
DROP TABLE IF EXISTS `sl_trade`,
CREATE TABLE `sl_trade` (
  `id` varchar(30) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
	`company_id` varchar(50) DEFAULT NULL,

	`adjust_fee` varchar(30) DEFAULT NULL,
	`buyer_rate` int(1) DEFAULT 0,
  `tid` BIGINT(20) DEFAULT NULL,
  `seller_nick` varchar(30) DEFAULT NULL,
  `pic_path` varchar(60) DEFAULT NULL,
  `payment` varchar(30) DEFAULT NULL,
  `seller_rate` int(1) DEFAULT 0,
  `post_fee` varchar(30) DEFAULT NULL,
  `status` varchar(30) DEFAULT NULL,
  `shop_pick` varchar(30) DEFAULT NULL,
  `num` BIGINT(20) DEFAULT NULL,
  `num_iid` BIGINT(20) DEFAULT NULL,
  `title` varchar(30) DEFAULT NULL,
  `type` varchar(10) DEFAULT NULL,
  `price` varchar(10) DEFAULT NULL,
  `discount_fee` varchar(10) DEFAULT NULL,
  `total_fee` varchar(10) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `pay_time` datetime DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `seller_flag` BIGINT(20) DEFAULT NULL,
  `buyer_nick` varchar(30) DEFAULT NULL,
  `received_payment` varchar(10) DEFAULT NULL,
  `sign_time` varchar(30) DEFAULT NULL,
  `dispatch_time` varchar(30) DEFAULT NULL,
  `collect_time` varchar(30) DEFAULT NULL,
  `delivery_time` varchar(30) DEFAULT NULL,
  `cutoff_minutes` varchar(30) DEFAULT NULL,
  `os_date` varchar(30) DEFAULT NULL,
  `os_range` varchar(30) DEFAULT NULL,
  `es_date` varchar(30) DEFAULT NULL,
  `es_range` varchar(30) DEFAULT NULL,
  `trade_from` varchar(30) DEFAULT NULL,
  `shipping_type` varchar(30) DEFAULT NULL,
  `mark_desc` varchar(30) DEFAULT NULL,
  `credit_card_fee` varchar(30) DEFAULT NULL,
  `topic` varchar(30) DEFAULT NULL,
  `has_buyer_message` int(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8,


DROP TABLE IF EXISTS `sl_trade_order`;
CREATE TABLE `sl_trade_order` (
  `id` varchar(30) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
	`company_id` varchar(50) DEFAULT NULL,

	`adjust_fee` varchar(30) DEFAULT NULL,
	`buyer_rate` int(1) NULL DEFAULT 0,
  `cid` BIGINT(20) DEFAULT NULL,
  `collect_time` varchar(30) DEFAULT NULL,
  `cutoff_minutes` varchar(30) DEFAULT NULL,
  `delivery_time` varchar(30) DEFAULT NULL,
  `discount_fee` varchar(30) DEFAULT NULL,
  `es_time` varchar(30) DEFAULT NULL,
  `is_daixiao` int(1) default 0,
  `num` BIGINT(20) DEFAULT NULL,
  `num_iid` BIGINT(20) DEFAULT NULL,
  `oid` BIGINT(20) DEFAULT NULL,
  `oid_str` varchar(30) DEFAULT NULL,
  `order_from` varchar(30) DEFAULT NULL,
  `outer_sku_id` varchar(30) DEFAULT NULL,
  `part_mjz_discount` varchar(30) DEFAULT NULL,
  `payment` varchar(30) DEFAULT NULL,
  `pic_path` varchar(60) DEFAULT NULL,
  `price` varchar(30) DEFAULT NULL,
  `promise_service` varchar(60) DEFAULT NULL,
  `propoint` varchar(30) DEFAULT NULL,
  `refund_status` varchar(30) DEFAULT NULL,
  `seller_rate` int(1) NULL DEFAULT 0,
  `seller_type` varchar(30) DEFAULT NULL,
  `sign_time` varchar(30) DEFAULT NULL,
  `sku_id` varchar(30) DEFAULT NULL,
  `sku_properties_name` varchar(30) DEFAULT NULL,
  `status` varchar(30) DEFAULT NULL,
  `store_code` varchar(30) DEFAULT NULL,
  `timing_promise` varchar(30) DEFAULT NULL,
  `title` varchar(30) DEFAULT NULL,
  `total_fee` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `sl_print_order_assignment`;
CREATE TABLE `sl_print_order_assignment` (
  `id` varchar(30) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(30) DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL,
	`company_id` varchar(50) DEFAULT NULL,

  `tid` varchar(30) DEFAULT NULL,
  `num` BIGINT(20) DEFAULT NULL,
  `num_iid` BIGINT(20) DEFAULT NULL,

	`receiver_name` varchar(30) DEFAULT NULL,
	`receiver_country` varchar(30) DEFAULT NULL,
	`receiver_state` varchar(30) DEFAULT NULL,
	`receiver_city` varchar(30) DEFAULT NULL,
	`receiver_district` varchar(30) DEFAULT NULL,
	`receiver_town` varchar(30) DEFAULT NULL,
	`receiver_address` varchar(100) DEFAULT NULL,
	`receiver_zip` varchar(30) DEFAULT NULL,
	`receiver_mobile` varchar(30) DEFAULT NULL,
	`receiver_phone` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-------产品辅助属性-------

DROP TABLE IF EXISTS `sl_supplementary`;
CREATE TABLE `sl_supplementary`  (
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
  `company_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公司ID'
)

DROP TABLE IF EXISTS `sl_supplementary_value`;
CREATE TABLE `sl_supplementary_value`  (
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
  `company_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公司ID',
	`source_id` varchar(50) DEFAULT NULL
)

DROP TABLE IF EXISTS `sl_material_auxiliary`;
CREATE TABLE `sl_material_auxiliary`  (
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
  `company_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公司ID',
	`source_id` varchar(50) DEFAULT NULL,
	`supp_code_map` varchar(100) DEFAULT NULL,
	`supp_value_map` varchar(100) DEFAULT NULL
)

DROP TABLE IF EXISTS `sl_material_auxiliary_item`;
CREATE TABLE `sl_material_auxiliary_item`  (
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
  `company_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公司ID',
	`source_id` varchar(50) DEFAULT NULL,
  `supp_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `supp_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `supp_value_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `supp_value_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `mtl_id` varchar(50) DEFAULT NULL
)

ALTER TABLE `sl_inventory_out_mtl`
  ADD COLUMN `auxiliary_id` varchar(30) NULL COMMENT '辅助属性ID' AFTER `mtl_id`;
ALTER TABLE `sl_sale_order_mtl`
  ADD COLUMN `auxiliary_id` varchar(30) NULL COMMENT '辅助属性ID' AFTER `mtl_id`;
ALTER TABLE `sl_change_order_dtl`
  ADD COLUMN `auxiliary_id` varchar(30) NULL COMMENT '辅助属性ID' AFTER `mtl_id`;
ALTER TABLE `sl_purchase_mtl`
  ADD COLUMN `auxiliary_id` varchar(30) NULL COMMENT '辅助属性ID' AFTER `mtl_id`;
ALTER TABLE `sl_assemble`
  ADD COLUMN `auxiliary_id` varchar(30) NULL COMMENT '辅助属性ID' AFTER `mtl_id`;
ALTER TABLE `sl_assemble_dtl`
  ADD COLUMN `auxiliary_id` varchar(30) NULL COMMENT '辅助属性ID' AFTER `mtl_id`;
ALTER TABLE `sl_teardown`
  ADD COLUMN `auxiliary_id` varchar(30) NULL COMMENT '辅助属性ID' AFTER `mtl_id`;
ALTER TABLE `sl_teardown_dtl`
  ADD COLUMN `auxiliary_id` varchar(30) NULL COMMENT '辅助属性ID' AFTER `mtl_id`;
ALTER TABLE `sl_allot_dtl`
  ADD COLUMN `auxiliary_id` varchar(30) NULL COMMENT '辅助属性ID' AFTER `mtl_id`;
ALTER TABLE `sl_inventory`
  ADD COLUMN `auxiliary_id` varchar(30) NULL COMMENT '辅助属性ID' AFTER `mtl_id`;
ALTER TABLE `sl_inventory_dtl`
  ADD COLUMN `auxiliary_id` varchar(30) NULL COMMENT '辅助属性ID' AFTER `mtl_id`;
ALTER TABLE `sl_inventory_in_mtl`
  ADD COLUMN `auxiliary_id` varchar(30) NULL COMMENT '辅助属性ID' AFTER `mtl_id`;
ALTER TABLE `sl_inventory_log`
  ADD COLUMN `auxiliary_id` varchar(30) NULL COMMENT '辅助属性ID' AFTER `mtl_id`;
ALTER TABLE `sl_inventory_out`
  ADD COLUMN `auxiliary_id` varchar(30) NULL COMMENT '辅助属性ID' AFTER `mtl_id`;
ALTER TABLE `sl_inventory_out_mtl`
  ADD COLUMN `auxiliary_id` varchar(30) NULL COMMENT '辅助属性ID' AFTER `mtl_id`;
ALTER TABLE `sl_purchase_mtl`
  ADD COLUMN `auxiliary_id` varchar(30) NULL COMMENT '辅助属性ID' AFTER `mtl_id`;
ALTER TABLE `sl_purchase_return_mtl`
  ADD COLUMN `auxiliary_id` varchar(30) NULL COMMENT '辅助属性ID' AFTER `mtl_id`;
ALTER TABLE `sl_sale_order_mtl`
  ADD COLUMN `auxiliary_id` varchar(30) NULL COMMENT '辅助属性ID' AFTER `mtl_id`;
ALTER TABLE `sl_sale_order_return_mtl`
  ADD COLUMN `auxiliary_id` varchar(30) NULL COMMENT '辅助属性ID' AFTER `mtl_id`;
ALTER TABLE `sl_work_order_dtl`
  ADD COLUMN `auxiliary_id` varchar(30) NULL COMMENT '辅助属性ID' AFTER `mtl_id`;
