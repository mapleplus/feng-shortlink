package com.feng.shortlink.admin.remote.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * @author FENGXIN
 * @date 2024/9/29
 * @project feng-shortlink
 * @description 短链接创建请求参数
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShortLinkSaveReqDTO {
    
    /** 域名 */
    private String domain;
    
    /** 原始链接 */
    private String originUrl;
    
    /** 分组标识 */
    private String gid;
    
    /**
     * 网站图标
     */
    private String favicon;
    
    /** 创建类型 0：接口 1：控制台 */
    private Integer createdType;
    
    /** 有效期类型 0：永久有效 1：用户自定义 */
    private Integer validDateType;
    
    /** 有效期 */
    private Date validDate;
    
    /** 描述 */
    private String describe;
}
