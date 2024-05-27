package com.zdf.sszxproduct.task;

import cn.hutool.json.JSONUtil;
import com.zdf.internalcommon.constant.CategoryConstant;
import com.zdf.sszxproduct.service.impl.CategoryServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 *@Description CacheTask
 *@Author mrzhang
 *@Date 2024/5/27 13:04
 */
@Component
@Slf4j
public class CacheTask {

    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private CategoryServiceImpl categoryService;

    @Scheduled(cron = "0 30 18 LW * ?")
    public void updatePrimaryCategory(){
        redisTemplate.opsForValue()
                .set(CategoryConstant.PRIMARY_CATEGORY, JSONUtil.parseArray(categoryService.getPrimaryCategory()).toString());
    }

    @Scheduled(cron = "0 30 18 LW * ?")
    public void updateAllCategory(){
        redisTemplate.opsForValue()
                .set(CategoryConstant.ALL_CATEGORY, JSONUtil.parseArray(categoryService.getAllCategory()).toString());
    }
}