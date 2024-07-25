package com.hdu.message.manager.bean;

import java.util.Date;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * (TblManagerNotice)实体类
 *
 * @author xwr
 * @since 2024-06-13 20:13:09
 */
@Data
public class ManagerNotice implements Serializable {

    private Long id;
/**
     * 系统通知ID
     */
    private Long systemNoticeId;
/**
     * 标题
     */
    private String title;
/**
     * 内容
     */
    private String content;
/**
     * 类型
     */
    private Integer type;
/**
     * 是否完成推送
     */
    private Integer state;

    /**
     * 版块ID
     */
    private Integer sectionId;
/**
     * 特殊接受通知的用户ID
     */
    private Long recipientId;
/**
     * 发布通知的管理员ID
     */
    private Long managerId;
/**
     * 发布时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date publishTime;

    private Date createTime;

    private Date updateTime;

}

