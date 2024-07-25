package com.hdu.message.user.bean;

import java.util.Date;
import java.io.Serializable;
import lombok.Data;

/**
 * (TblUserSection)实体类
 *
 * @author makejava
 * @since 2024-06-21 15:59:01
 */
@Data
public class UserSection implements Serializable {
    private static final long serialVersionUID = -42105243215484841L;

    private Long id;
/**
     * 用户ID
     */
    private Long userId;
/**
     * 栏目ID
     */
    private Integer sectionId;

    /**
     * 栏目key
     */
    private String sectionEnum;

    private Date createTime;

    private Date updateTime;

}

