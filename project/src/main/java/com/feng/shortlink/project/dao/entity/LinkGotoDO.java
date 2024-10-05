package com.feng.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author FENGXIN
 * @date 2024/10/2
 * @project feng-shortlink
 * @description 短链接路由实体
 **/
@Data
@TableName("t_link_goto")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinkGotoDO {
    /**
     * ID
     */
    private Long id;
    
    /**
     * 分组标识
     */
    private String gid;
    
    /**
     * 完整短 URL
     */
    private String fullShortUrl;
}
