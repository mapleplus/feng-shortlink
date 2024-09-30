package com.feng.shortlink.project.dto.request;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.feng.shortlink.project.dao.entity.ShortLinkDO;
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
public class ShortLinkPageReqDTO extends Page<ShortLinkDO> {
    /**
     * 分组标识
     */
    private String gid;
}
