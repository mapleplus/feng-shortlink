package com.feng.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.feng.shortlink.project.common.database.BaseDO;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

/**
 * @author FENGXIN
 * @date 2024/10/5
 * @project feng-shortlink
 * @description 浏览器统计信息实体
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_link_browser_stats")
public class LinkBrowserStatsDO extends BaseDO {
    
    /**
     * 自增主键 ID
     */
    @Id
    private Long id;
    
    /**
     * 完整短链接
     */
    private String fullShortUrl;
    
    /**
     * 统计日期
     */
    private LocalDateTime date;
    
    /**
     * 访问量
     */
    private Integer cnt;
    
    /**
     * 访问的浏览器类型
     */
    private String browser;
    
    /**
     * 删除时间
     */
    private Long delTime;
}
