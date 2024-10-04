package com.feng.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.feng.shortlink.project.common.database.BaseDO;
import lombok.*;

import java.util.Date;

/**
 * @author FENGXIN
 * @date 2024/10/4
 * @project feng-shortlink
 * @description 短链接监控 地区统计
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_link_locale_stats")
public class LinkLocaleStatsDO extends BaseDO {
    /**
     * ID
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
     * 日期
     */
    private Date date;
    
    /**
     * 访问量
     */
    private Integer cnt;
    
    /**
     * 省份名称
     */
    private String province;
    
    /**
     * 市名称
     */
    private String city;
    
    /**
     * 城市编码
     */
    private String adcode;
    
    /**
     * 国家标识
     */
    private String country;
}
