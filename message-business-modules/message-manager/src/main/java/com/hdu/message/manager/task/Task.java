package com.hdu.message.manager.task;

import com.hdu.message.common.base.enums.redismessage.RedisMessageKeyEnum;
import com.hdu.message.redis.utils.RedisUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;

@Configuration
@EnableScheduling
public class Task {

    // 每天0点检查redis中的板块时间戳是否存在过期
    @Scheduled(cron = "0 0 0 * * ?")
    public void checkRedisNotice() {
        // 获取当前时间戳
        long currentTimestamp = Instant.now().toEpochMilli();
        // 获取30天以前时间戳
        long lastTimestamp = currentTimestamp - (30L * 24 * 60 * 60 * 1000);
        // 遍历栏目枚举，清空redis中过期消息
        for (RedisMessageKeyEnum redisMessageKeyEnum : RedisMessageKeyEnum.values()) {
            // 清空redis中的部分过期消息
            RedisUtil.ZSetOps.zRemoveRangeByScore(redisMessageKeyEnum.getMessageKeyType(), lastTimestamp, currentTimestamp);
        }
    }
}
