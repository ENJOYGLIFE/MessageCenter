package com.hdu.message.manager.service;

import com.hdu.message.common.base.entity.Section;

import java.util.List;

public interface SectionService {

    Integer addNewSection(Section section);

    List<Section> getSectionList(Section section);

}
