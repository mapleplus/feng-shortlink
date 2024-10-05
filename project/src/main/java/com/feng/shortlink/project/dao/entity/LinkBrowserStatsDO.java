package com.feng.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.feng.shortlink.project.common.database.BaseDO;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.Date;

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
     * 分组标识，默认值为 'default'
     */
    private String gid ;
    
    /**
     * 统计日期
     */
    private Date date;
    
    /**
     * 访问量
     */
    private Integer cnt;
    
    /**
     * 访问的浏览器类型
     */
    private String browser;
    
}
