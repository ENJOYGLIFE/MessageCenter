package com.hdu.message.common.base.dto;

import com.hdu.message.common.base.entity.Section;
import lombok.Data;

import java.util.List;

@Data
public class UserSectionDto {

    private Long userId;

    private List<Section> sectionList;
}
