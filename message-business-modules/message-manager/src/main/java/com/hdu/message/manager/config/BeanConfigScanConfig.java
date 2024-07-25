package com.hdu.message.manager.config;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@ComponentScans(value =
        {@ComponentScan(value = "com.hdu.message.redis")})
@Configuration
public class BeanConfigScanConfig implements EnvironmentAware {

    @Override
    public void setEnvironment(Environment environment) {
        System.out.println("##################################初始化 BeanConfigScan - ManagerMessage ################################################");
    }
}
