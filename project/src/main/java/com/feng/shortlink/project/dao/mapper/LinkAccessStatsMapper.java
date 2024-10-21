package com.feng.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.feng.shortlink.project.dao.entity.LinkAccessStatsDO;
import com.feng.shortlink.project.dto.request.ShortLinkStatsGroupReqDTO;
import com.feng.shortlink.project.dto.request.ShortLinkStatsReqDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author FENGXIN
 * @date 2024/10/4
 * @project feng-shortlink
 * @description 基础数据统计mapper
 **/
public interface LinkAccessStatsMapper extends BaseMapper<LinkAccessStatsDO> {
    
    /**
     * 短链接访问状态
     *
     * @param linkAccessStatsDO 链接访问统计 do
     */
    @Insert("INSERT INTO t_link_access_stats ( full_short_url, date, pv, uv, uip, hour, weekday, create_time, update_time, del_flag) " +
            "VALUES ( #{linkAccessStats.fullShortUrl}, #{linkAccessStats.date}, #{linkAccessStats.pv}, #{linkAccessStats.uv}, #{linkAccessStats.uip}, #{linkAccessStats.hour}, #{linkAccessStats.weekday},NOW(),NOW(), 0)" +
            "ON DUPLICATE KEY UPDATE pv = pv + #{linkAccessStats.pv}, uv = uv + #{linkAccessStats.uv}, uip = uip + #{linkAccessStats.uip}, update_time = VALUES(update_time);" )
    void shortLinkAccessState (@Param ("linkAccessStats") LinkAccessStatsDO linkAccessStatsDO);
    /**
     * 根据短链接获取指定日期内基础监控数据
     */
    @Select("""
        SELECT tlas.date,
               SUM(tlas.pv)  AS pv,
               SUM(tlas.uv)  AS uv,
               SUM(tlas.uip) AS uip
        FROM t_link_access_stats tlas
                 INNER JOIN t_link tl
                            ON tlas.full_short_url = tl.full_short_url
        WHERE tlas.full_short_url = #{param.fullShortUrl}
          AND tl.gid =              #{param.gid}
          AND tl.del_flag = '0'
          AND tlas.date BETWEEN     #{param.startDate} and #{param.endDate}
        GROUP BY tlas.full_short_url, tl.gid, tlas.date;""")
    List<LinkAccessStatsDO> listStatsByShortLink(@Param("param") ShortLinkStatsReqDTO requestParam);
    
    /**
     * 分组根据短链接获取指定日期内基础监控数据
     */
    @Select("""
        SELECT tlas.date,
               SUM(tlas.pv)  AS pv,
               SUM(tlas.uv)  AS uv,
               SUM(tlas.uip) AS uip
        FROM t_link_access_stats tlas
                 INNER JOIN t_link tl
                            ON tlas.full_short_url = tl.full_short_url
        WHERE  tl.gid =              #{param.gid}
          AND tl.del_flag = '0'
          AND tlas.date BETWEEN     #{param.startDate} and #{param.endDate}
        GROUP BY tl.gid, tlas.date;""")
    List<LinkAccessStatsDO> listStatsByShortLinkGroup(@Param("param") ShortLinkStatsGroupReqDTO requestParam);
    
    /**
     * 根据短链接获取指定日期内小时基础监控数据
     */
    @Select("""
        SELECT tlas.hour,
               SUM(tlas.pv) AS pv
        FROM t_link_access_stats tlas
                 INNER JOIN t_link tl
                            ON tlas.full_short_url = tl.full_short_url
        WHERE tlas.full_short_url = #{param.fullShortUrl}
          AND tl.gid =              #{param.gid}
          AND tl.del_flag = '0'
          AND tlas.date BETWEEN     #{param.startDate} and #{param.endDate}
        GROUP BY tlas.full_short_url, tl.gid, tlas.hour;""")
    List<LinkAccessStatsDO> listHourStatsByShortLink(@Param("param") ShortLinkStatsReqDTO requestParam);
    
    /**
     * 分组根据短链接获取指定日期内小时基础监控数据
     */
    @Select("""
        SELECT tlas.hour,
               SUM(tlas.pv) AS pv
        FROM t_link_access_stats tlas
                 INNER JOIN t_link tl
                            ON tlas.full_short_url = tl.full_short_url
        WHERE tl.gid =              #{param.gid}
          AND tl.del_flag = '0'
          AND tlas.date BETWEEN     #{param.startDate} and #{param.endDate}
        GROUP BY tl.gid, tlas.hour;""")
    List<LinkAccessStatsDO> listHourStatsByShortLinkGroup(@Param("param") ShortLinkStatsGroupReqDTO requestParam);
    
    /**
     * 根据短链接获取指定日期内星期基础监控数据
     */
    @Select("""
        SELECT tlas.weekday,
               SUM(tlas.pv) AS pv
        FROM t_link_access_stats tlas
                 INNER JOIN t_link tl
                            ON tlas.full_short_url = tl.full_short_url
        WHERE tlas.full_short_url = #{param.fullShortUrl}
          AND tl.gid =              #{param.gid}
          AND tl.del_flag = '0'
          AND tlas.date BETWEEN     #{param.startDate} and #{param.endDate}
        GROUP BY tlas.full_short_url, tl.gid, tlas.weekday;""")
    List<LinkAccessStatsDO> listWeekdayStatsByShortLink(@Param("param") ShortLinkStatsReqDTO requestParam);
    
    /**
     * 分组根据短链接获取指定日期内星期基础监控数据
     */
    @Select("""
        SELECT tlas.weekday,
               SUM(tlas.pv) AS pv
        FROM t_link_access_stats tlas
                 INNER JOIN t_link tl
                            ON tlas.full_short_url = tl.full_short_url
        WHERE tl.gid =              #{param.gid}
          AND tl.del_flag = '0'
          AND tlas.date BETWEEN     #{param.startDate} and #{param.endDate}
        GROUP BY tl.gid, tlas.weekday;""")
    List<LinkAccessStatsDO> listWeekdayStatsByShortLinkGroup(@Param("param") ShortLinkStatsGroupReqDTO requestParam);
}
