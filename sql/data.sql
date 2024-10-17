create database feng_shortlink;
use feng_shortlink;
create table if not exists feng_shortlink.t_group_0
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
        unique (username)
);

create table if not exists feng_shortlink.t_group_1
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
        unique (gid)
);

create table if not exists feng_shortlink.t_group_10
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
        unique (gid)
);

create table if not exists feng_shortlink.t_group_11
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
        unique (gid)
);

create table if not exists feng_shortlink.t_group_12
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
        unique (gid)
);

create table if not exists feng_shortlink.t_group_13
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
        unique (gid)
);

create table if not exists feng_shortlink.t_group_14
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
        unique (gid)
);

create table if not exists feng_shortlink.t_group_15
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
        unique (gid)
);

create table if not exists feng_shortlink.t_group_2
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
        unique (gid)
);

create table if not exists feng_shortlink.t_group_3
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
        unique (gid)
);

create table if not exists feng_shortlink.t_group_4
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
        unique (gid)
);

create table if not exists feng_shortlink.t_group_5
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
        unique (gid)
);

create table if not exists feng_shortlink.t_group_6
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
        unique (gid)
);

create table if not exists feng_shortlink.t_group_7
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
        unique (gid)
);

create table if not exists feng_shortlink.t_group_8
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
        unique (gid)
);

create table if not exists feng_shortlink.t_group_9
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
        unique (gid)
);

create table if not exists feng_shortlink.t_group_unique
(
    id  bigint auto_increment comment 'ID'
        primary key,
    gid varchar(32) null comment '分组标识',
    constraint idx_unique_gid
        unique (gid)
);

create table if not exists feng_shortlink.t_link_0
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

create table if not exists feng_shortlink.t_link_1
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

create table if not exists feng_shortlink.t_link_10
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

create table if not exists feng_shortlink.t_link_11
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

create table if not exists feng_shortlink.t_link_12
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

create table if not exists feng_shortlink.t_link_13
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

create table if not exists feng_shortlink.t_link_14
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

create table if not exists feng_shortlink.t_link_15
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

create table if not exists feng_shortlink.t_link_2
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

create table if not exists feng_shortlink.t_link_3
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

create table if not exists feng_shortlink.t_link_4
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

create table if not exists feng_shortlink.t_link_5
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

create table if not exists feng_shortlink.t_link_6
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

create table if not exists feng_shortlink.t_link_7
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

create table if not exists feng_shortlink.t_link_8
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

create table if not exists feng_shortlink.t_link_9
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

create table if not exists feng_shortlink.t_link_access_logs
(
    id             bigint auto_increment comment 'ID'
        primary key,
    full_short_url varchar(128)     null comment '完整短链接',
    user           varchar(64)      null comment '用户信息',
    ip             varchar(64)      null comment 'IP',
    browser        varchar(64)      null comment '浏览器',
    os             varchar(64)      null comment '操作系统',
    network        varchar(64)      null comment '访问网络',
    device         varchar(64)      null comment '访问设备',
    locale         varchar(256)     null comment '访问地区',
    cnt            bigint           null comment '访问量',
    create_time    datetime         null comment '创建时间',
    update_time    datetime         null comment '修改时间',
    del_flag       tinyint(1)       null comment '删除标识 0：未删除 1：已删除',
    del_time       bigint default 0 null comment '删除时间'
)
    collate = utf8mb4_general_ci;

create index t_link_access_logs_index
    on feng_shortlink.t_link_access_logs (full_short_url, user, update_time, del_time);

create table if not exists feng_shortlink.t_link_access_stats
(
    id             bigint auto_increment comment 'ID'
        primary key,
    full_short_url varchar(128)     null comment '完整短链接',
    date           date             null comment '日期',
    pv             int              null comment '访问量',
    uv             int              null comment '独立访问数',
    uip            int              null comment '独立IP数',
    hour           int              null comment '小时',
    weekday        int              null comment '星期',
    create_time    datetime         null comment '创建时间',
    update_time    datetime         null comment '修改时间',
    del_flag       tinyint(1)       null comment '删除标识：0 未删除 1 已删除',
    del_time       bigint default 0 null comment '删除时间',
    constraint t_link_access_stats_full_short_url_date_uindex
        unique (full_short_url, date, del_time)
)
    row_format = DYNAMIC;

create table if not exists feng_shortlink.t_link_browser_stats
(
    id             bigint auto_increment comment 'ID'
        primary key,
    full_short_url varchar(128)     null comment '完整短链接',
    date           date             null comment '日期',
    cnt            int              null comment '访问量',
    browser        varchar(64)      null comment '浏览器',
    create_time    datetime         null comment '创建时间',
    update_time    datetime         null comment '修改时间',
    del_flag       tinyint(1)       null comment '删除标识 0：未删除 1：已删除',
    del_time       bigint default 0 null comment '删除时间',
    constraint t_link_browser_stats_pk
        unique (full_short_url, browser, del_time)
);

