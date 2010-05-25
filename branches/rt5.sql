/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50142
Source Host           : localhost:3306
Source Database       : rt5

Target Server Type    : MYSQL
Target Server Version : 50142
File Encoding         : 65001

Date: 2010-05-24 21:12:15
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `saved_games`
-- ----------------------------
DROP TABLE IF EXISTS `saved_games`;
CREATE TABLE `saved_games` (
  `username` varchar(16) NOT NULL,
  `password` varchar(32) NOT NULL,
  `coord_x` int(5) NOT NULL DEFAULT '3222',
  `coord_y` int(5) NOT NULL DEFAULT '3222',
  `coord_z` int(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of saved_games
-- ----------------------------
