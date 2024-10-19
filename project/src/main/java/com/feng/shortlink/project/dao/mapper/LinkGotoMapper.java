package com.feng.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.feng.shortlink.project.dao.entity.LinkGotoDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author FENGXIN
 * @date 2024/9/29
 * @project feng-shortlink
 * @description 短链接路由mapper
 **/
public interface LinkGotoMapper extends BaseMapper<LinkGotoDO> {
    
    @Select ("""
        SELECT  id,gid,full_short_url  FROM t_link_goto     WHERE (full_short_url = #{param})
    """)
    public LinkGotoDO selectGoto(@Param ("param") String param);
}
