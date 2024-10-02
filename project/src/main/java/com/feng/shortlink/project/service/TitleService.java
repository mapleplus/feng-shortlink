package com.feng.shortlink.project.service;

/**
 * @author FENGXIN
 * @date 2024/10/2
 * @project feng-shortlink
 * @description
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
