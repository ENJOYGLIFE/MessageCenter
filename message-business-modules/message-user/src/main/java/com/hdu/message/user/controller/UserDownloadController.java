package com.hdu.message.user.controller;

import com.hdu.message.common.base.constant.FilePathConstant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

@RestController
@RequestMapping("/download")
public class UserDownloadController {

    /**
     * 下载大文件
     * @return
     */
    @GetMapping("downloadBig")
    public void downloadBig(HttpServletResponse response, String md5, String fileName) throws IOException {
        md5 = "0ecd45584bc622d3297ce410c26d1583";
        fileName = "简历照片.jpg";
        String dstFile = String.format("%s\\%s\\%s.%s", FilePathConstant.UPLOAD_PATH, md5, md5, fileName);
        File file = new File(dstFile);
        // 设置下载request
        response.setHeader("Content-Disposition", "attachment; filename=简历照片.jpg");
        response.setContentType("application/octet-stream");
        response.setContentLength((int) file.length());
        // 使用RandomAccessFile断点传输
        // 完成不了分片式的写回
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            OutputStream outputStream = response.getOutputStream()) {
            // 写回响应流
            byte[] buffer = new byte[8192];
            int byteRead;
            while ((byteRead = randomAccessFile.read(buffer)) != -1) {
                outputStream.write(buffer, 0 , byteRead);
            }
        }
    }
}
