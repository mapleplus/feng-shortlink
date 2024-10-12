package com.feng.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.feng.shortlink.project.common.database.BaseDO;
import lombok.*;

import java.util.Date;

/**
 * @author FENGXIN
 * @date 2024/10/5
 * @project feng-shortlink
 * @description 操作系统统计信息实体
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_link_os_stats")
public class LinkOsStatsDO extends BaseDO {
    
    /**
     * 自增主键 ID
     */
    private Long id;
    
    /**
     * 完整短链接
     */
    private String fullShortUrl;
    
    /**
     * 统计日期
     */
    private Date date;
    
    /**
     * 访问量
     */
    private Integer cnt;
    
    /**
     * 访问的操作系统
     */
    private String os;
    
}
