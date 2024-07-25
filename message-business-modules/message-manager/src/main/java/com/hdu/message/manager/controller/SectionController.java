package com.hdu.message.manager.controller;

import com.hdu.message.common.base.entity.Section;
import com.hdu.message.common.base.enums.error.ErrorEnum;
import com.hdu.message.common.base.exception.SectionException;
import com.hdu.message.common.base.utils.ResponseUtil;
import com.hdu.message.manager.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("section")
public class SectionController {

    @Autowired
    private SectionService sectionService;

    /**
     * 新增板块
     * @param section 版块信息
     * @return 插入条数
     */
    @PostMapping("addNewSection")
    public Object addNewSection(@RequestBody Section section) {
        validateSection(section);
        return ResponseUtil.ok(sectionService.addNewSection(section));
    }

    @PostMapping("getSectionList")
    public Object getSectionList(@RequestBody Section section) {
        return ResponseUtil.okList(sectionService.getSectionList(section));
    }

    public Boolean validateSection(Section section) {
        if (section == null) {
            throw new SectionException(ErrorEnum.A0400);
        }
        if (section.getSectionEnum() == null || section.getSectionName() == null) {
            throw new SectionException(ErrorEnum.A0400);
        }
        return true;
    }

}
