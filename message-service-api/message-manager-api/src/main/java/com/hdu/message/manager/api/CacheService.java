package com.hdu.message.manager.api;

import com.hdu.message.common.base.config.FeignConfig;
import com.hdu.message.common.base.constant.ServiceNameConstant;
import com.hdu.message.common.base.entity.Section;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(value = ServiceNameConstant.MESSAGE_MANAGER, path = "cache", configuration = FeignConfig.class, contextId = "2")
public interface CacheService {

    @PostMapping("cacheSection")
    List<Section> cacheSectionFeign();
}
