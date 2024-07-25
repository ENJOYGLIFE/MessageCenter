package com.hdu.message.manager.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hdu.message.common.base.constant.CommonConstant;
import com.hdu.message.common.base.entity.Section;
import com.hdu.message.manager.mapper.SectionMapper;
import com.hdu.message.manager.service.CacheService;
import com.hdu.message.redis.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CacheServiceImpl implements CacheService {

    @Autowired
    private SectionMapper sectionMapper;

    @Override
    public List<Section> initSectionCache() {
        if (RedisUtil.KeyOps.hasKey(CommonConstant.SECTION_KEY)) {
            return null;
        }
        // 查询所有section板块信息
        List<Section> sections = sectionMapper.queryAllByLimit(new Section());
        List<String> strings = new ArrayList<>();
        for (Section section : sections) {
            String sectionString = JSONObject.toJSONString(section);
            strings.add(sectionString);
        }
        // 把section放入redis中，以String方式存储,全部初始化到section当中
        RedisUtil.ListOps.lLeftPushAll(CommonConstant.SECTION_KEY, strings);
        return sections;
    }
}
