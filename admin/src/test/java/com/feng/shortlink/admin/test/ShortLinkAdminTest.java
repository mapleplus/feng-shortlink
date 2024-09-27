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
            create table t_user%d
            (
                id            bigint auto_increment comment 'ID'
                    primary key,
                username      varchar(256) null comment '用户名',
                password      varchar(512) null comment '密码',
                real_name     varchar(256) null comment '真实姓名',
                phone         varchar(128) null comment '手机号',
                mail          varchar(512) null comment '邮箱',
                deletion_time bigint       null comment '注销时间戳',
                create_time   datetime     null comment '创建时间',
                update_time   datetime     null comment '修改时间',
                del_flag      tinyint(1)   null comment '删除标识 0：未删除 1：已删除'
            );""";
    public static void main (String[] args) {
        for (int i = 0; i < 16; i++) {
            System.out.printf ((sql) + "%n" ,i);
        }
    }
}
