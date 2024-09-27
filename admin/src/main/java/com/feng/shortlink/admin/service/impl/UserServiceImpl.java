package com.feng.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feng.shortlink.admin.common.convention.exception.ClientException;
import com.feng.shortlink.admin.common.enums.UserErrorCodeEnum;
import com.feng.shortlink.admin.dao.entity.UserDO;
import com.feng.shortlink.admin.dao.mapper.UserMapper;
import com.feng.shortlink.admin.dto.request.RegisterUserReqDTO;
import com.feng.shortlink.admin.dto.response.UserRespDTO;
import com.feng.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import static com.feng.shortlink.admin.common.constant.RedisCacheConstant.LOCK_SHORTLINK_USER_REGISTER_KEY;

/**
 * @author FENGXIN
 * @date 2024/9/26
 * @project feng-shortlink
 * @description UserServiceImpl
 **/
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {
    // 构造器注入布隆过滤器
    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;
    // 使用redis
    private final RedissonClient redissonClient;
    /**
     * 根据给定的用户名检索用户。
     *
     * @param username 要检索的用户名。
     * @return 指定用户名的用户详细信息。
     */
    @Override
    public UserRespDTO getUserByUserName (String username) {
        LambdaQueryWrapper<UserDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserDO::getUsername, username);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        // 捕获空异常
        if (userDO == null) {
            throw new ClientException (UserErrorCodeEnum.USER_NULL);
        }
        UserRespDTO result = new UserRespDTO();
        BeanUtils.copyProperties (userDO, result);
        return result;
    }
    
    /**
     * 检查用户名是否存在于Cache中。
     * 布隆过滤器存储用户名
     * @param username 要检查的用户名。
     * @return 如果用户名不存在于数据库中则返回 {@code true}，否则返回 {@code false}。
     */
    @Override
    public Boolean hasUserName (String username) {
        return !userRegisterCachePenetrationBloomFilter.contains (username);
    }
    
    /**
     * 在系统中注册一个新用户。
     *
     * @param registerUserReqDTO 包含要注册用户的详细信息的数据传输对象。
     */
    @Override
    public void registerUser (RegisterUserReqDTO registerUserReqDTO) {
        if (!hasUserName (registerUserReqDTO.getUsername ())){
            throw new ClientException (UserErrorCodeEnum.USER_NAME_EXISTS);
        }
        // 给register username上分布式锁 防止恶意注册
        RLock lock = redissonClient.getLock (LOCK_SHORTLINK_USER_REGISTER_KEY + registerUserReqDTO.getUsername ());
        try {
            boolean tryLock = lock.tryLock ();
            if (tryLock) {
                // 新增用户
                int insert = baseMapper.insert (BeanUtil.toBean (registerUserReqDTO , UserDO.class));
                if (insert < 1) {
                    throw new ClientException (UserErrorCodeEnum.USER_SAVE_ERROR);
                }
                // 添加用户名到布隆过滤器
                userRegisterCachePenetrationBloomFilter.add (registerUserReqDTO.getUsername ());
                return;
            }
            throw new ClientException (UserErrorCodeEnum.USER_NAME_EXISTS);
        } finally {
            // 释放锁
            lock.unlock ();
        }
        
        
    }
}
