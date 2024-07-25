package com.hdu.message.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Component
@Data
@ConfigurationProperties("white.list")
public class IgnoreUrlsConfig {

    private List<String> urls = new ArrayList<>();
}
