package com.feng.shortlink.admin.test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class TestClass {
 
    public static void main(String[] args) {
        String url = "https://douyin.com";
        System.out.println(getFavicon (url));
        System.out.println (getTitle (url));
    }
    
    public static String getFavicon(String url) {
        return url + "/favicon.ico";
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