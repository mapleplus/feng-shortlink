package com.feng.shortlink.admin.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.feng.shortlink.admin.common.database.BaseDO;
import lombok.*;

/**
 * @author FENGXIN
 * @date 2024/9/28
 * @project feng-shortlink
 * @description 短链接分组实体类
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("t_group")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupDO extends BaseDO {
    private Long id;
    
    private String gid;
    
    private String name;
    
    private String username;
    
    private Integer sortOrder;
    
}
