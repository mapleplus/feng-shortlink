package com.feng.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.feng.shortlink.project.common.database.BaseDO;
import lombok.*;

import java.sql.Date;

/**
 * @author FENGXIN
 * @date 2024/9/29
 * @project feng-shortlink
 * @description 短链接实体
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_link")
public class ShortLinkDO extends BaseDO {
    /** ID */
    private Long id;
    
    /** 域名 */
    private String domain;
    
    /** 短链接 */
    private String shortUri;
    
    /** 完整短链接 */
    private String fullShortUrl;
    
    /** 原始链接 */
    private String originUrl;
    
    /** 点击量 */
    private Integer clickNum = 0;
    
    /** 分组标识 */
    private String gid;
    
    /** 网站图标 */
    private String favicon;
    
    /** 启用标识 0：已启用 1：未启用 */
    private Integer enableStatus;
    
    /** 创建类型 0：接口 1：控制台 */
    private Integer createdType;
    
    /** 有效期类型 0：永久有效 1：用户自定义 */
    private Integer validDateType;
    
    /** 有效期 */
    private Date validDate;
    
    /** 描述 */
    @TableField("`describe`")
    private String describe;
    
    /**
     * 总uv
     */
    private Integer  totalUv;
    
    /**
     * 总 PV
     */
    private Integer  totalPv;
    
    /**
     * 总 UIP
     */
    private Integer  totalUip;
    
    /**
     * 今日PV
     */
    @TableField(exist = false)
    private Integer todayPv;
    
    /**
     * 今日UV
     */
    @TableField(exist = false)
    private Integer todayUv;
    
    /**
     * 今日IP数
     */
    @TableField(exist = false)
    private Integer todayUip;
}
