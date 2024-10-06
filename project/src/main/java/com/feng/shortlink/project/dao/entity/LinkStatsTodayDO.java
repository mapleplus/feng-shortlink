package com.feng.shortlink.project.dao.entity;

import com.feng.shortlink.project.common.database.BaseDO;
import lombok.*;

import java.util.Date;

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
public class LinkStatsTodayDO extends BaseDO {
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 分组标识
     */
    private String gid;
    
    /**
     * 短链接
     */
    private String fullShortUrl;
    
    /**
     * 日期
     */
    private Date date;
    
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
}
