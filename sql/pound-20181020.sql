/*
Navicat MySQL Data Transfer

Source Server         : mysql-local
Source Server Version : 50723
Source Host           : localhost:3306
Source Database       : pound

Target Server Type    : MYSQL
Target Server Version : 50723
File Encoding         : 65001

Date: 2018-10-20 17:25:14
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for inspection
-- ----------------------------
DROP TABLE IF EXISTS `inspection`;
CREATE TABLE `inspection` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `pl_id` int(11) unsigned DEFAULT NULL COMMENT '过磅单id',
  `pl_no` varchar(20) NOT NULL DEFAULT '' COMMENT '过磅单号',
  `insp_no` varchar(20) NOT NULL DEFAULT '' COMMENT '报检单号',
  `plate_no` varchar(20) NOT NULL COMMENT '车牌号',
  `goods_kind` int(4) NOT NULL DEFAULT '0' COMMENT '货品类型',
  `comp_name` varchar(30) NOT NULL DEFAULT '' COMMENT '供货商',
  `insp_weight` double(16,4) NOT NULL DEFAULT '0.0000' COMMENT '报价单重量',
  `return_weight` double(16,4) DEFAULT '0.0000' COMMENT '随车退重量',
  `types` int(4) NOT NULL DEFAULT '0' COMMENT '类型：是否样品',
  `status` int(4) NOT NULL DEFAULT '0' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `UK_pl_no` (`pl_no`) USING BTREE,
  KEY `UK_insp_no` (`insp_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of inspection
-- ----------------------------
INSERT INTO `inspection` VALUES ('19', null, '201810131647', '2018-10-13-16-48', '浙J8888', '3', '伟星管业', '1500.0000', '20.0000', '0', '0', '2018-10-13 16:49:12', '2018-10-15 14:14:36');
INSERT INTO `inspection` VALUES ('20', null, '201810131655', '2018-10-13-16-57', '浙B684H', '1', '星星冷链', '2000.0000', '0.0000', '1', '0', '2018-10-13 16:58:16', '2018-10-15 16:43:33');
INSERT INTO `inspection` VALUES ('21', null, '201810131655', '2018-10-13-16-58', '浙B684H', '3', '星星冷链', '200.0000', '0.0000', '0', '0', '2018-10-13 16:59:04', '2018-10-16 09:09:43');
INSERT INTO `inspection` VALUES ('22', null, '201810131647', '2018-10-15-10-18', '浙J8888', '1', '伟星管业1', '1500.0000', '0.0000', '0', '0', '2018-10-15 10:18:41', '2018-10-17 09:23:39');
INSERT INTO `inspection` VALUES ('23', null, '201810131101', '2018-10-15-13-16', '浙J123C', '2', '华为铝业', '800.0000', '120.0000', '1', '0', '2018-10-15 13:07:41', '2018-10-15 13:48:53');
INSERT INTO `inspection` VALUES ('24', null, '201810131655', '2018-10-15-13-33', '浙B684H', '2', '星星冷链', '900.0000', '300.0000', '0', '0', '2018-10-15 13:33:48', '2018-10-16 10:04:49');
INSERT INTO `inspection` VALUES ('25', null, '201810131647', '2018-10-16-16-46', '浙J8888', '1', '伟星管业', '500.0000', null, '0', '0', '2018-10-16 13:47:26', null);
INSERT INTO `inspection` VALUES ('26', null, '20181016161428', 'M2N0677004', '1006612020345396', '2', 'RCV', '123.0000', '123.0000', '0', '0', '2018-10-18 17:23:44', null);

-- ----------------------------
-- Table structure for pound_info
-- ----------------------------
DROP TABLE IF EXISTS `pound_info`;
CREATE TABLE `pound_info` (
  `id` int(5) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `pound_name` varchar(20) NOT NULL DEFAULT '' COMMENT '地磅名',
  `unit_id` int(5) NOT NULL COMMENT '所属单位',
  `model` varchar(100) DEFAULT NULL COMMENT '型号',
  `remark` varchar(200) DEFAULT '' COMMENT '备注',
  `status` int(4) NOT NULL DEFAULT '0' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pound_info
-- ----------------------------
INSERT INTO `pound_info` VALUES ('1', '总部东大门地方-1', '1000', 'Xk3190-A9+', null, '1', '2018-09-25 15:53:21');
INSERT INTO `pound_info` VALUES ('2', '总部东大门地方-2', '1000', 'Xk3190-A9+', null, '0', '2018-09-25 16:00:24');
INSERT INTO `pound_info` VALUES ('3', '总部西大门地方-3', '1003', 'Xk3190-A9+3', null, '1', '2018-09-25 16:07:02');
INSERT INTO `pound_info` VALUES ('4', '总部东大门地方-4', '1004', 'Xk3190-A9+4', null, '1', '2018-09-25 16:07:02');
INSERT INTO `pound_info` VALUES ('5', '总部南大门地方-5', '1005', 'Xk3190-A9+5', null, '1', '2018-09-25 16:07:02');
INSERT INTO `pound_info` VALUES ('6', '总部北大门地方-6', '1006', 'Xk3190-A9+6', null, '1', '2018-09-25 16:07:02');
INSERT INTO `pound_info` VALUES ('7', '总部南大门地方-7', '1007', 'Xk3190-A9+7', null, '1', '2018-09-25 16:07:02');
INSERT INTO `pound_info` VALUES ('8', '总部南大门地方-8', '1008', 'Xk3190-A9+8', null, '1', '2018-09-25 16:07:02');
INSERT INTO `pound_info` VALUES ('9', '总部南大门地方-9', '1009', 'Xk3190-A9+9', null, '0', '2018-09-25 16:07:02');
INSERT INTO `pound_info` VALUES ('10', '总部西大门地方-10', '1010', 'Xk3190-A9+10', null, '1', '2018-09-25 16:07:02');
INSERT INTO `pound_info` VALUES ('11', '总部西大门地方-11', '1011', 'Xk3190-A9+11', null, '1', '2018-09-25 16:07:02');
INSERT INTO `pound_info` VALUES ('12', '总部南大门地方-12', '1012', 'Xk3190-A9+12', null, '0', '2018-09-25 16:07:02');
INSERT INTO `pound_info` VALUES ('13', '总部东大门地方-13', '1013', 'Xk3190-A9+13', null, '0', '2018-09-25 16:07:02');

-- ----------------------------
-- Table structure for pound_log
-- ----------------------------
DROP TABLE IF EXISTS `pound_log`;
CREATE TABLE `pound_log` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `pound_id` int(11) unsigned NOT NULL COMMENT '地磅id',
  `pound_name` varchar(50) DEFAULT '' COMMENT '地磅名',
  `pound_log_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '过磅单号',
  `gross_weight` double(16,4) DEFAULT '0.0000' COMMENT '毛重',
  `tare_weight` double(16,4) DEFAULT '0.0000' COMMENT '皮重',
  `net_weight` double(16,4) DEFAULT '0.0000' COMMENT '净重',
  `sample_net_weight` double(16,4) DEFAULT '0.0000' COMMENT '样品净重',
  `return_weight_total` double(16,4) DEFAULT '0.0000' COMMENT '随车退货重量',
  `diff_weight` double(16,4) DEFAULT '0.0000' COMMENT '磅差',
  `gross_img_url` varchar(200) DEFAULT NULL COMMENT '总重图片',
  `tare_img_url` varchar(200) DEFAULT NULL COMMENT '毛重图片',
  `unit_name` varchar(30) DEFAULT '' COMMENT '收货单位',
  `plate_no` varchar(20) DEFAULT '' COMMENT '车牌号',
  `remark` varchar(200) DEFAULT '' COMMENT '备注',
  `types` int(4) NOT NULL DEFAULT '0' COMMENT '类型',
  `status` int(4) NOT NULL DEFAULT '0' COMMENT '状态',
  `return_goods` int(4) DEFAULT '0' COMMENT '是否退货 0不是，1退货',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  `first_time` timestamp NULL DEFAULT '0000-00-00 00:00:00' COMMENT '第一次过磅时间',
  `second_time` timestamp NULL DEFAULT '0000-00-00 00:00:00' COMMENT '第二次过磅时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_pound_log_no` (`pound_log_no`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pound_log
-- ----------------------------
INSERT INTO `pound_log` VALUES ('1', '1', 'æ»é¨ä¸å¤§é¨å°æ¹-1', '201810131101', '7879.0000', '5594.0000', '1365.0000', '800.0000', '120.0000', '1485.0000', 'http://localhost:8000/images/upload/2018/10/18/201810131101/1539850456599.jpg', 'http://localhost:8000/images/upload/2018/10/18/201810131101/1539851421586.jpg', '', '浙J123C', '', '0', '2', null, '2018-10-13 11:02:55', '2018-10-20 10:26:42', '2018-10-18 16:14:17', '2018-10-18 16:14:21');
INSERT INTO `pound_log` VALUES ('2', '1', 'æ»é¨ä¸å¤§é¨å°æ¹-1', '201810131647', '7998.0000', '7116.0000', '862.0000', '0.0000', '20.0000', '-2618.0000', 'http://localhost:8000/images/upload/2018/10/18/201810131647/1539827938115.jpg', 'http://localhost:8000/images/upload/2018/10/18/201810131647/1539851323635.jpg', '', '浙J8888', '', '0', '2', null, '2018-10-13 16:49:12', '2018-10-20 10:50:45', '2018-10-18 09:58:58', '2018-10-18 09:59:03');
INSERT INTO `pound_log` VALUES ('3', '1', 'æ»é¨ä¸å¤§é¨å°æ¹-1', '201810131655', '9791.0000', '5610.0000', '1881.0000', '2000.0000', '300.0000', '1081.0000', 'http://localhost:8000/images/upload/2018/10/18/201810131655/1539833249132.jpg', 'http://localhost:8000/images/upload/2018/10/18/201810131655/1539831414255.jpg', '生产一部', '浙B684H', '备注啥的啊啊', '0', '4', null, '2018-10-13 16:58:16', '2018-10-20 13:37:52', '2018-10-18 11:27:29', '2018-10-18 10:56:54');
INSERT INTO `pound_log` VALUES ('4', '1', 'ZongBu-1', '20181016154338', '9791.0000', '7953.0000', '1838.0000', '0.0000', '0.0000', '0.0000', 'http://localhost:8000/images/upload/2018/10/18/20181016154338/1539826522026.jpg', 'http://localhost:8000/images/upload/2018/10/18/20181016154338/1539831400905.jpg', '', null, '退货', '1', '2', null, '2018-10-16 15:44:51', '2018-10-20 10:31:07', '2018-10-18 09:35:10', '2018-10-18 10:56:41');
INSERT INTO `pound_log` VALUES ('5', '1', 'ZongBu-1', '20181016155116', '12666.0000', '8251.0000', '4415.0000', '0.0000', '0.0000', '0.0000', 'http://localhost:8000/images/upload/2018/10/18/20181016155116/1539826573398.jpg', 'http://localhost:8000/images/upload/2018/10/18/20181016155116/1539827121971.jpg', '', null, '退货或者卖废品', '1', '2', null, '2018-10-16 15:51:20', '2018-10-20 10:31:12', '2018-10-18 09:36:13', '2018-10-18 09:45:22');
INSERT INTO `pound_log` VALUES ('6', '1', 'ZongBu-1', '20181016161428', '11610.0000', '8162.0000', '3448.0000', '0.0000', '123.0000', '0.0000', 'http://localhost:8000/images/upload/2018/10/20/20181016161428/1540003519634.jpg', 'http://localhost:8000/images/upload/2018/10/19/20181016161428/1539911844706.jpg', '', null, '卖废品', '1', '4', null, '2018-10-16 16:14:31', '2018-10-20 15:57:16', '2018-10-20 10:45:20', '2018-10-19 09:17:25');
INSERT INTO `pound_log` VALUES ('7', '1', 'ZongBu-1', '20181019092347', '7254.0000', '5899.0000', '1355.0000', '0.0000', '0.0000', null, 'http://localhost:8000/images/upload/2018/10/19/20181019092347/1539912957977.jpg', 'http://localhost:8000/images/upload/2018/10/19/20181019092347/1539912953133.jpg', '', null, '出货', '1', '2', null, '2018-10-19 09:29:47', '2018-10-20 09:15:36', '2018-10-19 09:35:58', '2018-10-19 09:35:43');
INSERT INTO `pound_log` VALUES ('8', '1', 'ZongBu-1', '20181019093610', '11748.0000', '8694.0000', '3054.0000', '0.0000', '0.0000', '2804.0000', 'http://localhost:8000/images/upload/2018/10/19/20181019093610/1539913188031.jpg', 'http://localhost:8000/images/upload/2018/10/19/20181019093610/1539913179100.jpg', '', '1006612020345396', '', '0', '4', null, '2018-10-19 09:39:03', '2018-10-20 15:51:04', '2018-10-19 09:39:48', '2018-10-19 09:39:39');

-- ----------------------------
-- Table structure for task
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `task_name` varchar(20) NOT NULL DEFAULT '' COMMENT '任务名称',
  `description` varchar(100) DEFAULT NULL COMMENT '描述',
  `other_id` varchar(20) DEFAULT '' COMMENT '备注',
  `datas` varchar(1000) DEFAULT '' COMMENT '数据,推荐json格式',
  `weight` int(11) DEFAULT '0' COMMENT '权重',
  `types` int(4) NOT NULL DEFAULT '0' COMMENT '类型',
  `status` int(4) NOT NULL DEFAULT '0' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  `execute_time` timestamp NULL DEFAULT '0000-00-00 00:00:00' COMMENT '执行时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of task
-- ----------------------------
INSERT INTO `task` VALUES ('1', 'task-1', '第1个task', '2519', null, '1', '0', '1', '2018-09-27 15:29:10', null, '2018-09-30 13:10:39');
INSERT INTO `task` VALUES ('2', 'task-2', '第2个task', '8037', null, '0', '0', '1', '2018-09-27 15:29:10', null, null);
INSERT INTO `task` VALUES ('3', 'task-3', '第3个task', '9051', null, '1', '0', '1', '2018-09-27 15:29:10', null, '2018-09-30 13:10:41');
INSERT INTO `task` VALUES ('4', 'task-4', '第4个task', '3112', null, '0', '0', '1', '2018-09-27 15:29:10', null, null);
INSERT INTO `task` VALUES ('5', 'task-5', '第5个task', '5771', null, '0', '0', '0', '2018-09-27 15:29:10', null, null);
INSERT INTO `task` VALUES ('6', 'task-6', '第6个task', '9711', null, '0', '0', '1', '2018-09-27 15:29:10', null, null);
INSERT INTO `task` VALUES ('7', 'task-7', '第7个task', '7755', null, '0', '0', '1', '2018-09-27 15:29:10', null, null);
INSERT INTO `task` VALUES ('8', 'task-8', '第8个task', '3794', null, '0', '0', '0', '2018-09-27 15:29:10', null, null);
INSERT INTO `task` VALUES ('9', 'task-9', '第9个task', '5564', null, '0', '0', '1', '2018-09-27 15:29:10', null, null);
INSERT INTO `task` VALUES ('10', 'task-10', '第10个task', '6990', null, '0', '0', '1', '2018-09-27 15:29:10', null, null);
INSERT INTO `task` VALUES ('11', 'task-11', '第11个task', '7219', null, '0', '0', '0', '2018-09-27 15:29:10', null, null);
INSERT INTO `task` VALUES ('12', 'task-12', '第12个task', '6421', null, '0', '0', '0', '2018-09-27 15:29:10', null, null);
INSERT INTO `task` VALUES ('13', 'task-13', '第13个task', '1742', null, '0', '0', '1', '2018-09-27 15:29:11', null, null);
INSERT INTO `task` VALUES ('14', 'task-14', '第14个task', '7483', null, '0', '0', '1', '2018-09-27 15:29:11', null, null);
INSERT INTO `task` VALUES ('15', 'task-15', '第15个task', '6350', null, '0', '0', '1', '2018-09-27 15:29:11', null, null);
INSERT INTO `task` VALUES ('16', 'task-16', '第16个task', '3165', null, '0', '0', '1', '2018-09-27 15:29:11', null, null);
INSERT INTO `task` VALUES ('17', 'task-17', '第17个task', '2859', null, '0', '0', '0', '2018-09-27 15:29:11', null, null);
INSERT INTO `task` VALUES ('18', 'task-18', '第18个task', '0126', null, '0', '0', '1', '2018-09-27 15:29:11', null, null);
INSERT INTO `task` VALUES ('19', 'task-19', '第19个task', '2694', null, '0', '0', '1', '2018-09-27 15:29:11', null, null);
INSERT INTO `task` VALUES ('20', 'task-20', '第20个task', '2862', null, '0', '0', '1', '2018-09-27 15:29:11', null, null);
INSERT INTO `task` VALUES ('21', 'task-21', '第21个task', '5218', null, '0', '0', '1', '2018-09-27 15:29:11', null, null);
INSERT INTO `task` VALUES ('22', 'task-22', '第22个task', '8449', null, '0', '0', '1', '2018-09-27 15:29:11', null, null);
INSERT INTO `task` VALUES ('23', 'task-23', '第23个task', '8674', null, '0', '0', '1', '2018-09-27 15:29:11', null, null);
INSERT INTO `task` VALUES ('24', 'task-24', '第24个task', '3577', null, '0', '0', '0', '2018-09-27 15:29:11', null, null);
INSERT INTO `task` VALUES ('25', 'task-25', '第25个task', '1881', null, '0', '0', '1', '2018-09-27 15:29:11', null, null);
INSERT INTO `task` VALUES ('26', 'task-26', '第26个task', '1061', null, '0', '0', '1', '2018-09-27 15:29:11', null, null);
INSERT INTO `task` VALUES ('27', 'task-27', '第27个task', '8455', null, '0', '0', '0', '2018-09-27 15:29:11', null, null);
INSERT INTO `task` VALUES ('28', 'task-28', '第28个task', '2792', null, '0', '0', '1', '2018-09-27 15:29:11', null, null);
INSERT INTO `task` VALUES ('29', 'task-29', '第29个task', '2140', null, '0', '0', '0', '2018-09-27 15:29:11', null, null);
INSERT INTO `task` VALUES ('30', 'task-30', '第30个task', '8430', null, '0', '0', '1', '2018-09-27 15:29:11', null, null);
INSERT INTO `task` VALUES ('31', 'task-31', '第31个task', '0980', null, '0', '0', '0', '2018-09-27 15:29:11', null, null);
INSERT INTO `task` VALUES ('32', 'task-32', '第32个task', '9241', null, '0', '0', '1', '2018-09-27 15:29:11', null, null);
INSERT INTO `task` VALUES ('33', 'task-33', '第33个task', '5469', null, '0', '0', '0', '2018-09-27 15:29:11', null, null);
INSERT INTO `task` VALUES ('34', 'task-34', '第34个task', '7830', null, '0', '0', '1', '2018-09-27 15:29:11', null, null);
INSERT INTO `task` VALUES ('35', 'task-35', '第35个task', '0878', null, '0', '0', '0', '2018-09-27 15:29:11', null, null);
