package com.feng.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.feng.shortlink.project.dao.entity.LinkNetworkStatsDO;
import com.feng.shortlink.project.dto.request.ShortLinkStatsGroupReqDTO;
import com.feng.shortlink.project.dto.request.ShortLinkStatsReqDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author FENGXIN
 * @date 2024/10/5
 * @project feng-shortlink
 * @description 网络统计信息mapper
 **/
public interface LinkNetworkStatsMapper extends BaseMapper<LinkNetworkStatsDO> {
    /**
     * 记录访问设备监控数据
     */
    @Insert("INSERT INTO t_link_network_stats (full_short_url, date, cnt, network, create_time, update_time, del_flag) " +
            "VALUES( #{linkNetworkStats.fullShortUrl}, #{linkNetworkStats.date}, #{linkNetworkStats.cnt}, #{linkNetworkStats.network},NOW(),NOW(),0) " +
            "ON DUPLICATE KEY UPDATE cnt = cnt +  #{linkNetworkStats.cnt},update_time = VALUES(update_time);")
    void shortLinkNetworkState(@Param("linkNetworkStats") LinkNetworkStatsDO linkNetworkStatsDO);
    
    /**
     * 根据短链接获取指定日期内访问网络监控数据
     */
    @Select("""
        SELECT tlns.network,
               SUM(tlns.cnt) AS cnt
        FROM t_link_network_stats tlns
                 INNER JOIN t_link tl
                            ON tlns.full_short_url = tl.full_short_url
        WHERE tlns.full_short_url  = #{param.fullShortUrl}
          AND tl.gid =              #{param.gid}
          AND tl.del_flag = '0'
          AND tlns.date BETWEEN     #{param.startDate} and #{param.endDate}
        GROUP BY tlns.full_short_url, tl.gid, tlns.network;""")
    List<LinkNetworkStatsDO> listNetworkStatsByShortLink(@Param("param") ShortLinkStatsReqDTO requestParam);
    
    /**
     * 分组根据短链接获取指定日期内访问网络监控数据
     */
    @Select("""
        SELECT tlns.network,
               SUM(tlns.cnt) AS cnt
        FROM t_link_network_stats tlns
                 INNER JOIN t_link tl
                            ON tlns.full_short_url = tl.full_short_url
        WHERE tl.gid =              #{param.gid}
          AND tl.del_flag = '0'
          AND tlns.date BETWEEN     #{param.startDate} and #{param.endDate}
        GROUP BY tl.gid, tlns.network;""")
    List<LinkNetworkStatsDO> listNetworkStatsByShortLinkGroup(@Param("param") ShortLinkStatsGroupReqDTO requestParam);
}
