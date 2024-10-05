package com.feng.shortlink.project.util;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Date;
import java.util.Optional;

import static com.feng.shortlink.project.common.constant.ShortLinkConstant.SHORT_LINK_VALID_TIME;

/**
 * @author FENGXIN
 * @date 2024/10/2
 * @project feng-shortlink
 * @description
 **/
public class ShortLinkUtil {
    public static Long getShortLinkValidTime(Date validTime) {
        return Optional.ofNullable(validTime)
                .map(each -> DateUtil.between (new Date (),validTime, DateUnit.MS))
                .orElse(SHORT_LINK_VALID_TIME);
    }
    
    /**
     * 获取用户请求的 IP 地址
     *
     * @param request HttpServletRequest 对象
     * @return 用户的 IP 地址
     */
    public static String getUserIpAddress(HttpServletRequest request) {
        // 先尝试从 X-Forwarded-For 获取
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            // 尝试从 Proxy-Client-IP 获取
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            // 尝试从 WL-Proxy-Client-IP 获取
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            // 最后使用 getRemoteAddr 获取
            ipAddress = request.getRemoteAddr();
        }
        
        // 有可能 X-Forwarded-For 返回的是多个 IP 地址，取第一个
        if (ipAddress != null && ipAddress.contains(",")) {
            ipAddress = ipAddress.split(",")[0].trim();
        }
        
        return ipAddress;
    }
    
    /**
     * 获取用户操作系统的方法
     *
     * @param request HttpServletRequest对象
     * @return 操作系统名称
     */
    public static String getOperatingSystem(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent").toLowerCase();
        if (userAgent.contains("windows")) {
            return "Windows";
        } else if (userAgent.contains("mac")) {
            return "Mac OS";
        } else if (userAgent.contains("x11") || userAgent.contains("linux")) {
            return "Linux";
        } else if (userAgent.contains("android")) {
            return "Android";
        } else if (userAgent.contains("iphone") || userAgent.contains("ipad")) {
            return "iOS";
        } else {
            return "Unknown";
        }
    }
    
    /**
     * 获取用户访问浏览器
     *
     * @param request 请求
     * @return 访问浏览器
     */
    public static String getBrowser(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent.toLowerCase().contains("edg")) {
            return "Microsoft Edge";
        } else if (userAgent.toLowerCase().contains("chrome")) {
            return "Google Chrome";
        } else if (userAgent.toLowerCase().contains("firefox")) {
            return "Mozilla Firefox";
        } else if (userAgent.toLowerCase().contains("safari")) {
            return "Apple Safari";
        } else if (userAgent.toLowerCase().contains("opera")) {
            return "Opera";
        } else if (userAgent.toLowerCase().contains("msie") || userAgent.toLowerCase().contains("trident")) {
            return "Internet Explorer";
        } else {
            return "Unknown";
        }
    }
    
    /**
     * 获取用户访问设备
     *
     * @param request 请求
     * @return 访问设备
     */
    public static String getDevice(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent.toLowerCase().contains("mobile")) {
            return "Mobile";
        }
        return "PC";
    }
}
