package com.feng.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feng.shortlink.admin.common.biz.user.UserContext;
import com.feng.shortlink.admin.common.convention.exception.ClientException;
import com.feng.shortlink.admin.common.enums.UserErrorCodeEnum;
import com.feng.shortlink.admin.dao.entity.UserDO;
import com.feng.shortlink.admin.dao.mapper.UserMapper;
import com.feng.shortlink.admin.dto.request.UserLoginReqDTO;
import com.feng.shortlink.admin.dto.request.UserRegisterReqDTO;
import com.feng.shortlink.admin.dto.request.UserUpdateReqDTO;
import com.feng.shortlink.admin.dto.response.UserLoginRespDTO;
import com.feng.shortlink.admin.dto.response.UserRespDTO;
import com.feng.shortlink.admin.service.GroupService;
import com.feng.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

import static com.feng.shortlink.admin.common.constant.RedisCacheConstant.LOCK_SHORTLINK_USER_REGISTER_KEY;
import static com.feng.shortlink.admin.common.constant.RedisCacheConstant.SHORTLINK_USER_LOGIN_KEY;
import static com.feng.shortlink.admin.common.enums.UserErrorCodeEnum.USER_LOGOUT_ERROR;

/**
 * @author FENGXIN
 * @date 2024/9/26
 * @project feng-shortlink
 * @description 用户管理实现
 **/
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {
    // 构造器注入布隆过滤器
    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;
    // 使用redis
    private final RedissonClient redissonClient;
    private final StringRedisTemplate stringRedisTemplate;
    private final GroupService groupService;
    
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
    
    @Override
    public Boolean hasUserName (String username) {
        return !userRegisterCachePenetrationBloomFilter.contains (username);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerUser (UserRegisterReqDTO requestParams) {
        if (!hasUserName (requestParams.getUsername ())){
            throw new ClientException (UserErrorCodeEnum.USER_NAME_EXISTS);
        }
        // 给register username上分布式锁 防止恶意注册
        RLock lock = redissonClient.getLock (LOCK_SHORTLINK_USER_REGISTER_KEY + requestParams.getUsername ());
        boolean tryLock = lock.tryLock ();
        if (!tryLock){
            throw new ClientException (UserErrorCodeEnum.USER_NAME_EXISTS);
        }
        try {
            // 新增用户
            int insert = baseMapper.insert (BeanUtil.toBean (requestParams , UserDO.class));
            if (insert < 1) {
                throw new ClientException (UserErrorCodeEnum.USER_SAVE_ERROR);
            }
            // 提供短链接默认分组给用户
            groupService.saveGroupByGid (requestParams.getUsername (),"默认分组");
            // 添加用户名到布隆过滤器 （分组失败则不会添加user到布隆过滤器，布隆过滤器不会回滚 逻辑）
            userRegisterCachePenetrationBloomFilter.add (requestParams.getUsername ());
        } catch (DuplicateKeyException e){
            throw new ClientException (UserErrorCodeEnum.USER_SAVE_ERROR);
        }finally {
            // 释放锁
            lock.unlock ();
        }
        
    }
    
    @Override
    public void updateUser (UserUpdateReqDTO requestParams) {
        String paramsUsername = requestParams.getUsername ();
        String userName = UserContext.getUserName ();
        if (!StrUtil.equals ( paramsUsername,userName )) {
            throw new ClientException ("req_username: "+ paramsUsername + "username: "+ userName + "异常");
        }
        LambdaQueryWrapper<UserDO> updateWrapper = new LambdaQueryWrapper<UserDO>()
                .eq(UserDO::getUsername, requestParams.getUsername ());
        baseMapper.update (BeanUtil.toBean (requestParams , UserDO.class) , updateWrapper);
    }
    
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
        stringRedisTemplate.expire (SHORTLINK_USER_LOGIN_KEY + userDO.getUsername () , 30L , TimeUnit.DAYS);
        return new UserLoginRespDTO (token);
    }
    
    @Override
    public Boolean checkLogin (String username , String token) {
        return stringRedisTemplate.opsForHash ().hasKey (SHORTLINK_USER_LOGIN_KEY + username , token);
    }
    
    @Override
    public void logout (String username , String token) {
        if (Boolean.TRUE.equals (checkLogin (username , token))) {
            stringRedisTemplate.opsForHash ().delete (SHORTLINK_USER_LOGIN_KEY + username , token);
            return;
        }
        throw new ClientException (USER_LOGOUT_ERROR);
    }
}
