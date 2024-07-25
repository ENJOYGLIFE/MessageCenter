package com.hdu.message.manager.controller;

import com.hdu.message.common.base.entity.Section;
import com.hdu.message.common.base.utils.ResponseUtil;
import com.hdu.message.manager.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("cache")
public class CacheController {

    @Autowired
    private CacheService cacheService;

    /**
     * 供feign调用查询section栏目
     * @return
     */
    @PostMapping("cacheSection")
    public List<Section> cacheSectionFeign() {
        return cacheService.initSectionCache();
    }
}
