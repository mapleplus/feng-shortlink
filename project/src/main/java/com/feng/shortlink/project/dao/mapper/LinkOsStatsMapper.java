package com.feng.shortlink.project.dao.mapper;

import com.feng.shortlink.project.dao.entity.LinkOsStatsDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

/**
 * @author FENGXIN
 * @date 2024/10/5
 * @project feng-shortlink
 * @description 操作系统统计信息mapper
 **/
public interface LinkOsStatsMapper {
    /**
     * 记录OS访问监控数据
     */
    @Insert("INSERT INTO t_link_os_stats (full_short_url, gid, date, cnt, os, create_time, update_time, del_flag) " +
            "VALUES( #{linkOsStats.fullShortUrl}, #{linkOsStats.gid}, #{linkOsStats.date}, #{linkOsStats.cnt}, #{linkOsStats.os}, NOW(), NOW(), 0) " +
            "ON DUPLICATE KEY UPDATE cnt = cnt +  #{linkOsStats.cnt},update_time = VALUES(update_time);")
    void shortLinkBrowserState(@Param("linkOsStats") LinkOsStatsDO linkOsStatsDO);
}
