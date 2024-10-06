package com.feng.shortlink.admin.remote.dto.request;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.feng.shortlink.admin.dao.entity.GroupDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author FENGXIN
 * @date 2024/9/30
 * @project feng-shortlink
 * @description 分页查请求参数
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class ShortLinkPageReqDTO extends Page<GroupDO> {
    /**
     * 分组标识
     */
    private String gid;
    
    /**
     * 排序标签
     */
    private String orderTag;
}
