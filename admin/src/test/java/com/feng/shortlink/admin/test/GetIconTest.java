package com.feng.shortlink.admin.test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * @author FENGXIN
 * @date 2024/10/19
 * @project feng-shortlink
 * @description
 **/
public class GetIconTest {
    public static void main (String[] args) {
        String url = "https://www.bilibili.com/";
        System.out.println (getFavicon (url));
    }
    public static String getFavicon(String url) {
        try {
            // 通过Jsoup连接到指定的URL并解析HTML文档
            Document document = Jsoup.connect(url)
                    // 设置超时时间
                    .timeout(5000)
                    .get();
            // 尝试查找<link>标签中包含favicon的元素
            Element iconElement = document.select("link[rel~=(icon|shortcut icon)]").first();
            if (iconElement != null) {
                String iconUrl = iconElement.attr("href");
                return resolveUrl(iconUrl);
            } else {
                return "未找到网站图标";
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    private static String resolveUrl(String iconUrl) {
        if (iconUrl.startsWith("http://") || iconUrl.startsWith("https://")) {
            // 如果是绝对路径，直接返回
            return iconUrl;
        } else {
            // 如果是相对路径，拼接成绝对路径
            // 根据需要，可以使用URL的解析方法
            return "https:" + iconUrl;
        }
    }
}
