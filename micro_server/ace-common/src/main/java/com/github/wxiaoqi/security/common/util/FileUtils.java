package com.github.wxiaoqi.security.common.util;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件工具类
 */
public class FileUtils {


    /**
     * 匹配字符串中url，将图片url替换为本地url，网址设为"",返回新字符串
     */
    public static String replaceByPattern (String pattern,String[] imgSuffix,String oldStr,int oldLen, String path,String urlPrefix){
        StringBuilder sb = new StringBuilder();
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(oldStr);
        int s = 0;
        int changeNum=0;
        while (m.find()) {
            String item = m.group();
            if(StringHelper.isNullOrEmpty(item)){
                continue;
            }
            int index=item.lastIndexOf(".");
            boolean isImgUrl=false;
            if(index!=-1){
                String suffix=item.substring(index);
                for (int i=0,len=imgSuffix.length;i<len;i++){
                    String  is=imgSuffix[i];
                    if(suffix.equalsIgnoreCase(is)){
                        isImgUrl=true;
                        break;
                    }
                }
            }

            sb.append(oldStr, s, m.start());
            //除图片url外都设为""
            if(isImgUrl){
                String newFile= readUrlSaveLocal(item,path);
                if(newFile!=null){
                    sb.append(urlPrefix+newFile);
                    changeNum++;
                }else {
                    //获取失败则不修改
                    sb.append(item);;
                }
            }else {
               changeNum++;
            }
            s = m.end();
        }
        if(changeNum==0){
            return null;
        }
        sb.append(oldStr, s, oldLen);
        return sb.toString();
    }

        /**
         * 读取网络文件，保存到本地某一目录路径,返回新文件名
         */
        public static String readUrlSaveLocal (String fileUrl, String path){
            if (StringHelper.isNullOrEmpty(fileUrl) || StringHelper.isNullOrEmpty(path)) {
                return null;
            }

            //获得新文件名
            Integer index = fileUrl.lastIndexOf("/");
            if (index == -1) {
                return null;
            }
            String fileName = fileUrl.substring(index + 1);
            fileName = StringHelper.getUniqueCode() + UUIDUtils.generateShortUuid() + "__" + fileName;

            InputStream inStream = null;
            FileOutputStream outStream1 = null;
            File dest =null;
            try {
                //new一个URL对象
                URL url = new URL(fileUrl);
                //打开链接
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                //设置请求方式为"GET"
                conn.setRequestMethod("GET");
                //超时响应时间为5秒
                conn.setConnectTimeout(5 * 1000);
                //如果是服务器端禁止抓取,那么这个你可以通过设置User-Agent来欺骗服务器.
                conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36)");
                //通过输入流获取图片数据
                inStream = conn.getInputStream();

                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                //创建一个Buffer字符串
                byte[] buffer = new byte[1024];
                //每次读取的字符串长度，如果为-1，代表全部读取完毕
                int len = 0;
                //使用一个输入流从buffer里把数据读取出来
                while ((len = inStream.read(buffer)) != -1) {
                    //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
                    outStream.write(buffer, 0, len);
                }

                //把outStream里的数据写入内存（指向内存的流无需关闭）
                //得到图片的二进制数据，以二进制封装得到数据，具有通用性
                byte[] data = outStream.toByteArray();
                //new一个文件对象用来保存图片，默认保存当前工程根目录
                File folder = new File(path);
                //判断文件父目录是否存在
                if (!folder.exists() || !folder.isDirectory()) {
                    //删除不是文件夹的同名文件后创建文件夹
                    folder.delete();
                    folder.mkdirs();
                }
                dest = new File(path, fileName);
                //创建输出流
                outStream1 = new FileOutputStream(dest);
                //写入数据
                outStream1.write(data);
                return fileName;
            } catch (Exception e) {
                e.printStackTrace();
                //异常时删除可能存在的文件
                deleteFile(dest);
                return null;
            } finally {
                try {
                    if (inStream != null) {
                        inStream.close();
                    }
                } catch (IOException e) {
                }
                try {
                    if (outStream1 != null) {
                        outStream1.close();
                    }
                } catch (IOException e) {
                }
            }
        }


        /**
         * 旋转照片
         * @return
         */
        public static String rotatePhonePhoto (String fullPath,int angel){
            BufferedImage src;
            try {
                src = ImageIO.read(new File(fullPath));
                int src_width = src.getWidth(null);
                int src_height = src.getHeight(null);

                int swidth = src_width;
                int sheight = src_height;

                if (angel == 90 || angel == 270) {
                    swidth = src_height;
                    sheight = src_width;
                }
                Rectangle rect_des = new Rectangle(new Dimension(swidth, sheight));
                BufferedImage res = new BufferedImage(rect_des.width, rect_des.height, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2 = res.createGraphics();
                g2.translate((rect_des.width - src_width) / 2, (rect_des.height - src_height) / 2);
                g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);
                g2.drawImage(src, null, null);
                ImageIO.write(res, "jpg", new File(fullPath));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return fullPath;

        }


        /**
         * 上传文件到某一文件夹返回在该文件夹中的新文件名
         */
        public static String uploadFile (MultipartFile file, String path, String addFileName) throws IOException {
            //判断文件是否为空
            if (file == null || file.isEmpty()) {
                return null;
            }
            // 获取文件名
            String fileName = file.getOriginalFilename();

            //有些ie浏览器获得的是文件的全路径，需要将其截掉
            if (fileName != null) {
                Integer index = fileName.lastIndexOf("/");
                if (index != -1) {
                    fileName = fileName.substring(index + 1);
                }
                Integer j = fileName.lastIndexOf("\\");
                if (j != -1) {
                    fileName = fileName.substring(j + 1);
                }
            }

            if (addFileName != null) {
                fileName = addFileName + "__" + fileName;
            }

            fileName = StringHelper.getUniqueCode() + UUIDUtils.generateShortUuid() + "__" + fileName;


            File folder = new File(path);
            //判断文件父目录是否存在
            if (!folder.exists() || !folder.isDirectory()) {
                //删除不是文件夹的同名文件后创建文件夹
                folder.delete();
                folder.mkdirs();
            }
            File dest = new File(path, fileName);
            //上传文件
            file.transferTo(dest);
            return fileName;

        }

        /**
         * 删除url对应文件
         * @param url
         * @return
         */
        public static void deleteFile (String url){
            if (url != null) {
                File file = new File(url);
                deleteFile(file);
            }
        }

    /**
     * 删除对应文件
     */
    public static void deleteFile (File file){
        if (file != null) {
            if (file.exists()) {
                file.delete();
            }
        }
    }


        /**
         * 通过url获得对应文件
         * @param url
         * @return
         */
        public static File getFileByUrl (String url){
            if (url != null) {
                File file = new File(url);
                if (file.exists()) {
                    return file;
                }
            }
            return null;
        }


        /**
         * 获取文件大小
         * @param size
         * @return
         */
        public static String getPrintSize ( long size){
            // 如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
            if (size < 1024) {
                return String.valueOf(size) + "B";
            } else {
                size = size / 1024;
            }
            // 如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
            // 因为还没有到达要使用另一个单位的时候
            // 接下去以此类推
            if (size < 1024) {
                return String.valueOf(size) + "KB";
            } else {
                size = size / 1024;
            }
            if (size < 1024) {
                // 因为如果以MB为单位的话，要保留最后1位小数，
                // 因此，把此数乘以100之后再取余
                size = size * 100;
                return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "MB";
            } else {
                // 否则如果要以GB为单位的，先除于1024再作同样的处理
                size = size * 100 / 1024;
                return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "GB";
            }
        }


    }
