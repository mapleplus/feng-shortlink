package com.feng.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.feng.shortlink.project.dao.entity.ShortLinkDO;
import com.feng.shortlink.project.dto.request.ShortLinkPageReqDTO;
import com.feng.shortlink.project.dto.request.ShortLinkUpdatePvUvUipDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author FENGXIN
 * @date 2024/9/29
 * @project feng-shortlink
 * @description 短链接mapper
 **/
public interface ShortLinkMapper extends BaseMapper<ShortLinkDO> {
    @Update ("""
    update t_link set total_pv = total_pv + #{requestParam.totalPv},total_uv = total_uv + #{requestParam.totalUv},total_uip = total_uip + #{requestParam.totalUip}
    where gid = #{requestParam.gid} and full_short_url = #{requestParam.fullShortUrl};
    """)
    void totalPvUvUipUpdate(@Param ("requestParam") ShortLinkUpdatePvUvUipDO requestParam);
    
    /**
     * 分页统计短链接
     */
    IPage<ShortLinkDO> pageLink(ShortLinkPageReqDTO requestParam);
}
