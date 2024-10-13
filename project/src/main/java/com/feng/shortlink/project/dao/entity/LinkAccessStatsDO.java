package com.feng.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.feng.shortlink.project.common.database.BaseDO;
import lombok.*;

import java.time.LocalDateTime;
/**
 * @author FENGXIN
 * @date 2024/10/4
 * @project feng-shortlink
 * @description 短链接监控 基础数据统计
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_link_access_stats")
public class LinkAccessStatsDO extends BaseDO {
    // 主键ID
    private Long id;
    
    // 完整短链接
    private String fullShortUrl;
    
    // 日期
    private LocalDateTime date;
    
    // 访问量
    private Integer pv;
    
    // 独立访问数
    private Integer uv;
    
    // 独立IP数
    private Integer uip;
    
    /**
     * 小时
     */
    private Integer hour;
    
    // 星期
    private Integer weekday;
    
}
