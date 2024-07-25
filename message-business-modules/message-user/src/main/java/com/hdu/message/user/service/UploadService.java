package com.hdu.message.user.service;

import com.hdu.message.user.dto.UserUploadRecordDto;

public interface UploadService {

    // 新增文件和对应文件信息
    Integer addUploadRecord(UserUploadRecordDto userUploadRecordDto);

    // 更新分片对应上传状态
    Integer updateUploadRecordDetail(UserUploadRecordDto userUploadRecordDto);
}
