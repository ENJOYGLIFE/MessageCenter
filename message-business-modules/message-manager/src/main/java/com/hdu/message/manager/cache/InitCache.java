package com.hdu.message.manager.cache;

import com.hdu.message.manager.service.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class InitCache implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(InitCache.class);

    @Autowired
    private CacheService cacheService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 将数据加载到Redis中
        cacheService.initSectionCache();
        log.info("======================================Redis初始化栏目板块======================================");
    }
}
