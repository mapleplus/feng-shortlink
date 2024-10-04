package com.feng.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.feng.shortlink.project.dao.entity.LinkLocaleStatsDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

/**
 * @author FENGXIN
 * @date 2024/10/4
 * @project feng-shortlink
 * @description 地区统计实体mapper
 **/
public interface LinkLocaleStatsMapper extends BaseMapper<LinkLocaleStatsDO> {
    @Override
    @Insert(" INSERT INTO t_link_locale_stats (gid, full_short_url, date, cnt, province, city, adcode, country, create_time, update_time, del_flag) " +
            "VALUES (#{linkLocaleStats.gid}, #{linkLocaleStats.fullShortUrl}, #{linkLocaleStats.date}, #{linkLocaleStats.cnt}, #{linkLocaleStats.province}, #{linkLocaleStats.city}, #{linkLocaleStats.adcode}, #{linkLocaleStats.country}, NOW(), NOW(), 0) " +
            "ON DUPLICATE KEY UPDATE cnt = cnt + #{linkLocaleStats.cnt};")
    int insert(@Param("linkLocaleStats") LinkLocaleStatsDO linkLocaleStatsDO);
}
