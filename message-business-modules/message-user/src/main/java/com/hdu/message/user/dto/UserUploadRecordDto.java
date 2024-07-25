package com.hdu.message.user.dto;

import com.hdu.message.user.bean.UploadRecord;
import com.hdu.message.user.bean.UploadRecordDetail;
import lombok.Data;

import java.util.List;

/**
 * 用户上传信息存储
 */
@Data
public class UserUploadRecordDto {

    /**
     * 上传文件信息概述
     */
    private UploadRecord uploadRecord;

    /**
     * 上传文件信息详细分片信息
     */
    private List<UploadRecordDetail> uploadRecordDetail;
}
