package com.hdu.message.user.controller;

import com.hdu.message.common.base.constant.FilePathConstant;
import com.hdu.message.common.base.utils.ResponseUtil;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/userUpload")
public class UserUploadController {

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public Object upload(@RequestParam MultipartFile file) throws IOException {
        File dstFile = new File(FilePathConstant.UPLOAD_PATH, String.format("%s.%s", UUID.randomUUID(), StringUtils.getFilename(file.getOriginalFilename())));
        file.transferTo(dstFile);
        return ResponseUtil.ok("上传文件成功");
    }

    /**
     * 分片上传
     * @param chunkSize     每个分片大小
     * @param totalNumber   总共分片数量
     * @param chunkNumber   当前分片
     * @param md5           文件总MD5
     * @param file          当前分片文件数据
     * @return 响应体
     */
    @PostMapping(value = "/uploadBig", consumes = "multipart/form-data")
    public Object uploadBig(@RequestParam Long chunkSize, @RequestParam Integer totalNumber, @RequestParam Long chunkNumber,
                            @RequestParam String md5, @RequestParam MultipartFile file) throws IOException {
        // 文件分片存储地址
        String dstFile = String.format("%s\\%s\\%s.%s", FilePathConstant.UPLOAD_PATH, md5, md5, StringUtils.getFilename(file.getOriginalFilename()));
        // 文件分片详细信息使用数据库方式持久化
        // 使用文件IO方法
        String confFile = String.format("%s\\%s\\%s.conf", FilePathConstant.UPLOAD_PATH, md5, md5);
        // 使用数据库进行持久化，便于统一管理文件
        Path confPath = Paths.get(confFile);
        // 查看是否创建过分片文件
        File dir = new File(dstFile).getParentFile();
        if (!dir.exists()) {
            boolean mkdir = dir.mkdir();
            byte[] bytes = new byte[totalNumber];
            // 写入一个空chunk文件
            Files.write(confPath, bytes);
        }
        // 执行写入任务
        // 1. 写入文件本体的文件流
        // 2. 写入文件信息的文件流
        // 3. 读取文件流
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(dstFile, "rw");
            RandomAccessFile randomAccessConfFile = new RandomAccessFile(confFile, "rw");
            InputStream inputStream = file.getInputStream()) {
            // 先判断该分片是否已经完成上传，如果已经完成上传则返回给前端信息
            byte[] bytes = Files.readAllBytes(confPath);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(String.valueOf(bytes[(int)chunkNumber.longValue()]));
            if (stringBuilder.toString().equals("1")) {
                // 返回给前端完成该部分分片的上传
                return ResponseUtil.fail(1000, "分片已经完成上传");
            }
            // 未完成分片的上传，进入分片上传逻辑
            // 定位到该分片开始写入位置
            randomAccessFile.seek(chunkNumber * chunkSize);
            // 设置一个缓冲区
            byte[] buffer = new byte[8192];
            // 该变量每次循环中读取的字节数
            int bytesRead = 0;
            // 从InputSteam读取数据放到buffer中，读取的字节数为bytesRead
            while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
                // 写入文件对应分片部分
                randomAccessFile.write(buffer, 0, bytesRead);
            }
            // 完成写入后更新该分片信息对应状态
            randomAccessConfFile.seek(chunkNumber);
            randomAccessConfFile.write(1);
        }
        return ResponseUtil.ok();
    }

    /**
     * 检查文件完整性
     * @param md5 文件前端生成的MD5值
     * @return    响应体（成功则为文件路径？（是否安全）， 失败则为未完成上传的chunk值， 如果MD5不相同则表示上传途中出现问题）
     */
    @PostMapping("/checkUpload")
    public Object checkUpload(@RequestParam String md5) throws IOException {
        // 获取分片信息是否完成上传的chunk信息地址
        String uploadConfPath = String.format("%s\\%s\\%s.conf", FilePathConstant.UPLOAD_PATH, md5, md5);
        Path confPath = Paths.get(uploadConfPath);
        // 如果confPath不存在说明一个分片都未上传，还未完成初始化
        if (!Files.exists(confPath)) {
            return ResponseUtil.fail("文件未上传");
        }
        // 如果有值则判断所有分片是否都已经完成上传了
        StringBuilder stringBuilder = new StringBuilder();
        // 读取分片数据信息，如果还有0代表分片没有全部完成上传，可让前端端点续传或者等待
        byte[] bytes = Files.readAllBytes(confPath);
        for (byte b :bytes) {
            stringBuilder.append(String.valueOf(b));
        }
        // 如果没有0，则全部上传成功
        if (!stringBuilder.toString().contains("0")) {
            // 计算对应的MD5，判断其中上传过程中有没有被篡改
            File file = new File(String.format("%s\\%s", FilePathConstant.UPLOAD_PATH, md5));
            File[] files = file.listFiles();
            for (File f : files) {
                // 排除conf文件
                if (!f.getName().contains("conf")) {
                    try(InputStream inputStream = Files.newInputStream(f.toPath())) {
                        String md5pwd = DigestUtils.md5DigestAsHex(inputStream);
                        // 判断当前文件的md
                        if (!md5pwd.equals(md5)) {
                            return ResponseUtil.fail("文件上传失败");
                        }
                    }
                }
            }
            return ResponseUtil.ok("文件上传成功");
        } else {
            // 说明有分片传输失败，或者有分片还未传输完成
            // 返回对应的chunk字符串给前端
            return ResponseUtil.fail(-1, "分片有缺失", stringBuilder.toString());
        }
    }
}
