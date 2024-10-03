package com.feng.shortlink.project.service.impl;

import com.feng.shortlink.project.service.TitleService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.rmi.UnknownHostException;

/**
 * @author FENGXIN
 * @date 2024/10/2
 * @project feng-shortlink
 * @description 获取网站标题业务实现
 **/
@Service
public class TitleServiceImpl implements TitleService {
    
    @Override
    public String getTitle (String url) {
        try {
            // 通过Jsoup连接到指定的URL并解析HTML文档
            Document document = Jsoup.connect(url)
                    // 设置超时时间
                    .timeout(5000)
                    .get();
            // 从HTML文档中提取<title>标签内容
            Element titleElement = document.selectFirst("title");
            // 检查是否存在<title>标签
            if (titleElement != null) {
                return titleElement.text();
            } else {
                return "页面中未找到标题。";
            }
            
        } catch (MalformedURLException e) {
            return "URL格式不正确：" + e.getMessage();
        } catch (SocketTimeoutException e) {
            return "连接超时，无法访问该网站。";
        } catch (UnknownHostException e) {
            return "无法解析主机：" + e.getMessage();
        } catch (IOException e) {
            return "获取页面失败：" + e.getMessage();
        } catch (Exception e) {
            return "发生了一个意外错误：" + e.getMessage();
        }
    }
    
}
