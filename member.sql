/*
Navicat MySQL Data Transfer

Source Server         : this
Source Server Version : 50720
Source Host           : localhost:3306
Source Database       : member

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-01-04 18:15:08
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `integral_journal`
-- ----------------------------
DROP TABLE IF EXISTS `integral_journal`;
CREATE TABLE `integral_journal` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '利用主键策略生成的唯一键',
  `user_id` int(11) NOT NULL COMMENT '用户名',
  `integral_type` tinyint(2) NOT NULL COMMENT '流水类型,1:增加流水(add);2:减少流水(minus)',
  `integral_price` int(11) NOT NULL COMMENT '当前流水涉及到的积分值',
  `integral_source` tinyint(2) NOT NULL COMMENT '流水来源,1:完善个人资料(prefect_information);2:拉黑(black)',
  `integral` int(11) NOT NULL COMMENT '用户当前的积分值',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `created_by` int(11) NOT NULL COMMENT '特指后台创建人(公司员工 id)',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `updated_by` int(11) NOT NULL COMMENT '特指后台更新人(公司员工 id)',
  `remark` char(255) NOT NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='用户积分流水表';

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '利用主键策略生成的唯一键',
  `user_name` varchar(32) NOT NULL COMMENT '用户名',
  `password` varchar(32) NOT NULL COMMENT '密码',
  `telephone` varchar(32) NOT NULL COMMENT '手机号码',
  `email` varchar(32) DEFAULT NULL COMMENT '邮箱',
  `qq` varchar(32) DEFAULT NULL COMMENT ' qq',
  `integral` int(11) DEFAULT NULL COMMENT ' 积分',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `created_by` int(11) DEFAULT NULL COMMENT '特指后台创建人(公司员工 id)',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `updated_by` int(11) DEFAULT NULL COMMENT '特指后台更新人(公司员工 id)',
  `remark` char(255) DEFAULT '' COMMENT '备注',
  `user_status` int(11) DEFAULT NULL,
  `is_deleted` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `telephone` (`telephone`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 COMMENT='用户表';

