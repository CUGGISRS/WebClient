package com.emacsist.upload.demo.service;

import com.emacsist.upload.demo.config.FileUploadConfig;
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
        File videoFile =new File(videoSavePath+videoName);
        //如果是.mp4文件并且视频文件存在才进行截图
        if (extension.equals(".mp4") && videoFile.exists()) {
            try {
                fetchFrame(videoSavePath + videoName, thumbnailPath + thumbnailName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return thumbnailName;
        } else {
            //如果不是.mp4文件就不截图
            return "没有生成缩略图";
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
            if ((i > 48) && (f.image != null)) {
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

}
