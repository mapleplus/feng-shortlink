package com.feng.shortlink.project.dto.response;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.sql.Date;

/**
 * @author FENGXIN
 * @date 2024/9/30
 * @project feng-shortlink
 * @description 分页查询响应实体
 **/
@Data
public class ShortLinkPageRespDTO {
    
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
    
    /** 分组标识 */
    private String gid;
    
    /**
     * 网站图标
     */
    private String favicon;
    
    /** 有效期类型 0：永久有效 1：用户自定义 */
    private Integer validDateType;
    
    /** 有效期 */
    private Date validDate;
    
    /** 描述 */
    @TableField("`describe`")
    private String describe;
    
}
