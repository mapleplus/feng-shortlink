package com.feng.shortlink.project.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.sql.Date;

/**
 * @author FENGXIN
 * @date 2024/10/1
 * @project feng-shortlink
 * @description 修改短链接请求参数
 **/
@Data
@RequiredArgsConstructor
public class ShortLinkUpdateReqDTO {
    
    /** 完整短链接 */
    private String fullShortUrl;
    
    /** 分组标识 */
    private String gid;
    
    /** 原始链接 */
    private String originUrl;
    
    /** 网站图标 */
    private String favicon;
    
    /** 有效期类型 0：永久有效 1：用户自定义 */
    private Integer validDateType;
    
    /** 有效期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT + 8")
    private Date validDate;
    
    /** 描述 */
    private String describe;
}
