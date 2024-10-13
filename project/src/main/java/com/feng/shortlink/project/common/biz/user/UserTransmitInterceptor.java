package com.feng.shortlink.project.common.biz.user;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author FENGXIN
 * @date 2024/10/13
 * @project feng-shortlink
 * @description usercontext拦截器
 **/
@Component
public class UserTransmitInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(@Nullable HttpServletRequest request, @Nullable HttpServletResponse response,@Nullable Object handler) throws Exception {
        String username = null;
        if (request != null) {
            username = request.getHeader ("username");
        }
        if (StrUtil.isNotBlank (username)) {
            String realName = request.getHeader ("realName");
            String userId = request.getHeader ("userId");
            UserInfoDTO userInfoDTO = new UserInfoDTO(userId,username,realName);
            UserContext.setUser (userInfoDTO);
        }
        return true;
    }
    
    @Override
    public void afterCompletion(@Nullable HttpServletRequest request,@Nullable HttpServletResponse response,@Nullable Object handler, @Nullable Exception ex) throws Exception {
        UserContext.removeUser();
    }
}
