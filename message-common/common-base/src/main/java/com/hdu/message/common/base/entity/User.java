package com.hdu.message.common.base.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * (TblUser)实体类
 *
 * @author makejava
 * @since 2024-06-14 15:23:56
 */
@Data
public class User implements Serializable {

/**
     * 用户ID
     */
    private Long userId;
/**
     * 用户名称
     */
    private String userName;
/**
     * 是否启用
     */
    private Integer state;
/**
     * 上次登录时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLoginTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}

