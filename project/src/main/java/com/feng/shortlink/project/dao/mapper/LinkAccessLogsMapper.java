package com.feng.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.feng.shortlink.project.dao.entity.LinkAccessLogsDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

/**
 * @author FENGXIN
 * @date 2024/10/5
 * @project feng-shortlink
 * @description 访问日志统计信息mapper
 **/
public interface LinkAccessLogsMapper extends BaseMapper<LinkAccessLogsDO> {
    /**
     * 记录浏览器访问监控数据
     */
    @Insert("INSERT INTO t_link_access_logs (full_short_url, gid, user, browser,os,ip,cnt,create_time, update_time, del_flag) " +
            "VALUES( #{linkAccessLogs.fullShortUrl}, #{linkAccessLogs.gid}, #{linkAccessLogs.user}, #{linkAccessLogs.browser}, #{linkAccessLogs.os},#{linkAccessLogs.ip},#{linkAccessLogs.cnt},NOW(), NOW(), 0) " +
            "ON DUPLICATE KEY UPDATE cnt = cnt +  #{linkAccessLogs.cnt},update_time = VALUES(update_time);")
    void shortLinkBrowserState(@Param("linkAccessLogs") LinkAccessLogsDO linkAccessLogsDO);
}
