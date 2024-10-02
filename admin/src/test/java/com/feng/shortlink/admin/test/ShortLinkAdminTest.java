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
            create table t_link_goto_%d
              (
                  id             bigint auto_increment comment 'ID'
                      primary key,
                  gid            varchar(32)  null comment 'GID',
                  full_short_url varchar(128) null comment '完整短链接'
              )
                  comment '路由表';""";
    public static void main (String[] args) {
        for (int i = 0; i < 16; i++) {
            System.out.printf ((sql) + "%n" ,i);
        }
    }
}
