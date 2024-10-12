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
            create table t_group_%d
            (
              id          bigint auto_increment comment 'ID'
                  primary key,
              gid         varchar(32)  null comment '分组标识',
              name        varchar(64)  null comment '分组名称',
              username    varchar(256) null comment '创建分组用户名',
              sort_order  int          null comment '分组排序',
              create_time datetime     null comment '创建时间',
              update_time datetime     null comment '修改时间',
              del_flag    tinyint(1)   null comment '删除标识 0：未删除 1：已删除',
              constraint idx_unique_username_gid
                  unique (gid, username)
              );
            create table t_link_%d
            (
                id              bigint auto_increment comment 'ID'
                    primary key,
                domain          varchar(128)                   null comment '域名',
                short_uri       varchar(8) collate utf8mb3_bin null comment '短链接',
                full_short_url  varchar(128)                   null comment '完整短链接',
                origin_url      varchar(1024)                  null comment '原始链接',
                click_num       int default 0                  null comment '点击量',
                gid             varchar(32)                    null comment '分组标识',
                favicon         varchar(256)                   null comment '网站图标',
                enable_status   tinyint(1)                     null comment '启用标识 0：已启用 1：未启用',
                created_type    tinyint(1)                     null comment '创建类型 0：接口 1：控制台',
                valid_date_type tinyint(1)                     null comment '有效期类型 0：永久有效 1：用户自定义',
                valid_date      datetime                       null comment '有效期',
                `describe`      varchar(1024)                  null comment '描述',
                total_uv        int default 0                  null comment '历史uv',
                total_pv        int default 0                  null comment '历史pv',
                total_uip       int default 0                  null comment '历史uip',
                create_time     datetime                       null comment '创建时间',
                update_time     datetime                       null comment '修改时间',
                del_flag        tinyint(1)                     null comment '删除标识 0：未删除 1：已删除',
                del_time        bigint                         null,
                constraint idx_unique_full_short_url
                    unique (full_short_url, del_time)
            );
            create table t_link_access_logs
            (
                id             bigint auto_increment comment 'ID'
                    primary key,
                full_short_url varchar(128) null comment '完整短链接',
                user           varchar(64)  null comment '用户信息',
                ip             varchar(64)  null comment 'IP',
                browser        varchar(64)  null comment '浏览器',
                os             varchar(64)  null comment '操作系统',
                network        varchar(64)  null comment '访问网络',
                device         varchar(64)  null comment '访问设备',
                locale         varchar(256) null comment '访问地区',
                cnt            bigint       null comment '访问量',
                create_time    datetime     null comment '创建时间',
                update_time    datetime     null comment '修改时间',
                del_flag       tinyint(1)   null comment '删除标识 0：未删除 1：已删除',
                constraint t_link_access_logs_full_short_url_user_uindex
                    unique (full_short_url, user),
                constraint t_link_access_logs_gid_full_short_url_user_uindex
                    unique (full_short_url, user)
            )
                collate = utf8mb4_general_ci;
            create table t_link_access_stats
            (
                id             bigint auto_increment comment 'ID'
                    primary key,
                full_short_url varchar(128) null comment '完整短链接',
                date           date         null comment '日期',
                pv             int          null comment '访问量',
                uv             int          null comment '独立访问数',
                uip            int          null comment '独立IP数',
                hour           int          null comment '小时',
                weekday        int          null comment '星期',
                create_time    datetime     null comment '创建时间',
                update_time    datetime     null comment '修改时间',
                del_flag       tinyint(1)   null comment '删除标识：0 未删除 1 已删除',
                constraint t_link_access_stats_full_short_url_date_uindex
                    unique (full_short_url, date),
                constraint t_link_access_stats_gid_full_short_url_date_uindex
                    unique (full_short_url, date)
            )
                row_format = DYNAMIC;
            create table t_link_browser_stats
            (
                id             bigint auto_increment comment 'ID'
                    primary key,
                full_short_url varchar(128) null comment '完整短链接',
                date           date         null comment '日期',
                cnt            int          null comment '访问量',
                browser        varchar(64)  null comment '浏览器',
                create_time    datetime     null comment '创建时间',
                update_time    datetime     null comment '修改时间',
                del_flag       tinyint(1)   null comment '删除标识 0：未删除 1：已删除',
                constraint t_link_browser_stats_pk
                    unique (full_short_url, date, browser)
            );
            create table t_link_device_stats
            (
                id             bigint auto_increment comment 'ID'
                    primary key,
                full_short_url varchar(128) null comment '完整短链接',
                date           date         null comment '日期',
                cnt            int          null comment '访问量',
                device         varchar(64)  null comment '访问设备',
                create_time    datetime     null comment '创建时间',
                update_time    datetime     null comment '修改时间',
                del_flag       tinyint(1)   null comment '删除标识 0：未删除 1：已删除',
                constraint idx_unique_browser_stats
                    unique (full_short_url, date, device)
            );
            create table t_link_locale_stats
            (
                id             bigint auto_increment comment 'ID'
                    primary key,
                full_short_url varchar(128) null comment '完整短链接',
                date           date         null comment '日期',
                cnt            int          null comment '访问量',
                province       varchar(64)  null comment '省份名称',
                city           varchar(64)  null comment '市名称',
                adcode         varchar(64)  null comment '城市编码',
                country        varchar(64)  null comment '国家标识',
                create_time    datetime     null comment '创建时间',
                update_time    datetime     not null comment '修改时间',
                del_flag       tinyint(1)   null comment '删除标识 0表示删除 1表示未删除',
                constraint idx_unique_locale_stats
                    unique (full_short_url, date, adcode, province)
            )
                collate = utf8mb4_general_ci;
            create table t_link_network_stats
            (
                id             bigint auto_increment comment 'ID'
                    primary key,
                full_short_url varchar(128) null comment '完整短链接',
                date           date         null comment '日期',
                cnt            int          null comment '访问量',
                network        varchar(64)  null comment '访问网络',
                create_time    datetime     null comment '创建时间',
                update_time    datetime     null comment '修改时间',
                del_flag       tinyint(1)   null comment '删除标识 0：未删除 1：已删除',
                constraint idx_unique_browser_stats
                    unique (full_short_url, date, network)
            );
            create table t_link_os_stats
            (
                id             bigint auto_increment comment 'ID'
                    primary key,
                full_short_url varchar(128) null comment '完整短链接',
                date           date         null comment '日期',
                cnt            int          null comment '访问量',
                os             varchar(64)  null comment '操作系统',
                create_time    datetime     null comment '创建时间',
                update_time    datetime     null comment '修改时间',
                del_flag       tinyint(1)   null comment '删除标识 0：未删除 1：已删除',
                constraint idx_unique_browser_stats
                    unique (full_short_url, date, os)
            );
            create table t_link_stats_today
            (
                id             bigint auto_increment comment 'ID'
                    primary key,
                full_short_url varchar(128)  null comment '短链接',
                date           date          null comment '日期',
                today_pv       int default 0 null comment '今日PV',
                today_uv       int default 0 null comment '今日UV',
                today_uip      int default 0 null comment '今日IP数',
                create_time    datetime      null comment '创建时间',
                update_time    datetime      null comment '修改时间',
                del_flag       tinyint(1)    null comment '删除标识 0：未删除 1：已删除',
                constraint idx_unique_today_stats
                    unique (full_short_url, date)
            );
            create table t_user_%d
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
            );
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
            System.out.printf ((sql) + "%n" , i, i, i, i);
        }
    }
}
