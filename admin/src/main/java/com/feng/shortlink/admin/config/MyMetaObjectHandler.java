package com.feng.shortlink.admin.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author FENGXIN
 * @date 2024/9/27
 * @project feng-shortlink
 * @description mp自动填充
 **/
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    
    @Override
    public void insertFill(MetaObject metaObject) {
        strictInsertFill(metaObject, "createTime", LocalDateTime.class , LocalDateTime.now ());
        strictInsertFill(metaObject, "updateTime", LocalDateTime.class , LocalDateTime.now ());
        strictInsertFill(metaObject, "delFlag", () -> 0, Integer.class);
    }
    
    @Override
    public void updateFill(MetaObject metaObject) {
        strictInsertFill(metaObject, "updateTime", LocalDateTime.class , LocalDateTime.now ());
    }
}
