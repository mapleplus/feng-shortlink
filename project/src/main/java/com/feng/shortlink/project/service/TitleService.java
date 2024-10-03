package com.feng.shortlink.project.service;

/**
 * @author FENGXIN
 * @date 2024/10/2
 * @project feng-shortlink
 * @description 获取网站标题业务接口
 **/
public interface TitleService {
    
    /**
     * 获取标题
     *
     * @param url 网站地址
     * @return 网站标题
     */
    String getTitle (String url);
}
