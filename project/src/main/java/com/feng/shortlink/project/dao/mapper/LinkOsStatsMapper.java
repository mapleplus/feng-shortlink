package com.feng.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.feng.shortlink.project.dao.entity.LinkOsStatsDO;
import com.feng.shortlink.project.dto.request.ShortLinkStatsGroupReqDTO;
import com.feng.shortlink.project.dto.request.ShortLinkStatsReqDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;

/**
 * @author FENGXIN
 * @date 2024/10/5
 * @project feng-shortlink
 * @description 操作系统统计信息mapper
 **/
public interface LinkOsStatsMapper extends BaseMapper<LinkOsStatsDO> {
    /**
     * 记录OS访问监控数据
     */
    @Insert("INSERT INTO t_link_os_stats (full_short_url, date, cnt, os, create_time, update_time, del_flag) " +
            "VALUES( #{linkOsStats.fullShortUrl},  #{linkOsStats.date}, #{linkOsStats.cnt}, #{linkOsStats.os}, NOW(), NOW(), 0) " +
            "ON DUPLICATE KEY UPDATE cnt = cnt +  #{linkOsStats.cnt},update_time = VALUES(update_time);")
    void shortLinkOsState (@Param("linkOsStats") LinkOsStatsDO linkOsStatsDO);
    
    /**
     * 根据短链接获取指定日期内操作系统监控数据
     */
    @Select("""
        SELECT tlos.os,
               SUM(tlos.cnt) AS count
        FROM t_link_os_stats tlos
                 INNER JOIN t_link tl
                            ON tlos.full_short_url = tl.full_short_url
        WHERE tlos.full_short_url = #{param.fullShortUrl}
          AND tl.gid =              #{param.gid}
          AND tl.del_flag = '0'
          AND tlos.date BETWEEN     #{param.startDate} and #{param.endDate}
        GROUP BY tlos.full_short_url, tl.gid, tlos.date, tlos.os;""")
    List<HashMap<String, Object>> listOsStatsByShortLink(@Param("param") ShortLinkStatsReqDTO requestParam);
    
    /**
     * 分组根据短链接获取指定日期内操作系统监控数据
     */
    @Select("""
        SELECT tlos.os,
               SUM(tlos.cnt) AS count
        FROM t_link_os_stats tlos
                 INNER JOIN t_link tl
                            ON tlos.full_short_url = tl.full_short_url
        WHERE tl.gid =              #{param.gid}
          AND tl.del_flag = '0'
          AND tlos.date BETWEEN     #{param.startDate} and #{param.endDate}
        GROUP BY tl.gid, tlos.date, tlos.os;""")
    List<HashMap<String, Object>> listOsStatsByShortLinkGroup(@Param("param") ShortLinkStatsGroupReqDTO requestParam);
}
