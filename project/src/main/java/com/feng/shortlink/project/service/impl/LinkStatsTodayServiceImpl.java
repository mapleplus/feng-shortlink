package com.feng.shortlink.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feng.shortlink.project.dao.entity.LinkStatsTodayDO;
import com.feng.shortlink.project.dao.mapper.LinkStatsTodayMapper;
import com.feng.shortlink.project.service.LinkStatsTodayService;
import org.springframework.stereotype.Service;

/**
 * @author FENGXIN
 * @date 2024/10/8
 * @project feng-shortlink
 * @description 短链接今日统计接口实现层
 **/
@Service
public class LinkStatsTodayServiceImpl extends ServiceImpl<LinkStatsTodayMapper, LinkStatsTodayDO> implements LinkStatsTodayService {
}
