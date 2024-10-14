package com.feng.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.feng.shortlink.project.common.database.BaseDO;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @author FENGXIN
 * @date 2024/10/6
 * @project feng-shortlink
 * @description
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_link_stats_today")
public class LinkStatsTodayDO extends BaseDO {
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 短链接
     */
    private String fullShortUrl;
    
    /**
     * 日期
     */
    private LocalDateTime date;
    
    /**
     * 今日PV
     */
    private Integer todayPv;
    
    /**
     * 今日UV
     */
    private Integer todayUv;
    
    /**
     * 今日IP数
     */
    private Integer todayUip;
    
    /**
     * 删除时间
     */
    private Long delTime;
}
