package com.github.wxiaoqi.security.common.util;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件工具类
 */
public class FileUtil {
    /**
     * 文件暂存区路径
     */
    public static String tmpPath="D:/sc/tmp/";

    public static List<File> fileList=new ArrayList<>();

    /**
     * 清空暂存区
     * @param list
     */
    public static   void  clearTMP(List<File> list) {
        int size=list.size();
        if(size==0){
            return;
        }
        for (int i=0;i<size;i++){
            File file=list.get(i);
            file.delete();
        }
        list.clear();
    }
    /**
     *将暂存区文件存入某处后清空缓存区
     * @param list
     * @param path
     */
    public static void returnFile(List<File> list, String path) {
        int size=list.size();
        if(size==0){
            return;
        }
        for (int i=0;i<size;i++){
            File file=list.get(i);
            File f1=new File(path+file.getName());
            //填充删除了的文件
            if(!f1.exists()){
                file.renameTo(f1);
            }else {
                file.delete();
            }

        }
        list.clear();
    }

    /**
     * 上传文件返回url
     * @param file
     * @return
     * @throws IOException
     */
    public static String uploadFile(MultipartFile file, HttpServletRequest request,String path) throws IOException {
        //判断文件是否为空
        if (file==null||file.isEmpty()) {
            return "";
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        fileName = UUIDUtils.generateShortUuid() + "_" + fileName;
        //文件绝对路径
   //     System.out.print("保存文件绝对路径"+path+fileName+"\n");

        File folder = new File(path);
        //判断文件父目录是否存在
        if (!folder.exists()||!folder.isDirectory()) {
            //删除不是文件夹的同名文件后创建文件夹
            folder.delete();
            folder.mkdirs();
        }
        File dest=new File(path,fileName);
        //上传文件
        file.transferTo(dest); //保存文件
        //url=协议+ip+端口+*     项目启动时访问文件的url
        String url=request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/picture/"+fileName;
     //   System.out.print("保存的完整url===="+url+"\n");
        return url;
    }

    /**
     * 删除url对应文件
     * @param url
     * @param path
     * @return
     */
    public static boolean deleteFile(String url,String path){
        if(url==null||!(url.indexOf("/picture/") >-1)){
            return true;
        }
        url=path+url.substring(url.indexOf("/picture/")+9);
      //  System.out.println(url);
        File file=new File(url);
        if(!file.exists()){
            return true;
        }
        return file.delete();
    }

    /**
     * 通过url获得对应文件
     * @param url
     * @param path
     * @return
     */
    public static  File getFileByUrl(String url,String path){
        if(url==null||!(url.indexOf("/picture/") >-1)){
            return null;
        }
        url=path+url.substring(url.indexOf("/picture/")+9);
        File file=new File(url);
        if(!file.exists()){
            return null;
        }
        return file;
    }

    /**
     * 将文件存入,返回文件路径
     * @param file
     * @throws IOException
     */
    public static String saveFile(File file,String path) throws IOException {
        if(file==null){
            throw new FileNotFoundException("文件不能为null");
        }
        String name=file.getName();
        File folder = new File(path);
        //判断文件父目录是否存在
        if (!folder.exists()||!folder.isDirectory()) {
            //删除不是文件夹的同名文件后创建文件夹
            folder.delete();
            folder.mkdirs();
        }

        //暂存区保存文件
        File f1=new File(path,name);
    //    System.out.println(name);
        InputStream input = new FileInputStream(file);
        OutputStream output = new FileOutputStream(f1);
        byte[] buf = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
        }
        input.close();
        output.close();
        return path+name;

    }
}
