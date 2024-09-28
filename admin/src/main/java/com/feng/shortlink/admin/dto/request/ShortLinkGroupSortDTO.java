package com.feng.shortlink.admin.dto.request;

import lombok.Data;

/**
 * @author FENGXIN
 * @date 2024/9/28
 * @project feng-shortlink
 * @description 新增短链接分组请求参数
 **/
@Data
public class ShortLinkGroupSortDTO {
    /**
     * 分组 ID
     */
    private String gid;
    
    /**
     * 排序顺序
     */
    private Integer sortOrder;
}
