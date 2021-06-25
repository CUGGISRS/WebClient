package com.emacsist.upload.demo.controller;

import com.emacsist.upload.demo.annotation.UserToken;
import com.emacsist.upload.demo.biz.DtVideoBiz;
import com.emacsist.upload.demo.config.FileUploadConfig;
import com.emacsist.upload.demo.entity.DtVideo;
import com.emacsist.upload.demo.service.FileUploadService;
import com.github.wxiaoqi.security.auth.common.util.jwt.IJWTInfo;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import org.apache.coyote.InputBuffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;

/**
 * @author TsaiJun
 */

@RestController
@RequestMapping("/file")
public class FileUploadController extends BaseController<DtVideoBiz, DtVideo> {

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private FileUploadConfig fileUploadConfig;

    /**
     * 将状态设置为1
     *
     * @param
     */
    @PostMapping(value = "/setState")
    public void setState(@RequestBody DtVideo dtVideo) {
        DtVideo dtVideo1 = baseBiz.selectById(dtVideo.getId());
        dtVideo1.setState(1);
        baseBiz.updateSelectiveById(dtVideo1);
    }

    /**
     * 上传分块文件
     */
    @PostMapping("/appendUpload2Server")
    public void appendUpload2Server(HttpServletRequest request, HttpServletResponse response) {
        boolean uploadSuccess = fileUploadService.appendUpload2Server(request, response);
        if (uploadSuccess) {
            int id = Integer.parseInt(request.getParameter("id"));
            DtVideo dtVideo1 = baseBiz.selectById(id);
            dtVideo1.setState(1);
            baseBiz.updateSelectiveById(dtVideo1);
        }
    }

    /**
     * 上传前先在数据库中添加一条数据，生成uuid返回给前端，并将这条数据的状态设置为0
     *
     * @param dtVideo
     * @return
     */
    @PostMapping("/add2Server")
    public ObjectRestResponse<DtVideo> add2Sever(@UserToken IJWTInfo ijwtInfo, @RequestBody DtVideo dtVideo) {
        return baseBiz.addVideoInfo2Server(ijwtInfo, dtVideo);
    }

    /**
     * 测试自定义注解
     *
     * @param ijwtInfo
     * @return
     */
    @GetMapping("/test")
    public IJWTInfo test(@UserToken IJWTInfo ijwtInfo) {
        return ijwtInfo;
    }

//    @PostMapping("/upload")
//    public String uploadChunk(HttpServletRequest request, MultipartRequest multipartRequest) {
//        MultipartFile file = multipartRequest.getFile("file");
//        String index = request.getParameter("chunkNumber");
//        String fileName = request.getParameter("filename");
//        try {
//            byte[] bytes = file.getBytes();
//            Path path = Paths.get(fileUploadConfig.getVideoSavePath() + fileName);
//            //文件写入指定路径
//            Files.write(path, bytes);
////            chunkService.saveChunk(chunk);
//            return "文件上传成功";
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "后端异常...";
//        }
//    }


