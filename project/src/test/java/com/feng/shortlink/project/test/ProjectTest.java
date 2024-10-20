package com.feng.shortlink.project.test;

import com.feng.shortlink.project.dao.entity.LinkDeviceStatsDO;
import com.feng.shortlink.project.dto.request.ShortLinkStatsReqDTO;
import com.feng.shortlink.project.dto.response.ShortLinkStatsDeviceRespDTO;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static com.feng.shortlink.project.service.impl.ShortLinkServiceImpl.millisecondsUntilEndOfDay;

/**
 * @author FENGXIN
 * @date 2024/10/20
 * @project feng-shortlink
 * @description
 **/
@SpringBootTest
@RequiredArgsConstructor
public class ProjectTest {
    @Test
    public void contextLoads() {
        ShortLinkStatsReqDTO requestParam = new ShortLinkStatsReqDTO ();
        requestParam.setFullShortUrl ("s.fxink.cn/287yPG");
        requestParam.setGid ("Q8SXvf");
        requestParam.setStartDate ("2024-10-14 00:00:00");
        requestParam.setEndDate ("2024-10-20 23:59:59");
        LinkDeviceStatsDO linkDeviceStatsDO = LinkDeviceStatsDO.builder ()
                .device ("Mobile")
                .cnt (1)
                .build ();
        LinkDeviceStatsDO linkDeviceStatsDO1 = LinkDeviceStatsDO.builder ()
                .device ("PC")
                .cnt (12)
                .build ();
        List<ShortLinkStatsDeviceRespDTO> shortLinkDeviceRespDTOList = new ArrayList<> ();
        List<LinkDeviceStatsDO> listDeviceStatsByShortLink = new ArrayList<> ();
        listDeviceStatsByShortLink.add (linkDeviceStatsDO);
        listDeviceStatsByShortLink.add (linkDeviceStatsDO1);
        int deviceCnt = 13;
        listDeviceStatsByShortLink.forEach (each ->{
            double ratio = (double) each.getCnt () / deviceCnt;
            double actualRatio = Math.round (ratio * 100.0) / 100.0;
            ShortLinkStatsDeviceRespDTO build = ShortLinkStatsDeviceRespDTO.builder ()
                    .cnt (each.getCnt())
                    .ratio (actualRatio)
                    .device (each.getDevice ())
                    .build ();
            shortLinkDeviceRespDTOList.add (build);
        });
        shortLinkDeviceRespDTOList.forEach (System.out::println);
    }
    
    @Test
    public void timeTest(){
        System.out.println (millisecondsUntilEndOfDay ());
    }
}
