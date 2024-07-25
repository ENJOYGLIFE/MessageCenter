package com.hdu.message.user.bean;

import java.io.Serializable;
import lombok.Data;

/**
 * (UploadRecordDetail)实体类
 *
 * @author xwr
 * @since 2024-07-09 16:31:26
 */
@Data
public class UploadRecordDetail implements Serializable {
    private static final long serialVersionUID = -72622014844639555L;

    private Long id;
/**
     * 文件MD5码
     */
    private String md5;

    private Integer chunkNumber;

}

