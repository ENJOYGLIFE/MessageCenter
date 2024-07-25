package com.hdu.message.manager.api;

import com.hdu.message.common.base.config.FeignConfig;
import com.hdu.message.common.base.constant.ServiceNameConstant;
import com.hdu.message.common.base.entity.Section;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = ServiceNameConstant.MESSAGE_MANAGER, path = "section", configuration = FeignConfig.class, contextId = "1")
public interface SectionService {

    @PostMapping("getSectionList")
    String  getSectionList(@RequestBody Section section);
}
