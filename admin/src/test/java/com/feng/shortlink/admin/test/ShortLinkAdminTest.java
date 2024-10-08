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
            alter table t_link_%d
            add constraint idx_unique_full_short_url
            unique (full_short_url, del_time);""";
    public static void main (String[] args) {
        for (int i = 0; i < 16; i++) {
            System.out.printf ((sql) + "%n" ,i);
        }
    }
}
