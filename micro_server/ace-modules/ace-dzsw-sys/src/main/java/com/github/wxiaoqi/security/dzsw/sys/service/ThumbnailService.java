package com.github.wxiaoqi.security.dzsw.sys.service;

import com.github.wxiaoqi.security.dzsw.sys.config.FileUploadConfig;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author TsaiJun
 */
@Component
@Service
public class ThumbnailService {

    @Autowired
    private FileUploadConfig fileUploadConfig;

    /**
     * 根据视频名称获取生成好的同名称缩略图名称
     *
     * @param videoName
     * @return
     */
    public String getThumbnail(String videoName) {
        //获取缩略图文件路径
        String thumbnailPath = fileUploadConfig.getThumbnailSavePath();
        //得到缩略图名称
        String thumbnailName = videoName.substring(0, videoName.lastIndexOf(".")) + ".jpg";
        //如果视频文件存在，直接返回同名称的缩略图名称
        File videoFile = new File(thumbnailPath + thumbnailName);
        if (videoFile.exists()) {
            return thumbnailName;
        } else {
            //没有缩略图文件
            return null;
        }
    }


    /**
     * 根据视频名称生成同名称的缩略图
     *
     * @param videoName
     */
    public String generateThumbnail(String videoName) {
        //获取视频文件和缩略图文件路径
        String videoSavePath = fileUploadConfig.getVideoSavePath();
        String thumbnailPath = fileUploadConfig.getThumbnailSavePath();
        //获取文件名后缀并转小写
        String extension = videoName.substring(videoName.lastIndexOf(".")).toLowerCase();
        //得到缩略图名称
        String thumbnailName = videoName.substring(0, videoName.lastIndexOf(".")) + ".jpg";
        //判断缩略图文件夹是否存在
        File thumbnailDir = new File(thumbnailPath);
        if (!thumbnailDir.exists()) {
            thumbnailDir.mkdirs();
        }
        //判断视频文件是否存在
        File videoFile = new File(videoSavePath + videoName);
        //如果是.mp4文件并且视频文件存在才进行截图
        if (extension.equals(".mp4") && videoFile.exists()) {
            try {
                fetchFrame(videoSavePath + videoName, thumbnailPath + thumbnailName);
//                generateVideoPic(videoSavePath + videoName, thumbnailPath, thumbnailName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return thumbnailName;
        } else {
            //如果不是.mp4文件就不截图
            return "没有生成缩略图";
        }
    }

    public String generateThumb(String videoName, String videoSavePath, String thumbnailPath) {
//        //获取视频文件和缩略图文件路径
//        String videoSavePath = fileUploadConfig.getVideoSavePath();
//        String thumbnailPath = fileUploadConfig.getThumbnailSavePath();
        //获取文件名后缀并转小写
        String extension = videoName.substring(videoName.lastIndexOf(".")).toLowerCase();
        //得到缩略图名称
        String thumbnailName = videoName.substring(0, videoName.lastIndexOf(".")) + ".jpg";
        //判断缩略图文件夹是否存在
        File thumbnailDir = new File(thumbnailPath);
        if (!thumbnailDir.exists()) {
            thumbnailDir.mkdirs();
        }
        //判断视频文件是否存在
        File videoFile = new File(videoSavePath + videoName);
        //如果是.mp4文件并且视频文件存在才进行截图
        if (extension.equals(".mp4") && videoFile.exists()) {
            try {
                fetchFrame(videoSavePath + videoName, thumbnailPath + thumbnailName);
//                generateVideoPic(videoSavePath + videoName, thumbnailPath, thumbnailName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return thumbnailName;
        } else {
            //如果不是.mp4文件就不截图
            return "没有生成缩略图";
        }
    }

    public boolean generateThumByRelativePath(String videoRelativePath, String thumbnailRelativePath) {
        //判断视频文件是否存在
        File videoFile = new File(videoRelativePath);
        if (!videoFile.exists()) return false;
        //获取文件名后缀并转小写
        String videoName = videoFile.getName();
        String extension = videoName.substring(videoName.lastIndexOf(".")).toLowerCase();
        //如果是.mp4文件并且视频文件存在才进行截图
        if (extension.equals(".mp4")) {
            try {
                fetchFrame(videoRelativePath, thumbnailRelativePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        } else {
            //如果不是.mp4文件就不截图
            return false;
        }
    }


    /**
     * 生成缩略图工具
     *
     * @param videofile
     * @param framefile
     * @throws Exception
     */
    public void fetchFrame(String videofile, String framefile) throws Exception {
        long start = System.currentTimeMillis();
        File targetFile = new File(framefile);
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videofile);
        ff.start();
        int lenght = ff.getLengthInFrames();
        int i = 0;
        Frame f = null;
        while (i < lenght) {
            // 过滤前5帧，避免出现全黑的图片，依自己情况而定
            f = ff.grabFrame();
            if ((i > 5) && (f.image != null)) {
                break;
            }
            i++;
        }
        //IplImage img = f.image;
        int owidth = f.imageWidth;
        int oheight = f.imageHeight;
        // 对截取的帧进行等比例缩放
        int width = 300;
        int height = (int) (((double) width / owidth) * oheight);
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage fecthedImage = converter.getBufferedImage(f);
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        bi.getGraphics().drawImage(fecthedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH),
                0, 0, null);
        ImageIO.write(bi, "jpg", targetFile);
        ff.stop();
        System.out.println(System.currentTimeMillis() - start);
    }

    /**
     * 截取视频生成指定帧的图片
     *
     * @param videoUrl   源视频文件绝对路径
     * @param parentPath 截图存放父目录路径
     * @param fileName   截图文件名
     */
    public static void generateVideoPic(String videoUrl, String parentPath, String fileName) {
        File video = new File(videoUrl);
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(video);
        try {
            ff.start();
            String rotate_old = ff.getVideoMetadata("rotate");//视频旋转角度，可能是null

            // 截取中间帧图片(可改动)
            int i = 0;
            int length = ff.getLengthInFrames();
            int middleFrame = length / 2;
            Frame frame = null;
            while (i < length) {
                frame = ff.grabFrame();
                if ((i > middleFrame) && (frame.image != null)) {
                    break;
                }
                i++;
            }
            // 截取的帧图片
            Java2DFrameConverter converter = new Java2DFrameConverter();
            BufferedImage srcImage = converter.getBufferedImage(frame);
            int srcImageWidth = srcImage.getWidth();
            int srcImageHeight = srcImage.getHeight();

            // 对截图进行等比例缩放(缩略图)
            int width = 1280;
            int height = (int) (((double) width / srcImageWidth) * srcImageHeight);
            BufferedImage thumbnailImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            thumbnailImage.getGraphics().drawImage(srcImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);

            File folder = new File(parentPath);
            //判断文件父目录是否存在
            if (!folder.exists() || !folder.isDirectory()) {
                //删除不是文件夹的同名文件后创建文件夹
                folder.delete();
                folder.mkdirs();
            }
            File picFile = new File(parentPath, fileName);
            ImageIO.write(thumbnailImage, "jpg", picFile);
            ff.stop();
            //有需要旋转(比如手机视频)
            if (rotate_old != null && !rotate_old.isEmpty()) {
                int rotate = Integer.parseInt(rotate_old);
                rotatePhonePhoto(picFile.getAbsolutePath(), rotate);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 旋转照片
     *
     * @return
     */
    public static String rotatePhonePhoto(String fullPath, int angel) {
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
}
