package com.github.wxiaoqi.security.dzsw.sys.rest;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.dzsw.sys.annotation.UserToken;
import com.github.wxiaoqi.security.dzsw.sys.biz.VideoInfoBiz;
import com.github.wxiaoqi.security.dzsw.sys.config.FileUploadConfig;
import com.github.wxiaoqi.security.dzsw.sys.entity.VideoInfo;
import com.github.wxiaoqi.security.dzsw.sys.service.FileUploadService;
import com.github.wxiaoqi.security.dzsw.sys.vo.JwtUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

@Api(tags = {"视频信息"})
@RestController
@RequestMapping("/video")
@ResponseBody
public class VideoInfoController {
    @Resource
    VideoInfoBiz videoInfoBiz;

//    @Autowired
//    private FileUploadService fileUploadService;
//
//    @Autowired
//    private FileUploadConfig fileUploadConfig;

    @ApiOperation(value = "新增一条视频信息", notes = "使用postman测试接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "videoName", value = "视频名称", required = true, paramType = "form"),
            @ApiImplicitParam(name = "author", value = "作者", required = false, paramType = "form"),
            @ApiImplicitParam(name = "watchTimes", value = "浏览量", required = false, paramType = "form"),
            @ApiImplicitParam(name = "description", value = "描述", required = false, paramType = "form"),
            @ApiImplicitParam(name = "type", value = "视频分类", required = false, paramType = "form"),
            @ApiImplicitParam(name = "file", value = "视频文件", required = false, paramType = "form"),
    })
    @RequestMapping(value = "/addVideo", method = RequestMethod.POST)
    public ObjectRestResponse addVideo(@RequestParam Map<String, String> params, MultipartFile file) {
        return videoInfoBiz.addVideo(params, file);
    }


    @ApiOperation(value = "删除:数据库移除")
    @DeleteMapping("/del")
    public ObjectRestResponse delVideo(@RequestParam Integer[] ids) {
        return videoInfoBiz.delVideo(ids);
    }

    @ApiOperation(value = "根据id查询单个视频详情")
    @GetMapping("/getVideoById")
    //url:?1
    public ObjectRestResponse getVideoById(@RequestParam Integer id) {
        return videoInfoBiz.getVideoById(id);
    }

//    /**
//     * 将状态设置为1
//     *
//     * @param
//     */
//    @PostMapping(value = "/setState")
//    public void setState(@RequestBody VideoInfo videoInfo) {
//        VideoInfo newVideoInfo = baseBiz.selectById(videoInfo.getId());
//        newVideoInfo.setState(1);
//        baseBiz.updateSelectiveById(newVideoInfo);
//    }

//    /**
//     * 上传分块文件
//     */
//    @PostMapping("/appendUpload2Server")
//    public void appendUpload2Server(HttpServletRequest request, HttpServletResponse response) {
//        boolean uploadSuccess = fileUploadService.appendUpload2Server(request, response);
//        if (uploadSuccess) {
//            int id = Integer.parseInt(request.getParameter("id"));
//            VideoInfo videoInfo = baseBiz.selectById(id);
//            videoInfo.setState(1);
//            baseBiz.updateSelectiveById(videoInfo);
//        }
//    }

//    /**
//     * 上传前先在数据库中添加一条数据，生成uuid返回给前端，并将这条数据的状态设置为0
//     *
//     * @param videoInfo
//     * @return
//     */
//    @PostMapping("/add2Server")
//    public ObjectRestResponse<VideoInfo> add2Sever(@UserToken JwtUser jwtUser, @RequestBody VideoInfo videoInfo) {
//        return baseBiz.addVideoInfo2Server(jwtUser, videoInfo);
//    }

//    @PostMapping("/upload")
//    public void upload(HttpServletRequest request, HttpServletResponse response, MultipartRequest multipartRequest) {  //, @RequestParam("file") MultipartFile multipartFile
//        PrintWriter printWriter = null;
//        try {
//            printWriter = response.getWriter();
//            String fileSize = request.getParameter("chunkSize");
//            Long totalSize = 0L;
//            if (fileSize != null && !fileSize.equals("")) {
//                totalSize = Long.valueOf(fileSize);
//            }
//            RandomAccessFile randomAccessfile = null;
//            // 记录当前文件大小，用于判断文件是否上传完成
//            long currentFileLength = 0;
//            // 记录当前文件的绝对路径
//            String fileSavePath = fileUploadConfig.getVideoSavePath();
//            String fileName = new String(request.getParameter("filename").getBytes("UTF-8"), "UTF-8");
//            File file = new File(fileSavePath + "/" + fileName);
//            // 存在文件
//            if (file.exists()) {
//                randomAccessfile = new RandomAccessFile(file, "rw");
//            } else {
//                // 不存在文件，根据文件标识创建文件
//                randomAccessfile = new RandomAccessFile(fileSavePath + "/" + fileName, "rw");
//            }
//            // 开始文件传输
////            InputStream in = request.getInputStream();
//            MultipartFile multipartFile = multipartRequest.getFile("file");
//            InputStream in = multipartFile.getInputStream();
//            randomAccessfile.seek(randomAccessfile.length());
//            byte b[] = new byte[1024];
//            int n;
//            while ((n = in.read(b)) != -1) {
//                randomAccessfile.write(b, 0, n);
//            }
//            currentFileLength = randomAccessfile.length();
//            // 关闭文件
//            closeRandomAccessFile(randomAccessfile);
////            randomAccessfile = null;
//            //整个文件上传完毕，修改文件后缀
//            if (currentFileLength == totalSize) {
//                String msg = "文件上传完毕";
//                System.out.println(msg);
//            }
//            printWriter.print(currentFileLength);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    //    public static void closeRandomAccessFile(RandomAccessFile rFile) {
//        if (null != rFile) {
//            try {
//                rFile.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
    @ApiOperation(value = "分页查询视频信息")
    @RequestMapping(value = "/videosByPage", method = RequestMethod.GET)
    public ObjectRestResponse page(@RequestParam Map<String, Object> params,@RequestParam(required = false) List<String> typeList) {
        return videoInfoBiz.getVideoListByPage(params, typeList);
    }

//    /**
//     * 获取所有视频（文件）列表
//     *
//     * @return
//     */
//    @GetMapping("/allVideos")
//    public ObjectRestResponse<List<VideoInfo>> allVideo(HttpServletRequest request) {
////        return baseBiz.selectAllVideoList(request);
//        return null;
//    }

//    /**
//     * 获取指定id的视频
//     *
//     * @param id
//     * @param request
//     * @return
//     */
//    @GetMapping("/VideoDetail")
//    public ObjectRestResponse<VideoInfo> VideoDetail(@RequestParam int id, HttpServletRequest request) {
////        return baseBiz.selectOneVideo(id, request);
//        return null;
//    }

}
