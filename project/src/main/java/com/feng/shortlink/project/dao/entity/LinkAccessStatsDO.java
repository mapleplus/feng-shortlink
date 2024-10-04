package com.feng.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.Date;
/**
 * @author FENGXIN
 * @date 2024/10/4
 * @project feng-shortlink
 * @description
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_link_access_stats")
public class LinkAccessStatsDO{
    // 主键ID
    private Long id;
    
    // 分组标识
    private String gid;
    
    // 完整短链接
    private String fullShortUrl;
    
    // 日期
    private Date date;
    
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
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 更新时间
     */
    private Date updateTime;
    
    /**
     * del flag
     */
    private Integer delFlag;
    
}
