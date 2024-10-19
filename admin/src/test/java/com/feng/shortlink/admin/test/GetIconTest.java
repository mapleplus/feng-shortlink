package com.feng.shortlink.admin.test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class GetIconTest {
    public static void main(String[] args) {
        String url = "https://taobao.com";
        System.out.println(getFavicon(url));
    }
    
    public static String getFavicon(String url) {
        try {
            Document document = Jsoup.connect(url)
                    .timeout(5000)
                    .get();
            // 查找link标签中可能的favicon元素
            Element iconElement = document.select("link[rel~=(icon|shortcut icon|apple-touch-icon)]").first();
            if (iconElement != null) {
                return resolveUrl(iconElement.attr("href"), url);
            } else {
                // 尝试使用默认路径
                return resolveUrl("/favicon.ico", url);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    private static String resolveUrl(String iconUrl, String baseUrl) {
        if (iconUrl.startsWith("http://") || iconUrl.startsWith("https://")) {
            return iconUrl;
        } else {
            // 使用URL拼接生成绝对路径
            try {
                return new java.net.URI(new java.net.URI(baseUrl).resolve(iconUrl).toString()).toString();
            } catch (Exception e) {
                return "图标路径解析失败";
            }
        }
    }
}