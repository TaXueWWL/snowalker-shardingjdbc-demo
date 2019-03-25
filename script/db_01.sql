/*
Navicat MySQL Data Transfer

Source Server         : localhost-3306
Source Server Version : 50621
Source Host           : 127.0.0.1:3306
Source Database       : marketingdb_01

Target Server Type    : MYSQL
Target Server Version : 50621
File Encoding         : 65001

Date: 2019-03-14 13:55:49
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_order_0000
-- ----------------------------
DROP TABLE IF EXISTS `t_order_0000`;
CREATE TABLE `t_order_0000` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(32) DEFAULT NULL,
  `order_id` bigint(32) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=453 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_order_0001
-- ----------------------------
DROP TABLE IF EXISTS `t_order_0001`;
CREATE TABLE `t_order_0001` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(32) DEFAULT NULL,
  `order_id` bigint(32) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1026 DEFAULT CHARSET=utf8;
