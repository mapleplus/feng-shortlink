package com.feng.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.feng.shortlink.project.common.database.BaseDO;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @author FENGXIN
 * @date 2024/10/5
 * @project feng-shortlink
 * @description 网络统计信息实体
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_link_network_stats")
public class LinkNetworkStatsDO extends BaseDO {
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 完整短链接
     */
    private String fullShortUrl;
    
    /**
     * 访问日期
     */
    private LocalDateTime date;
    
    /**
     * 访问量
     */
    private Integer cnt;
    
    /**
     * 访问网络
     */
    private String network;
    
    /**
     * 删除时间
     */
    private Long delTime;
}
