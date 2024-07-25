package com.hdu.message.manager.service.impl;

import com.hdu.message.common.base.constant.CommonConstant;
import com.hdu.message.common.base.entity.Section;
import com.hdu.message.manager.mapper.SectionMapper;
import com.hdu.message.manager.service.SectionService;
import com.hdu.message.redis.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Transactional(rollbackFor = Exception.class)
@Service
public class SectionServiceImpl implements SectionService {

    @Autowired
    private SectionMapper sectionMapper;

    @Override
    public Integer addNewSection(Section section) {
        // 先写数据库，然后删除key
        int insertNum = sectionMapper.insert(section);
        // 删除key保证数据一致性
        RedisUtil.KeyOps.delete(CommonConstant.SECTION_KEY);
        return insertNum;
    }

    @Override
    public List<Section> getSectionList(Section section) {
        return sectionMapper.queryAllByLimit(new Section());
    }
}