create table if not exists feng_shortlink.t_link_device_stats
(
    id             bigint auto_increment comment 'ID'
        primary key,
    full_short_url varchar(128)     null comment '完整短链接',
    date           date             null comment '日期',
    cnt            int              null comment '访问量',
    device         varchar(64)      null comment '访问设备',
    create_time    datetime         null comment '创建时间',
    update_time    datetime         null comment '修改时间',
    del_flag       tinyint(1)       null comment '删除标识 0：未删除 1：已删除',
    del_time       bigint default 0 null comment '删除时间',
    constraint idx_unique_browser_stats
        unique (full_short_url, device, del_time)
);

create table if not exists feng_shortlink.t_link_goto_0
(
    id             bigint auto_increment comment 'ID'
        primary key,
    gid            varchar(32)  null comment 'GID',
    full_short_url varchar(128) null comment '完整短链接',
    constraint t_link_goto_0_full_short_url_uindex
        unique (full_short_url)
)
    comment '路由表';

create table if not exists feng_shortlink.t_link_goto_1
(
    id             bigint auto_increment comment 'ID'
        primary key,
    gid            varchar(32)  null comment 'GID',
    full_short_url varchar(128) null comment '完整短链接',
    constraint t_link_goto_1_full_short_url_uindex
        unique (full_short_url)
)
    comment '路由表';

create table if not exists feng_shortlink.t_link_goto_10
(
    id             bigint auto_increment comment 'ID'
        primary key,
    gid            varchar(32)  null comment 'GID',
    full_short_url varchar(128) null comment '完整短链接',
    constraint t_link_goto_10_full_short_url_uindex
        unique (full_short_url)
)
    comment '路由表';

create table if not exists feng_shortlink.t_link_goto_11
(
    id             bigint auto_increment comment 'ID'
        primary key,
    gid            varchar(32)  null comment 'GID',
    full_short_url varchar(128) null comment '完整短链接',
    constraint t_link_goto_11_full_short_url_uindex
        unique (full_short_url)
)
    comment '路由表';

create table if not exists feng_shortlink.t_link_goto_12
(
    id             bigint auto_increment comment 'ID'
        primary key,
    gid            varchar(32)  null comment 'GID',
    full_short_url varchar(128) null comment '完整短链接',
    constraint t_link_goto_12_full_short_url_uindex
        unique (full_short_url)
)
    comment '路由表';

create table if not exists feng_shortlink.t_link_goto_13
(
    id             bigint auto_increment comment 'ID'
        primary key,
    gid            varchar(32)  null comment 'GID',
    full_short_url varchar(128) null comment '完整短链接',
    constraint t_link_goto_13_full_short_url_uindex
        unique (full_short_url)
)
    comment '路由表';

create table if not exists feng_shortlink.t_link_goto_14
(
    id             bigint auto_increment comment 'ID'
        primary key,
    gid            varchar(32)  null comment 'GID',
    full_short_url varchar(128) null comment '完整短链接',
    constraint t_link_goto_14_full_short_url_uindex
        unique (full_short_url)
)
    comment '路由表';

create table if not exists feng_shortlink.t_link_goto_15
(
    id             bigint auto_increment comment 'ID'
        primary key,
    gid            varchar(32)  null comment 'GID',
    full_short_url varchar(128) null comment '完整短链接',
    constraint t_link_goto_15_full_short_url_uindex
        unique (full_short_url)
)
    comment '路由表';

create table if not exists feng_shortlink.t_link_goto_2
(
    id             bigint auto_increment comment 'ID'
        primary key,
    gid            varchar(32)  null comment 'GID',
    full_short_url varchar(128) null comment '完整短链接'
)
    comment '路由表';

create index t_link_goto_2_full_short_url_index
    on feng_shortlink.t_link_goto_2 (full_short_url);

create table if not exists feng_shortlink.t_link_goto_3
(
    id             bigint auto_increment comment 'ID'
        primary key,
    gid            varchar(32)  null comment 'GID',
    full_short_url varchar(128) null comment '完整短链接',
    constraint t_link_goto_3_full_short_url_uindex
        unique (full_short_url)
)
    comment '路由表';

create table if not exists feng_shortlink.t_link_goto_4
(
    id             bigint auto_increment comment 'ID'
        primary key,
    gid            varchar(32)  null comment 'GID',
    full_short_url varchar(128) null comment '完整短链接',
    constraint t_link_goto_4_full_short_url_uindex
        unique (full_short_url)
)
    comment '路由表';

create table if not exists feng_shortlink.t_link_goto_5
(
    id             bigint auto_increment comment 'ID'
        primary key,
    gid            varchar(32)  null comment 'GID',
    full_short_url varchar(128) null comment '完整短链接',
    constraint t_link_goto_5_full_short_url_uindex
        unique (full_short_url)
)
    comment '路由表';

