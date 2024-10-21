package com.feng.shortlink.project.dao.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.feng.shortlink.project.dao.entity.LinkLocaleStatsDO;
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
 * @description 地区统计mapper
 **/
public interface LinkLocaleStatsMapper extends BaseMapper<LinkLocaleStatsDO> {
   
    @Insert(" INSERT INTO t_link_locale_stats ( full_short_url, date, cnt, province, city, adcode, country, create_time, update_time, del_flag) " +
            "VALUES ( #{linkLocaleStats.fullShortUrl}, #{linkLocaleStats.date}, #{linkLocaleStats.cnt}, #{linkLocaleStats.province}, #{linkLocaleStats.city}, #{linkLocaleStats.adcode}, #{linkLocaleStats.country},NOW(),NOW(), 0) " +
            "ON DUPLICATE KEY UPDATE cnt = cnt + #{linkLocaleStats.cnt},update_time = VALUES(update_time);")
    void shortLinkLocaleState (@Param("linkLocaleStats") LinkLocaleStatsDO linkLocaleStatsDO);
    
    /**
     * 根据短链接获取指定日期内基础监控数据
     */
    @Select("""
        SELECT tlls.province,
               SUM(tlls.cnt) AS cnt
        FROM t_link_locale_stats tlls
                 INNER JOIN t_link tl
                            ON tlls.full_short_url = tl.full_short_url COLLATE utf8mb4_general_ci
        WHERE tlls.full_short_url  = #{param.fullShortUrl}
          AND tl.gid =              #{param.gid}
          AND tl.del_flag = '0'
          AND tlls.date BETWEEN     #{param.startDate} and #{param.endDate}
        GROUP BY tlls.full_short_url, tl.gid, tlls.province
        ORDER BY cnt DESC;""")
    List<LinkLocaleStatsDO> listLocaleByShortLink(@Param("param") ShortLinkStatsReqDTO requestParam);
    
    /**
     * 分组根据短链接获取指定日期内基础监控数据
     */
    @Select("""
        SELECT tlls.province,
               SUM(tlls.cnt) AS cnt
        FROM t_link_locale_stats tlls
                 INNER JOIN t_link tl
                            ON tlls.full_short_url = tl.full_short_url COLLATE utf8mb4_general_ci
        WHERE tl.gid =              #{param.gid}
          AND tl.del_flag = '0'
          AND tlls.date BETWEEN     #{param.startDate} and #{param.endDate}
        GROUP BY tl.gid, tlls.province
        ORDER BY cnt DESC;""")
    List<LinkLocaleStatsDO> listLocaleByShortLinkGroup(@Param("param") ShortLinkStatsGroupReqDTO requestParam);
}
