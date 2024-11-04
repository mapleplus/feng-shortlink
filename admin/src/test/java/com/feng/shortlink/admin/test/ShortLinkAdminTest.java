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
            CREATE TABLE `t_user_coupon_%d` (
                                `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
                                `coupon_template_id` bigint(20) DEFAULT NULL COMMENT '优惠券模板ID',
                                `receive_time` datetime DEFAULT NULL COMMENT '领取时间',
                                `receive_count` int(3) DEFAULT NULL COMMENT '领取次数',
                                `valid_start_time` datetime DEFAULT NULL COMMENT '有效期开始时间',
                                `valid_end_time` datetime DEFAULT NULL COMMENT '有效期结束时间',
                                `use_time` datetime DEFAULT NULL COMMENT '使用时间',
                                `row_num` bigint(20) DEFAULT NULL COMMENT '行数',
                                `source` tinyint(1) DEFAULT NULL COMMENT '券来源 0：领券中心 1：平台发放 2：店铺领取',
                                `status` tinyint(1) DEFAULT NULL COMMENT '状态 0：未使用 1：锁定 2：已使用 3：已过期 4：已撤回',
                                `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识 0：未删除 1：已删除',
                                PRIMARY KEY (`id`),
                                UNIQUE KEY `idx_user_id_coupon_template_receive_count` (`user_id`,`coupon_template_id`,`receive_count`) USING BTREE,
                                KEY `idx_user_id` (`user_id`) USING BTREE
                              ) ENGINE=InnoDB AUTO_INCREMENT=1815640588360376337 DEFAULT CHARSET=utf8mb4 COMMENT='用户优惠券表';""";
    public static void main (String[] args) {
        for (int i = 0; i < 32; i++) {
            System.out.printf ((sql) + "%n" , i);
        }
    }
    public static void statsOs(){}
}
