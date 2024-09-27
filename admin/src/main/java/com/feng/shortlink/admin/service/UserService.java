package com.feng.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feng.shortlink.admin.dao.entity.UserDO;
import com.feng.shortlink.admin.dto.request.RegisterUserReqDTO;
import com.feng.shortlink.admin.dto.request.UpdateUserReqDTO;
import com.feng.shortlink.admin.dto.response.UserRespDTO;

/**
 * @author FENGXIN
 * @date 2024/9/26
 * @project feng-shortlink
 * @description
 **/
public interface UserService extends IService<UserDO>{
    UserRespDTO getUserByUserName (String username);
    
    Boolean hasUserName (String username);
    
    void registerUser (RegisterUserReqDTO requestParams);
    
    void updateUser (UpdateUserReqDTO requestParams);
}