    @PostMapping("/upload")
    public void upload(HttpServletRequest request, HttpServletResponse response, MultipartRequest multipartRequest) {  //, @RequestParam("file") MultipartFile multipartFile
        PrintWriter printWriter = null;
        try {
            printWriter = response.getWriter();
            String fileSize = request.getParameter("chunkSize");
            Long totalSize = 0L;
            if (fileSize != null && !fileSize.equals("")) {
                totalSize = Long.valueOf(fileSize);
            }
            RandomAccessFile randomAccessfile = null;
            // 记录当前文件大小，用于判断文件是否上传完成
            long currentFileLength = 0;
            // 记录当前文件的绝对路径
            String fileSavePath = fileUploadConfig.getVideoSavePath();
            String fileName = new String(request.getParameter("filename").getBytes("UTF-8"), "UTF-8");
            File file = new File(fileSavePath + "/" + fileName);
            // 存在文件
            if (file.exists()) {
                randomAccessfile = new RandomAccessFile(file, "rw");
            } else {
                // 不存在文件，根据文件标识创建文件
                randomAccessfile = new RandomAccessFile(fileSavePath + "/" + fileName, "rw");
            }
            // 开始文件传输
//            InputStream in = request.getInputStream();
            MultipartFile multipartFile = multipartRequest.getFile("file");
            InputStream in = multipartFile.getInputStream();
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
                String msg = "文件上传完毕";
            }
            printWriter.print(currentFileLength);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void closeRandomAccessFile(RandomAccessFile rFile) {
        if (null != rFile) {
            try {
                rFile.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping("/videosByPage")
    public TableResultResponse<DtVideo> page(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        return baseBiz.getVideoListByPage(params, request);
    }

    /**
     * 获取所有视频（文件）列表
     *
     * @return
     */
    @GetMapping("/allVideos")
    public ObjectRestResponse<List<DtVideo>> allVideo(HttpServletRequest request) {
        return baseBiz.selectAllVideoList(request);
    }

    /**
     * 获取指定id的视频
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/VideoDetail")
    public ObjectRestResponse<DtVideo> VideoDetail(@RequestParam int id, HttpServletRequest request) {
        return baseBiz.selectOneVideo(id, request);
    }

    /**
     * 根据标签（classification）获取视频视频（文件）列表
     *
     * @param classification
     * @return
     */
    @GetMapping("/video")
    public ObjectRestResponse<List<DtVideo>> seachVideoByClassification(@RequestParam String classification, HttpServletRequest request) {
        return baseBiz.selectByExample("classification", classification, request);
    }

    /**
     * 根据上传人（userid）获取视频（文件）列表
     *
     * @return
     */
    @GetMapping("/videoByme")
    public ObjectRestResponse<List<DtVideo>> seachVideoByUserId(@UserToken IJWTInfo ijwtInfo, HttpServletRequest request) {
        return baseBiz.selectByExample("userid", ijwtInfo.getId(), request);
    }

    /**
     * 通过视频id更新视频信息
     *
     * @param dtVideo
     * @return
     */
    @PostMapping("/updateById")
    public ObjectRestResponse updateById(@RequestBody DtVideo dtVideo) {
        ObjectRestResponse objectRestResponse = null;
        if (baseBiz.updateById(dtVideo) > 0) {
            objectRestResponse = new ObjectRestResponse<>(StatusCode.SUCCESS);
        } else {
            objectRestResponse = new ObjectRestResponse(StatusCode.FAIL);
        }
        return objectRestResponse;
    }

    /**
     * 通过视频id删除这条视频记录,同时要删除服务器上的视频文件
     *
     * @param id
     * @return
     */
    @PostMapping("/deleteById")
    public ObjectRestResponse deleteById(@RequestParam int id) {
        ObjectRestResponse objectRestResponse = null;
        //删除服务器上的视频文件
        if (baseBiz.deletefileById(id)) {
            objectRestResponse = new ObjectRestResponse(StatusCode.FILEDELETESUCCESS, "数据库记录已删除!");
        } else {
            objectRestResponse = new ObjectRestResponse(StatusCode.FILEDELETEFAIL, "数据库记录已删除!");
        }
        //删除数据库中的记录
        baseBiz.deleteById(id);
        return objectRestResponse;
    }

    /**
     * 通过idlist实现多选删除
     *
     * @param idList
     * @return
     */
    @PostMapping("/deleteByIdlist")
    public ObjectRestResponse deleteByIdlist(@RequestBody List<Integer> idList) {
        ObjectRestResponse objectRestResponse = null;
        int changCount = baseBiz.deleteByIdList(idList);
        if (changCount > 0) {
            objectRestResponse = new ObjectRestResponse(StatusCode.SUCCESS, "删除" + changCount + "条数据！");
        } else {
            objectRestResponse = new ObjectRestResponse(StatusCode.FAIL, "删除" + changCount + "条数据！");
        }
        return objectRestResponse;
    }


}
