package com.feng.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.feng.shortlink.project.dao.entity.LinkStatsTodayDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

/**
 * @author FENGXIN
 * @date 2024/10/6
 * @project feng-shortlink
 * @description
 **/
public interface LinkStatsTodayMapper extends BaseMapper<LinkStatsTodayDO> {
    /**
     * 记录浏览器访问监控数据
     */
    @Insert("INSERT INTO t_link_stats_today (full_short_url,date,today_pv,today_uv,today_uip,create_time, update_time, del_flag) " +
            "VALUES( #{requestParam.fullShortUrl},#{requestParam.date}, #{requestParam.todayPv}, #{requestParam.todayUv},#{requestParam.todayUip},DATE_ADD(NOW(), INTERVAL 8 HOUR), DATE_ADD(NOW(), INTERVAL 8 HOUR), 0) " +
            "ON DUPLICATE KEY UPDATE today_pv = today_pv + #{requestParam.todayPv},today_uv = today_uv + #{requestParam.todayUv},today_uip = today_uip + #{requestParam.todayUip},update_time = VALUES(update_time);")
    void linkStatTodayState(@Param("requestParam") LinkStatsTodayDO requestParam);
}
