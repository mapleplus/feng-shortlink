package com.feng.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feng.shortlink.admin.common.convention.exception.ClientException;
import com.feng.shortlink.admin.common.enums.UserErrorCodeEnum;
import com.feng.shortlink.admin.dao.entity.UserDO;
import com.feng.shortlink.admin.dao.mapper.UserMapper;
import com.feng.shortlink.admin.dto.request.UserLoginReqDTO;
import com.feng.shortlink.admin.dto.request.UserRegisterReqDTO;
import com.feng.shortlink.admin.dto.request.UserUpdateReqDTO;
import com.feng.shortlink.admin.dto.response.UserLoginRespDTO;
import com.feng.shortlink.admin.dto.response.UserRespDTO;
import com.feng.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static com.feng.shortlink.admin.common.constant.RedisCacheConstant.LOCK_SHORTLINK_USER_REGISTER_KEY;
import static com.feng.shortlink.admin.common.constant.RedisCacheConstant.SHORTLINK_USER_LOGIN_KEY;
import static com.feng.shortlink.admin.common.enums.UserErrorCodeEnum.USER_LOGOUT_ERROR;

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
    private final StringRedisTemplate stringRedisTemplate;
    
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
     * @param requestParams 包含要注册用户的详细信息的数据传输对象。
     */
    @Override
    public void registerUser (UserRegisterReqDTO requestParams) {
        if (!hasUserName (requestParams.getUsername ())){
            throw new ClientException (UserErrorCodeEnum.USER_NAME_EXISTS);
        }
        // 给register username上分布式锁 防止恶意注册
        RLock lock = redissonClient.getLock (LOCK_SHORTLINK_USER_REGISTER_KEY + requestParams.getUsername ());
        try {
            boolean tryLock = lock.tryLock ();
            if (tryLock) {
                // 新增用户
                int insert = baseMapper.insert (BeanUtil.toBean (requestParams , UserDO.class));
                if (insert < 1) {
                    throw new ClientException (UserErrorCodeEnum.USER_SAVE_ERROR);
                }
                // 添加用户名到布隆过滤器
                userRegisterCachePenetrationBloomFilter.add (requestParams.getUsername ());
                return;
            }
            throw new ClientException (UserErrorCodeEnum.USER_NAME_EXISTS);
        } finally {
            // 释放锁
            lock.unlock ();
        }
        
        
    }
    
    /**
     * 更新用户信息。
     *
     * @param requestParams 包含要更新用户的详细信息的数据传输对象。
     */
    @Override
    public void updateUser (UserUpdateReqDTO requestParams) {
        LambdaQueryWrapper<UserDO> updateWrapper = new LambdaQueryWrapper<UserDO>()
                .eq(UserDO::getUsername, requestParams.getUsername ());
        baseMapper.update (BeanUtil.toBean (requestParams , UserDO.class) , updateWrapper);
    }
    
    /**
     * 处理用户的登录过程。
     *
     * @param requestParams 包含用户凭据的登录请求参数。
     * @return 包含用户登录详细信息的响应DTO，例如身份验证令牌。
     */
    @Override
    public UserLoginRespDTO login (UserLoginReqDTO requestParams) {
        LambdaQueryWrapper<UserDO> queryWrapper = new LambdaQueryWrapper<UserDO> ()
                .eq (UserDO::getUsername , requestParams.getUsername ())
                .eq (UserDO::getPassword , requestParams.getPassword ())
                .eq (UserDO::getDelFlag , 0);
        UserDO userDO = baseMapper.selectOne (queryWrapper);
        if (userDO == null) {
            throw new ClientException (UserErrorCodeEnum.USER_NULL);
        }
        // 防止用户恶意刷登录token 使redis崩溃
        Boolean hasKey = stringRedisTemplate.hasKey (SHORTLINK_USER_LOGIN_KEY + userDO.getUsername ());
        if (Boolean.TRUE.equals (hasKey)) {
            throw new ClientException (UserErrorCodeEnum.USER_LOGIN_ERROR);
        }
        // 存在 登录成功 存入redis
        String token = UUID.randomUUID ().toString ();
        String jsonString = JSON.toJSONString (userDO);
        stringRedisTemplate.opsForHash ().put (SHORTLINK_USER_LOGIN_KEY + userDO.getUsername () , token , jsonString);
        // 设置有效时间
        stringRedisTemplate.expire (SHORTLINK_USER_LOGIN_KEY + userDO.getUsername () , 30L , TimeUnit.MINUTES);
        return new UserLoginRespDTO (token);
    }
    
    /**
     * 检查用户是否使用特定的令牌登录。
     *
     * @param username 要检查的用户名。
     * @param token    用于验证用户登录状态的令牌。
     * @return 如果用户使用给定令牌登录，则返回 {@code true}，否则返回 {@code false}。
     */
    @Override
    public Boolean checkLogin (String username , String token) {
        return stringRedisTemplate.opsForHash ().hasKey (SHORTLINK_USER_LOGIN_KEY + username , token);
    }
    
    /**
     * 通过从Redis中删除用户的会话令牌来注销用户，如果用户当前已登录，
     * 并在注销操作失败时抛出异常。
     *
     * @param username 要注销的用户用户名
     * @param token    用于注销的用户会话令牌
     * @throws ClientException 如果用户未登录或注销过程中出现错误
     */
    @Override
    public void logout (String username , String token) {
        if (Boolean.TRUE.equals (checkLogin (username , token))) {
            stringRedisTemplate.opsForHash ().delete (SHORTLINK_USER_LOGIN_KEY + username , token);
            return;
        }
        throw new ClientException (USER_LOGOUT_ERROR);
    }
}
