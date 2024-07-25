package com.hdu.message.common.base.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * (TblUserNotice)实体类
 *
 * @author makejava
 * @since 2024-06-14 15:13:59
 */
@Data
public class UserNotice implements Serializable {
    private static final long serialVersionUID = -23415718737065054L;

    private Long id;

    private Long userNoticeId;
/**
     * 标题
     */
    private String title;
/**
     * 内容
     */
    private String content;
/**
     * 是否已读
     */
    private Integer state;
/**
     * 消息通知类型
     */
    private Integer type;
/**
     * 通知来源ID
     */
    private Long sourceId;

    /**
     * 版块ID
     */
    private Integer sectionId;
    /**
     * 发送人ID
     */
    private Long sendId;
/**
     * 特殊接受通知的用户ID
     */
    private Long recipientId;
/**
     * 推拉时间
     */
    private LocalDateTime pullTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}

