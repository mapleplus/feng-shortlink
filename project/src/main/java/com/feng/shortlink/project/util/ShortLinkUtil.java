package com.feng.shortlink.project.util;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;

import java.net.URI;
import java.time.LocalDateTime;
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
    /**
     * 获取短链接有效时间
     *
     * @param validTime 有效时间
     * @return {@code Long }
     */
    public static Long getShortLinkValidTime(LocalDateTime validTime) {
        return Optional.ofNullable(validTime)
                .map(each -> {
                    // 将 LocalDateTime 转换为 Date
                    Date validDate = DateUtil.date(validTime);
                    return DateUtil.between (new Date (),validDate, DateUnit.MS);
                })
                // 永久设置30天默认有效
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
        } else if (userAgent.contains("macintosh") || userAgent.contains("mac os")) {
            return "Mac OS";
        } else if (userAgent.contains("x11") || userAgent.contains("linux")) {
            return "Linux";
        } else if (userAgent.contains("android")) {
            return "Android";
        } else if (userAgent.contains("iphone") || userAgent.contains("ipad")) {
            return "iOS";
        } else if (userAgent.contains("unix")) {
            return "Unix";
        } else if (userAgent.contains("chrome os")) {
            return "Chrome OS";
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
        String userAgent = request.getHeader("User-Agent").toLowerCase();
        
        if (userAgent.contains("mobile")) {
            if (userAgent.contains("tablet")) {
                return "Tablet";
            }
            return "Mobile";
        } else {
            return "PC";
        }
    }
    
    /**
     * 获取用户访问的网络信息
     *
     * @param request HTTP 请求对象
     * @return 用户的网络信息
     */
    public static String getUserNetwork(HttpServletRequest request) {
        String actualIp = getUserIpAddress(request);
        // 这里简单判断IP地址范围，您可能需要更复杂的逻辑
        // 例如，通过调用IP地址库或调用第三方服务来判断网络类型
        return actualIp.startsWith("192.168.") || actualIp.startsWith("10.") ? "WIFI" : "Mobile";
    }
    
    
    /**
     * 获取原始链接中的域名
     * 如果原始链接包含 www 开头的话需要去掉
     *
     * @param url 创建或者修改短链接的原始链接
     * @return 原始链接中的域名
     */
    public static String extractDomain(String url) {
        String domain = null;
        try {
            URI uri = new URI(url);
            String host = uri.getHost();
            if (StrUtil.isNotBlank(host)) {
                domain = host;
                if (domain.startsWith("www.")) {
                    domain = host.substring(4);
                }
            }
        } catch (Exception ignored) {
        }
        return domain;
    }
}
