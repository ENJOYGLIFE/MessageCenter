package com.hdu.message.user.bean;

import java.time.LocalDateTime;
import java.util.Date;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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

