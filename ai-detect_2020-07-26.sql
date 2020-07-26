/*
 Navicat Premium Data Transfer

 Source Server         : root
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : localhost:3306
 Source Schema         : ai-detect

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : 65001

 Date: 26/07/2020 16:49:46
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for camera
-- ----------------------------
DROP TABLE IF EXISTS `camera`;
CREATE TABLE `camera`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '摄像头编号',
  `user_id` int(11) NULL DEFAULT NULL,
  `play_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `push_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `position` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '摄像头具体放置地点',
  `token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `warning` int(11) NULL DEFAULT NULL,
  `status` int(11) NULL DEFAULT NULL,
  `organization_id` int(11) NULL DEFAULT NULL COMMENT '所属组织',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of camera
-- ----------------------------
INSERT INTO `camera` VALUES (1, 'test', 'source-test', NULL, '1221', 1, NULL, NULL, 'pos', NULL, NULL, NULL, 1, 1, NULL, NULL);

-- ----------------------------
-- Table structure for flow
-- ----------------------------
DROP TABLE IF EXISTS `flow`;
CREATE TABLE `flow`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `flow` int(11) NULL DEFAULT NULL COMMENT '人流量(统计方式为取1秒内人流量的平均数据)',
  `photo_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `detect_status` int(255) NULL DEFAULT NULL,
  `camera_id` bigint(20) NULL DEFAULT NULL,
  `record_time` datetime(0) NULL DEFAULT NULL COMMENT '当前记录时间点',
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of flow
-- ----------------------------
INSERT INTO `flow` VALUES (1, 10, NULL, NULL, 1, '2020-07-21 14:23:46', NULL);
INSERT INTO `flow` VALUES (2, 25, NULL, NULL, 1, '2020-07-23 14:41:11', NULL);
INSERT INTO `flow` VALUES (3, 30, NULL, NULL, 1, '2020-07-11 12:56:15', NULL);
INSERT INTO `flow` VALUES (4, 13, NULL, NULL, 1, '2020-07-25 23:00:00', NULL);
INSERT INTO `flow` VALUES (5, 36, NULL, NULL, 1, '2020-07-24 23:59:27', NULL);

-- ----------------------------
-- Table structure for organization
-- ----------------------------
DROP TABLE IF EXISTS `organization`;
CREATE TABLE `organization`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `organization` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组织名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of organization
-- ----------------------------
INSERT INTO `organization` VALUES (1, 'org', 'des', NULL, NULL);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(255) NULL DEFAULT NULL,
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, 1, 'admin', NULL, NULL);
INSERT INTO `role` VALUES (2, 2, 'user', NULL, NULL);
INSERT INTO `role` VALUES (4, 2, 'admin', NULL, NULL);

-- ----------------------------
-- Table structure for snapshot
-- ----------------------------
DROP TABLE IF EXISTS `snapshot`;
CREATE TABLE `snapshot`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片路径',
  `info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `status` int(255) NULL DEFAULT NULL,
  `camera_id` bigint(20) NULL DEFAULT NULL,
  `flow_id` bigint(255) NULL DEFAULT NULL,
  `record_time` datetime(0) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of snapshot
-- ----------------------------
INSERT INTO `snapshot` VALUES (1, '/test/1.jpg', 'info', 1, NULL, 1, '2020-07-26 15:23:13', NULL, NULL);
INSERT INTO `snapshot` VALUES (2, NULL, NULL, 1, NULL, 1, '2020-07-23 15:23:18', NULL, NULL);
INSERT INTO `snapshot` VALUES (6, '0d911532d709447d9d6b01480fd156a9.jpg', NULL, 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `snapshot` VALUES (7, '2267240763004cc6ada454d131e1fa88.jpg', NULL, 1, NULL, NULL, '2020-07-28 16:00:00', NULL, NULL);
INSERT INTO `snapshot` VALUES (8, 'b759a95d335443d0bf8fd685cb22a9f8.jpeg', 'ioewlka', 0, NULL, NULL, '2020-06-30 16:00:00', NULL, NULL);
INSERT INTO `snapshot` VALUES (9, '8abe9fc0f5e54749b32f3fa08d292eaf.jpg', NULL, 0, NULL, NULL, '2020-07-28 16:00:00', NULL, NULL);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` int(11) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', '123456', 0, NULL, NULL);
INSERT INTO `user` VALUES (2, 'user', '123456', 1, NULL, NULL);
INSERT INTO `user` VALUES (3, 'test', '123456', 1, NULL, NULL);

-- ----------------------------
-- Table structure for video
-- ----------------------------
DROP TABLE IF EXISTS `video`;
CREATE TABLE `video`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '视频地址',
  `camera_id` bigint(20) NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '视频开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '视频结束时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- View structure for flow_hour
-- ----------------------------
DROP VIEW IF EXISTS `flow_hour`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `flow_hour` AS select `f`.`camera_id` AS `camera_id`,cast(`f`.`record_time` as date) AS `date`,hour(`f`.`record_time`) AS `hour`,sum(`f`.`flow`) AS `hour_flow` from `flow` `f` group by `f`.`camera_id`,cast(`f`.`record_time` as date),hour(`f`.`record_time`) order by cast(`f`.`record_time` as date),hour(`f`.`record_time`);

SET FOREIGN_KEY_CHECKS = 1;
