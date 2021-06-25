package com.github.wxiaoqi.security.info.sys.biz;

import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.info.sys.config.FileUploadConfig;
import com.github.wxiaoqi.security.info.sys.entity.FileInfo;
import com.github.wxiaoqi.security.info.sys.mapper.FileInfoMapper;
import com.github.wxiaoqi.security.info.sys.utils.UploadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Transactional(rollbackFor = Exception.class)
public class FileInfoBiz extends BaseBiz<FileInfoMapper, FileInfo> {

    @Autowired
    private FileUploadConfig fileUploadConfig;

    //子目录
    @Value("${UploadFile.SON_PATH}")
    private String SON_PATH;


    public boolean addOneFile(MultipartFile[] files, String tableName, int tableId) {
        try {
            String path = fileUploadConfig.getImgSavePath();
            // 获取当前年月
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
            Date curDate = new Date();
            String YMPath = df.format(curDate) + "/";
            path = path + YMPath;
            //判断是否有路径
            if (!new File(path).exists()) {
                new File(path).mkdirs();
            }
            for (MultipartFile file : files) {
                // 将图片文件保存到服务器，同时返回上传后图片的名字
                String fileName = UploadFile.uploadFile(file, path);
                FileInfo fileInfo = new FileInfo();
                // 关联表id
                fileInfo.setTableId(tableId);
                // 关联表名
                fileInfo.setLinkTable(tableName);
                fileInfo.setPath(SON_PATH + YMPath + fileName);
                int insert = insertSelective(fileInfo);
                System.out.println("上传成功");
                if (insert == 0) {
                    return false;
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


}
