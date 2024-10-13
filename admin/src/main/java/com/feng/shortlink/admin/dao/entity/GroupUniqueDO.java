package com.feng.shortlink.admin.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

/**
 * @author FENGXIN
 * @date 2024/10/13
 * @project feng-shortlink
 * @description 短链接分组唯一gid实体
 **/
@Data
@TableName("t_group_unique")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupUniqueDO {
    /**
     * id
     */
    private Long id;
    
    /**
     * 分组标识
     */
    private String gid;
}
