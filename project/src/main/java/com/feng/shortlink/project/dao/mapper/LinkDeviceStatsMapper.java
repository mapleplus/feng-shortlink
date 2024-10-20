package com.feng.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.feng.shortlink.project.dao.entity.LinkDeviceStatsDO;
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
 * @description 设备统计信息mapper
 **/
public interface LinkDeviceStatsMapper extends BaseMapper<LinkDeviceStatsDO> {
    /**
     * 记录访问设备监控数据
     */
    @Insert("INSERT INTO t_link_device_stats (full_short_url, date, cnt, device, create_time, update_time, del_flag) " +
            "VALUES( #{linkDeviceStats.fullShortUrl}, #{linkDeviceStats.date}, #{linkDeviceStats.cnt}, #{linkDeviceStats.device},DATE_ADD(NOW(), INTERVAL 8 HOUR), DATE_ADD(NOW(), INTERVAL 8 HOUR), 0) " +
            "ON DUPLICATE KEY UPDATE cnt = cnt +  #{linkDeviceStats.cnt},update_time = VALUES(update_time);")
    void shortLinkDeviceState(@Param("linkDeviceStats") LinkDeviceStatsDO linkDeviceStatsDO);
    
    /**
     * 根据短链接获取指定日期内访问设备监控数据
     */
    @Select("""
        SELECT tlds.device,
               SUM(tlds.cnt) AS cnt
        FROM t_link_device_stats tlds
                 INNER JOIN t_link tl
                            ON tlds.full_short_url = tl.full_short_url
        WHERE tlds.full_short_url  = #{param.fullShortUrl}
          AND tl.gid =              #{param.gid}
          AND tl.del_flag = '0'
          AND tlds.date BETWEEN     #{param.startDate} and #{param.endDate}
        GROUP BY tlds.full_short_url, tl.gid, tlds.device;""")
    List<LinkDeviceStatsDO> listDeviceStatsByShortLink(@Param("param") ShortLinkStatsReqDTO requestParam);
    
    /**
     * 分组根据短链接获取指定日期内访问设备监控数据
     */
    @Select("""
        SELECT tlds.device,
               SUM(tlds.cnt) AS cnt
        FROM t_link_device_stats tlds
                 INNER JOIN t_link tl
                            ON tlds.full_short_url = tl.full_short_url
        WHERE tl.gid =              #{param.gid}
          AND tl.del_flag = '0'
          AND tlds.date BETWEEN     #{param.startDate} and #{param.endDate}
        GROUP BY tl.gid, tlds.device;""")
    List<LinkDeviceStatsDO> listDeviceStatsByShortLinkGroup(@Param("param") ShortLinkStatsGroupReqDTO requestParam);
}
