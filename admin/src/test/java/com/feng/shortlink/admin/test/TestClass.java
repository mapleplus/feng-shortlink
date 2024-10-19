package com.feng.shortlink.admin.test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class TestClass {
 
    public static void main(String[] args) {
        String url = "https://douyin.com";
        // System.out.println(getFavicon (url));
        System.out.println (getTitle (url));
    }
    
    public static String getFavicon(String url) {
        try {
            Document document = Jsoup.connect(url).timeout(5000).get();
            Element titleElement = document.selectFirst("title");
            if (titleElement != null) {
                return titleElement.text();
            }
            Element metaTitleElement = document.selectFirst("meta[name=title]");
            return metaTitleElement != null ? metaTitleElement.attr("content") : "页面中未找到标题。";
        } catch (Exception e) {
            return "发生错误：" + e.getMessage();
        }
    }
    public static String getTitle(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            return doc.title();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
}