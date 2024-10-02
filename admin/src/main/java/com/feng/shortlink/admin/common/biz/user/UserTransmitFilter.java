package com.feng.shortlink.admin.common.biz.user;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.feng.shortlink.admin.common.constant.RedisCacheConstant;
import com.feng.shortlink.admin.common.convention.exception.ClientException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.IOException;
import java.util.List;

import static com.feng.shortlink.admin.common.enums.UserErrorCodeEnum.USER_NAME_TOKEN_ERROR;

/**
 * @author FENGXIN
 * @date 2024/9/28
 * @project feng-shortlink
 * @description 用户信息传输过滤器
 **/
@RequiredArgsConstructor
public class UserTransmitFilter implements Filter {
    
    private final StringRedisTemplate stringRedisTemplate;
    // 放行的url
    private final List<String> IGNORE_URL = List.of (
            "/api/fenglink/v1/admin/user/login",
            "/api/fenglink/v1/admin/user/has-username",
            "/api/fenglink/v1/admin/user",
            "/api/fenglink/v1/admin/title"
    );
    @Override
    public void doFilter(ServletRequest servletRequest
            , ServletResponse servletResponse
            , FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String requestUri = httpServletRequest.getRequestURI ();
        
        // 其它url时将用户存入userContext
        // 如果包含则放行
        if(!IGNORE_URL.contains (requestUri)){
            if(!("/api/fenglink/v1/admin/user".equals (requestUri) && "POST".equals (httpServletRequest.getMethod ()))){
                // 获取头信息
                String userName = httpServletRequest.getHeader("username");
                String token = httpServletRequest.getHeader("token");
                // 处理请求头信息验证异常
                if (StrUtil.isBlank(userName) || StrUtil.isBlank(token)) {
                    throw new ClientException(USER_NAME_TOKEN_ERROR);
                }
                // 处理redis查询登录用户异常
                Object strUserInfoDTO;
                try {
                    strUserInfoDTO = stringRedisTemplate
                            .opsForHash ()
                            .get (RedisCacheConstant.SHORTLINK_USER_LOGIN_KEY + userName , token);
                    if (strUserInfoDTO == null) {
                        throw new ClientException (USER_NAME_TOKEN_ERROR);
                    }
                } catch (Exception e) {
                    throw new ClientException (USER_NAME_TOKEN_ERROR);
                }
                // 存入context
                UserInfoDTO userInfoDTO = JSON.parseObject (strUserInfoDTO.toString () , UserInfoDTO.class);
                UserContext.setUser(userInfoDTO);
            }
        }
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            UserContext.removeUser();
        }
    }
}
