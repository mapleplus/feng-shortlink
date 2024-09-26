package com.feng.shortlink.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feng.shortlink.admin.common.convention.exception.ClientException;
import com.feng.shortlink.admin.common.enums.UserErrorCodeEnum;
import com.feng.shortlink.admin.dao.entity.UserDO;
import com.feng.shortlink.admin.dao.mapper.UserMapper;
import com.feng.shortlink.admin.dto.response.UserRespDTO;
import com.feng.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @author FENGXIN
 * @date 2024/9/26
 * @project feng-shortlink
 * @description
 **/
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {
    // 构造器注入布隆过滤器
    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;
    
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
     *
     * @param username 要检查的用户名。
     * @return 如果用户名不存在于数据库中则返回 {@code true}，否则返回 {@code false}。
     */
    @Override
    public Boolean hasUserName (String username) {
        return userRegisterCachePenetrationBloomFilter.contains (username);
    }
}
