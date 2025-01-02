package com.feng.shortlink.admin.common.biz.user;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

/**
 * @author FENGXIN
 */
@Slf4j
@Component
public class UserTransmitInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle (@Nullable HttpServletRequest request , @Nullable HttpServletResponse response , @Nullable Object handler) throws Exception {
        if (request != null) {
            String userName = Optional.ofNullable (request.getHeader ("username"))
                    .orElse ("");
            String userId =  Optional.ofNullable (request.getHeader ("userId"))
                    .orElse ("");
            String realName = Optional.ofNullable (request.getHeader ("realName"))
                    .orElse("-");
            UserInfoDTO userInfoDTO = new UserInfoDTO (userId, userName, realName);
            UserContext.setUser(userInfoDTO);
            log.info ("userId: {} username: {} realName: {}", userId, userName, realName);
        }
        
        return true;
    }
    
    @Override
    public void afterCompletion(@Nullable HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Object handler, Exception exception) throws Exception {
        UserContext.removeUser();
    }
}
