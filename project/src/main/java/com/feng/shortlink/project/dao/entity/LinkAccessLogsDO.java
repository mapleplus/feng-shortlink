package com.feng.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.feng.shortlink.project.common.database.BaseDO;
import lombok.*;

/**
 * @author FENGXIN
 * @date 2024/10/5
 * @project feng-shortlink
 * @description 访问日志信息实体 用于统计新老访客 高频访问ip
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_link_access_logs")
public class LinkAccessLogsDO extends BaseDO {
    /**
     * 自增主键 ID
     */
    private Long id;
    
    /**
     * 完整短链接
     */
    private String fullShortUrl;
    
    /**
     * 分组标识
     */
    private String gid;
    
    /**
     * 用户信息
     */
    private String user;
    
    /**
     * 浏览器信息
     */
    private String browser;
    
    /**
     * 操作系统信息
     */
    private String os;
    
    /**
     * IP 地址
     */
    private String ip;
    
    /**
     * 访问量
     */
    private Integer cnt;
}
