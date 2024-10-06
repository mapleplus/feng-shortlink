package com.feng.shortlink.admin.test;

import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author FENGXIN
 * @date 2024/9/27
 * @project feng-shortlink
 * @description
 **/
@SpringBootTest
public class ShortLinkAdminTest {
    private static final String sql = """
            CREATE TABLE `t_link_stats_today_%d` (
              `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
              `gid` varchar(32) DEFAULT 'default' COMMENT '分组标识',
              `full_short_url` varchar(128) DEFAULT NULL COMMENT '短链接',
              `date` date DEFAULT NULL COMMENT '日期',
              `today_pv` int DEFAULT '0' COMMENT '今日PV',
              `today_uv` int DEFAULT '0' COMMENT '今日UV',
              `today_uip` int DEFAULT '0' COMMENT '今日IP数',
              `create_time` datetime DEFAULT NULL COMMENT '创建时间',
              `update_time` datetime DEFAULT NULL COMMENT '修改时间',
              `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识 0：未删除 1：已删除',
      PRIMARY KEY (`id`),
      UNIQUE KEY `idx_unique_today_stats` (`full_short_url`,`gid`,`date`) USING BTREE
    ) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;""";
    public static void main (String[] args) {
        for (int i = 0; i < 16; i++) {
            System.out.printf ((sql) + "%n" ,i);
        }
    }
}
