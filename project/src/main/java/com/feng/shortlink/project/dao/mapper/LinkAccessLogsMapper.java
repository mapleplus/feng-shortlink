package com.feng.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.feng.shortlink.project.dao.entity.LinkAccessLogsDO;
import com.feng.shortlink.project.dao.entity.LinkPageStatsDO;
import com.feng.shortlink.project.dao.entity.LinkPageStatsGroupDO;
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
 * @description 访问日志统计信息mapper
 **/
public interface LinkAccessLogsMapper extends BaseMapper<LinkAccessLogsDO> {
    /**
     * 记录浏览器访问监控数据
     */
    @Insert("INSERT INTO t_link_access_logs (full_short_url, user, browser,os,ip,device,network,locale,cnt,create_time, update_time, del_flag) " +
            "VALUES( #{linkAccessLogs.fullShortUrl},  #{linkAccessLogs.user}, #{linkAccessLogs.browser}, #{linkAccessLogs.os},#{linkAccessLogs.ip},#{linkAccessLogs.device},#{linkAccessLogs.network},#{linkAccessLogs.locale},#{linkAccessLogs.cnt},NOW(),NOW(), 0) " +
            "ON DUPLICATE KEY UPDATE cnt = cnt +  #{linkAccessLogs.cnt},update_time = VALUES(update_time);")
    void shortLinkAccessLogState (@Param("linkAccessLogs") LinkAccessLogsDO linkAccessLogsDO);
    
    /**
     * 根据短链接获取指定日期内高频访问IP数据
     */
    @Select("""
        SELECT
           tlal.ip,
           COUNT(tlal.ip) AS count
        FROM
           t_link_access_logs tlal inner join t_link tl ON tl.full_short_url = tlal.full_short_url COLLATE utf8mb4_general_ci
        WHERE
           tlal.full_short_url = #{param.fullShortUrl}
        AND tl.gid = #{param.gid}
        AND tl.del_flag = '0'
        AND tlal.create_time BETWEEN #{param.startDate} and #{param.endDate}
        GROUP BY
          tlal.full_short_url,tl.gid,tlal.ip
        ORDER BY count DESC
        LIMIT 5;
        """)
    List<HashMap<String, Object>> listTopIpByShortLink(@Param("param") ShortLinkStatsReqDTO requestParam);
    
    /**
     * 分组根据短链接获取指定日期内高频访问IP数据
     */
    @Select("""
        SELECT
           tlal.ip,
           COUNT(tlal.ip) AS count
        FROM
           t_link_access_logs tlal inner join t_link tl ON tl.full_short_url = tlal.full_short_url COLLATE utf8mb4_general_ci
        WHERE
            tl.gid =  #{param.gid}
        AND tl.del_flag = '0'
        AND tlal.create_time BETWEEN #{param.startDate} and #{param.endDate}
        GROUP BY
          tl.gid,tlal.ip
        ORDER BY count DESC
        LIMIT 5;""")
    List<HashMap<String, Object>> listTopIpByShortLinkGroup(@Param("param") ShortLinkStatsGroupReqDTO requestParam);
    
    /**
     * 根据短链接获取指定日期内新旧访客数据
     */
    @Select("""
        SELECT
            SUM(old_user) AS oldUserCnt,
            SUM(new_user) AS newUserCnt
        FROM (
            SELECT
                CASE WHEN COUNT(DISTINCT DATE(tlal.create_time)) > 1 THEN 1 ELSE 0 END AS old_user,
                CASE WHEN COUNT(DISTINCT DATE(tlal.create_time)) = 1 AND MAX(tlal.create_time) >= #{param.startDate} AND MAX(tlal.create_time) <= #{param.endDate} THEN 1 ELSE 0 END AS new_user
            FROM
                t_link_access_logs tlal inner join t_link tl
            on tlal.full_short_url = tl.full_short_url COLLATE utf8mb4_general_ci
            WHERE
                tlal.full_short_url = #{param.fullShortUrl}
                AND tl.gid = #{param.gid}
                AND tl.del_flag = '0'
            GROUP BY
                tlal.user
        ) AS user_counts;""")
    HashMap<String, Object> findUvTypeCntByShortLink(@Param("param") ShortLinkStatsReqDTO requestParam);
    
    /**
     * 根据短链接获取指定日期内新旧访客数据
     */
    @Select("""
        SELECT
            SUM(old_user) AS oldUserCnt,
            SUM(new_user) AS newUserCnt
        FROM (
            SELECT
                CASE WHEN COUNT(DISTINCT DATE(tlal.create_time)) > 1 THEN 1 ELSE 0 END AS old_user,
                CASE WHEN COUNT(DISTINCT DATE(tlal.create_time)) = 1 AND MAX(tlal.create_time) >= #{param.startDate} AND MAX(tlal.create_time) <= #{param.endDate} THEN 1 ELSE 0 END AS new_user
            FROM
                t_link_access_logs tlal inner join t_link tl
            on tlal.full_short_url = tl.full_short_url COLLATE utf8mb4_general_ci
            WHERE
                tl.gid = #{param.gid}
                AND tl.del_flag = '0'
            GROUP BY
                tlal.user
        ) AS user_counts;""")
    HashMap<String, Object> findUvTypeCntByShortLinkGroup(@Param("param") ShortLinkStatsGroupReqDTO requestParam);
    
    /**
     * 分页查询短链接新老访客访问记录
     */
    @Select("""
          <script>
           SELECT
               tlal.user,
               CASE
                   WHEN MIN(tlal.create_time) BETWEEN #{requestParam.startDate} AND #{requestParam.endDate} THEN '新访客'
                   ELSE '老访客'
               END AS uvType
           FROM
               t_link_access_logs tlal INNER JOIN t_link tl
          ON tlal.full_short_url = tl.full_short_url COLLATE utf8mb4_general_ci
           WHERE
             tlal.full_short_url = #{requestParam.fullShortUrl}
             AND tl.gid = #{requestParam.gid}
             AND tlal.del_flag = '0'
           AND tlal.user IN
           <foreach item='item' index='index' collection='requestParam.userAccessLogsList' open='(' separator=',' close=')'>
               #{item}
           </foreach>
           GROUP BY
              tlal.user;
          </script>""")
    List<HashMap<String, Object>> listAccessRecordByShortLink(@Param("requestParam") LinkPageStatsDO requestParam);
    
    /**
     * 分组分页查询短链接新老访客访问记录
     */
    @Select("""
          <script>
           SELECT
               tlal.user,
               CASE
                   WHEN MIN(tlal.create_time) BETWEEN #{requestParam.startDate} AND #{requestParam.endDate} THEN '新访客'
                   ELSE '老访客'
               END AS uvType
           FROM
               t_link_access_logs tlal INNER JOIN t_link tl
          ON tlal.full_short_url = tl.full_short_url COLLATE utf8mb4_general_ci
           WHERE
              tl.gid = #{requestParam.gid}
             AND tlal.del_flag = '0'
           AND tlal.user IN
           <foreach item='item' index='index' collection='requestParam.userAccessLogsList' open='(' separator=',' close=')'>
               #{item}
           </foreach>
           GROUP BY
              tlal.user;
          </script>""")
    List<HashMap<String, Object>> listGroupAccessRecordByShortLink(@Param("requestParam") LinkPageStatsGroupDO requestParam);
}