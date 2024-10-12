package com.feng.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.feng.shortlink.project.dao.entity.LinkBrowserStatsDO;
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
 * @description 浏览器统计信息mapper
 **/
public interface LinkBrowserStatsMapper extends BaseMapper<LinkBrowserStatsDO> {
    /**
     * 记录浏览器访问监控数据
     */
    @Insert("INSERT INTO t_link_browser_stats (full_short_url, date, cnt, browser, create_time, update_time, del_flag) " +
            "VALUES( #{linkBrowserStats.fullShortUrl},  #{linkBrowserStats.date}, #{linkBrowserStats.cnt}, #{linkBrowserStats.browser}, NOW(), NOW(), 0) " +
            "ON DUPLICATE KEY UPDATE cnt = cnt +  #{linkBrowserStats.cnt},update_time = VALUES(update_time);")
    void shortLinkBrowserState(@Param("linkBrowserStats") LinkBrowserStatsDO linkBrowserStatsDO);
    
    /**
     * 根据短链接获取指定日期内浏览器监控数据
     */
    @Select("""
        SELECT tlbs.browser,
               SUM(tlbs.cnt) AS cnt
        FROM t_link_browser_stats tlbs
                 INNER JOIN t_link tl
                            ON tlbs.full_short_url = tl.full_short_url
        WHERE tlbs.full_short_url  = #{param.fullShortUrl}
          AND tl.gid =              #{param.gid}
          AND tl.del_flag = '0'
          AND tl.enable_status =    #{param.enableStatus}
          AND tlbs.date BETWEEN     #{param.startDate} and #{param.endDate}
        GROUP BY tlbs.full_short_url, tl.gid, tlbs.browser;""")
    List<HashMap<String, Object>> listBrowserStatsByShortLink(@Param("param") ShortLinkStatsReqDTO requestParam);
    
    /**
     * 分组根据短链接获取指定日期内浏览器监控数据
     */
    @Select("""
        SELECT tlbs.browser,
               SUM(tlbs.cnt) AS cnt
        FROM t_link_browser_stats tlbs
                 INNER JOIN t_link tl
                            ON tlbs.full_short_url = tl.full_short_url
        WHERE tl.gid =              #{param.gid}
          AND tl.del_flag = '0'
          AND tl.enable_status =    #{param.enableStatus}
          AND tlbs.date BETWEEN     #{param.startDate} and #{param.endDate}
        GROUP BY tlbs.full_short_url, tl.gid, tlbs.browser;""")
    List<HashMap<String, Object>> listBrowserStatsByShortLinkGroup(@Param("param") ShortLinkStatsGroupReqDTO requestParam);
}