create table if not exists feng_shortlink.t_link_goto_6
(
    id             bigint auto_increment comment 'ID'
        primary key,
    gid            varchar(32)  null comment 'GID',
    full_short_url varchar(128) null comment '完整短链接',
    constraint t_link_goto_6_full_short_url_uindex
        unique (full_short_url)
)
    comment '路由表';

create table if not exists feng_shortlink.t_link_goto_7
(
    id             bigint auto_increment comment 'ID'
        primary key,
    gid            varchar(32)  null comment 'GID',
    full_short_url varchar(128) null comment '完整短链接',
    constraint t_link_goto_7_full_short_url_uindex
        unique (full_short_url)
)
    comment '路由表';

create table if not exists feng_shortlink.t_link_goto_8
(
    id             bigint auto_increment comment 'ID'
        primary key,
    gid            varchar(32)  null comment 'GID',
    full_short_url varchar(128) null comment '完整短链接',
    constraint t_link_goto_8_full_short_url_uindex
        unique (full_short_url)
)
    comment '路由表';

create table if not exists feng_shortlink.t_link_goto_9
(
    id             bigint auto_increment comment 'ID'
        primary key,
    gid            varchar(32)  null comment 'GID',
    full_short_url varchar(128) null comment '完整短链接',
    constraint t_link_goto_9_full_short_url_uindex
        unique (full_short_url)
)
    comment '路由表';

create table if not exists feng_shortlink.t_link_locale_stats
(
    id             bigint auto_increment comment 'ID'
        primary key,
    full_short_url varchar(128)     null comment '完整短链接',
    date           date             null comment '日期',
    cnt            int              null comment '访问量',
    province       varchar(64)      null comment '省份名称',
    city           varchar(64)      null comment '市名称',
    adcode         varchar(64)      null comment '城市编码',
    country        varchar(64)      null comment '国家标识',
    create_time    datetime         null comment '创建时间',
    update_time    datetime         not null comment '修改时间',
    del_flag       tinyint(1)       null comment '删除标识 0表示删除 1表示未删除',
    del_time       bigint default 0 null comment '删除时间',
    constraint idx_unique_locale_stats
        unique (full_short_url, adcode, province, del_time)
)
    collate = utf8mb4_general_ci;

create table if not exists feng_shortlink.t_link_network_stats
(
    id             bigint auto_increment comment 'ID'
        primary key,
    full_short_url varchar(128)     null comment '完整短链接',
    date           date             null comment '日期',
    cnt            int              null comment '访问量',
    network        varchar(64)      null comment '访问网络',
    create_time    datetime         null comment '创建时间',
    update_time    datetime         null comment '修改时间',
    del_flag       tinyint(1)       null comment '删除标识 0：未删除 1：已删除',
    del_time       bigint default 0 null comment '删除时间',
    constraint idx_unique_browser_stats
        unique (full_short_url, network, del_time)
);

create table if not exists feng_shortlink.t_link_os_stats
(
    id             bigint auto_increment comment 'ID'
        primary key,
    full_short_url varchar(128)     null comment '完整短链接',
    date           date             null comment '日期',
    cnt            int              null comment '访问量',
    os             varchar(64)      null comment '操作系统',
    create_time    datetime         null comment '创建时间',
    update_time    datetime         null comment '修改时间',
    del_flag       tinyint(1)       null comment '删除标识 0：未删除 1：已删除',
    del_time       bigint default 0 null comment '删除时间',
    constraint idx_unique_browser_stats
        unique (full_short_url, os, del_time)
);

create table if not exists feng_shortlink.t_link_stats_today
(
    id             bigint auto_increment comment 'ID'
        primary key,
    full_short_url varchar(128)     null comment '短链接',
    date           date             null comment '日期',
    today_pv       int    default 0 null comment '今日PV',
    today_uv       int    default 0 null comment '今日UV',
    today_uip      int    default 0 null comment '今日IP数',
    create_time    datetime         null comment '创建时间',
    update_time    datetime         null comment '修改时间',
    del_flag       tinyint(1)       null comment '删除标识 0：未删除 1：已删除',
    del_time       bigint default 0 null comment '删除时间',
    constraint idx_unique_today_stats
        unique (full_short_url, date, del_time)
);

create table if not exists feng_shortlink.t_user_0
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

create table if not exists feng_shortlink.t_user_1
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

create table if not exists feng_shortlink.t_user_10
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

create table if not exists feng_shortlink.t_user_11
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

create table if not exists feng_shortlink.t_user_12
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

create table if not exists feng_shortlink.t_user_13
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

create table if not exists feng_shortlink.t_user_14
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

create table if not exists feng_shortlink.t_user_15
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

create table if not exists feng_shortlink.t_user_2
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

create table if not exists feng_shortlink.t_user_3
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

create table if not exists feng_shortlink.t_user_4
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

create table if not exists feng_shortlink.t_user_5
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

create table if not exists feng_shortlink.t_user_6
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

create table if not exists feng_shortlink.t_user_7
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

create table if not exists feng_shortlink.t_user_8
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

create table if not exists feng_shortlink.t_user_9
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


