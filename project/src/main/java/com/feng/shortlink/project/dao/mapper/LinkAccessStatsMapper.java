package com.feng.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.feng.shortlink.project.dao.entity.LinkAccessStatsDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

/**
 * @author FENGXIN
 * @date 2024/10/4
 * @project feng-shortlink
 * @description 基础数据统计实体mapper
 **/
public interface LinkAccessStatsMapper extends BaseMapper<LinkAccessStatsDO> {
    @Override
    @Insert("INSERT INTO t_link_access_stats (gid, full_short_url, date, pv, uv, uip, hour, weekday, create_time, update_time, del_flag) " +
            "VALUES (#{linkAccessStats.gid}, #{linkAccessStats.fullShortUrl}, #{linkAccessStats.date}, #{linkAccessStats.pv}, #{linkAccessStats.uv}, #{linkAccessStats.uip}, #{linkAccessStats.hour}, #{linkAccessStats.weekday}, #{linkAccessStats.createTime}, #{linkAccessStats.updateTime}, 0)" +
            "ON DUPLICATE KEY UPDATE pv = pv + #{linkAccessStats.pv}, uv = uv + #{linkAccessStats.uv}, uip = uip + #{linkAccessStats.uip}, update_time = VALUES(update_time);" )
    int insert(@Param ("linkAccessStats") LinkAccessStatsDO linkAccessStatsDO);
}
