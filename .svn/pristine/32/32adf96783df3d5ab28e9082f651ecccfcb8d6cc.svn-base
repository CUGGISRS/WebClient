package com.github.wxiaoqi.security.dzsw.sys.service;

import com.github.wxiaoqi.security.dzsw.sys.config.FileUploadConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;

/**
 * @author TsaiJun
 */
@Component
@Service
public class FileUploadService {

    @Autowired
    private FileUploadConfig fileUploadConfig;
    @Autowired
    private ThumbnailService thumbnailService;

    /**
     * 获取已上传的文件大小
     *
     * @param request
     * @param
     */
    public Long getChunkedFileSize(HttpServletRequest request) {
        //存储文件的路径，根据自己实际确定
        String currentFilePath = fileUploadConfig.getVideoSavePath();
        File testFile = new File(currentFilePath);
        if (!testFile.exists()) {
            testFile.mkdirs();
            System.out.println("测试文件夹不存在,已创建目标文件夹");
        }
        try {
            request.setCharacterEncoding("utf-8");
            String fileName = new String(request.getParameter("fileName").getBytes("ISO-8859-1"), "UTF-8");
            File file = new File(currentFilePath + fileName);
            if (file.exists()) {
                return file.length();
            } else {
                return -1L;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 断点文件上传 1.先判断断点文件是否存在 2.存在直接流上传 3.不存在直接新创建一个文件 4.上传完成以后设置文件名称
     */
    public boolean appendUpload2Server(HttpServletRequest request, HttpServletResponse response) {
        boolean uploadsuccess = false;
        PrintWriter printWriter = null;
        try {
            request.setCharacterEncoding("utf-8");
            printWriter = response.getWriter();
            String fileSize = request.getParameter("fileSize");
            String url = request.getParameter("url");
            Long totalSize = 0L;
            if (fileSize != null && !fileSize.equals("")) {
                totalSize = Long.valueOf(fileSize);
            }
            RandomAccessFile randomAccessfile = null;
            // 记录当前文件大小，用于判断文件是否上传完成
            long currentFileLength = 0;
            // 记录当前文件的绝对路径
            String fileSavePath = fileUploadConfig.getVideoSavePath();
            File testFile = new File(fileSavePath);
            if (!testFile.exists()) {
                testFile.mkdirs();
                System.out.println("测试文件夹不存在,已创建目标文件夹");
            }
            File file = new File(fileSavePath + "/" + url);
            // 存在文件
            if (file.exists()) {
                randomAccessfile = new RandomAccessFile(file, "rw");
            } else {
                // 不存在文件，根据文件标识创建文件
                randomAccessfile = new RandomAccessFile(fileSavePath + "/" + url, "rw");
            }
            // 开始文件传输
            InputStream in = request.getInputStream();
            randomAccessfile.seek(randomAccessfile.length());
            byte b[] = new byte[1024];
            int n;
            while ((n = in.read(b)) != -1) {
                randomAccessfile.write(b, 0, n);
            }
            currentFileLength = randomAccessfile.length();
            // 关闭文件
            closeRandomAccessFile(randomAccessfile);
            randomAccessfile = null;
            //整个文件上传完毕，修改文件后缀
            if (currentFileLength == totalSize) {
                uploadsuccess = true;
                //生成缩略图
                thumbnailService.generateThumbnail(url);
            }
            printWriter.print(currentFileLength);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uploadsuccess;
    }


    /**
     * 关闭随机访问文件
     *
     * @param
     */
    public static void closeRandomAccessFile(RandomAccessFile rFile) {
        if (null != rFile) {
            try {
                rFile.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
