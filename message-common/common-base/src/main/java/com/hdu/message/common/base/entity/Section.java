package com.hdu.message.common.base.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * (TblSection)实体类
 * @author xwr
 * @since 2024-06-20 16:09:04
 */
@Data
public class Section implements Serializable {
    private static final long serialVersionUID = -38209787738030648L;

/**
     * 栏目ID
     */
    private Integer sectionId;
/**
     * 栏目名称
     */
    private String sectionName;
/**
     * 栏目枚举
     */
    private String sectionEnum;

    /**
     * 栏目描述
     */
    private String sectionContent;

    private Date createTime;

    private Date updateTime;

    private Integer isDelete;

}

