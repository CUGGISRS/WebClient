package com.github.wxiaoqi.security.dzsw.sys.biz;

import cn.hutool.core.io.FileUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.common.util.Query;
import com.github.wxiaoqi.security.dzsw.sys.config.FileUploadConfig;
import com.github.wxiaoqi.security.dzsw.sys.constants.CommonConstants;
import com.github.wxiaoqi.security.dzsw.sys.entity.FileInfo;
import com.github.wxiaoqi.security.dzsw.sys.entity.VideoInfo;
import com.github.wxiaoqi.security.dzsw.sys.mapper.FileInfoMapper;
import com.github.wxiaoqi.security.dzsw.sys.mapper.VideoInfoMapper;
import com.github.wxiaoqi.security.dzsw.sys.service.ThumbnailService;
import com.github.wxiaoqi.security.dzsw.sys.utils.CommonUtil;
import com.github.wxiaoqi.security.dzsw.sys.vo.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class VideoInfoBiz extends BaseBiz<VideoInfoMapper, VideoInfo> {

    @Autowired
    private FileUploadConfig fileUploadConfig;

    @Autowired
    private ThumbnailService thumbnailService;

    @Autowired
    private FileInfoBiz fileInfoBiz;

    @Value("${server.port}")
    private String port;

    @Value("${server.ip}")
    private String ip;

    @Resource
    private FileInfoMapper fileInfoMapper;

    //根路径
    @Value("${UploadFile.ROOT_PATH}")
    private String ROOT_PATH;

    // 视频子目录
    @Value("${UploadFile.VIDEO_PATH}")
    private String VIDEO_PATH;

    // 缩略图子目录
    @Value("${UploadFile.THUMB_PATH}")
    private String THUMB_PATH;


    // 新增一条视频
    public ObjectRestResponse addVideo(Map<String, String> map, MultipartFile multipartFile) {
        try {
            Date curDate = new Date();
            VideoInfo videoInfo = fillByMap(map);
            videoInfo.setPubTime(curDate);
            videoInfo.setState(1);
            //在数据库插入一条视频信息
            int affect = mapper.insertSelective(videoInfo);
            if (affect == 0) {
                return new ObjectRestResponse(StatusCode.FAIL, false);
            }
            // 上传视频文件
            String path = fileInfoBiz.addVideo(multipartFile, CommonConstants.ULTABLE_CVI, videoInfo.getId());
            videoInfo.setPath(path);
            affect = mapper.updateByPrimaryKey(videoInfo);
            if (affect == 0) {
                return new ObjectRestResponse(StatusCode.FAIL, false);
            }
            return new ObjectRestResponse(StatusCode.SUCCESS);
        } catch (Exception ex) {
            return new ObjectRestResponse(StatusCode.FAIL);
        }
    }

    public ObjectRestResponse getVideoById(Integer id) {
        //查询单条视频信息
        VideoInfo videoInfo = selectById(id);
        if (videoInfo == null) {
            return new ObjectRestResponse<>(StatusCode.FAIL, false);
        }
        addHostAndPort(videoInfo);
        return new ObjectRestResponse<>(StatusCode.SUCCESS, videoInfo);
    }

    public void addHostAndPort(VideoInfo videoInfo) {
//        String host = null;
//        try {
//            host = InetAddress.getLocalHost().getHostAddress();
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
        String s = ip + ":" + port;
        videoInfo.setPath(s + videoInfo.getPath());
    }

    // 删除视频
    public ObjectRestResponse delVideo(Integer[] ids) {
        int i = 0;
        try {
            Example example = new Example(FileInfo.class);
            Example.Criteria criteria = example.createCriteria();
            List<Integer> delIdList = new ArrayList<>();
            Collections.addAll(delIdList, ids);
            criteria.andIn("tableId", delIdList);
            criteria.andEqualTo("linkTable", CommonConstants.ULTABLE_CVI);
            List<FileInfo> fileInfos = fileInfoMapper.selectByExample(example);
            //根据id删除中间表数据库保存的信息
            for (FileInfo file : fileInfos) {
                fileInfoMapper.deleteByPrimaryKey(file.getId());
            }
            //根据List<FileInfo>，取出路径进行批量删除图片
            delFilesByPath(fileInfos);
            //主表删除
            for (Integer id : ids) {
                i = mapper.deleteByPrimaryKey(id);
            }
        } catch (Exception e) {
            return new ObjectRestResponse<>(StatusCode.FAIL, e.getMessage());
        }
        if (i > 0) {
            return new ObjectRestResponse(StatusCode.SUCCESS, true);
        }
        return new ObjectRestResponse(StatusCode.FAIL, false);
    }

    public int delFilesByPath(List<FileInfo> fileInfos) {
        int i = 0;
        for (FileInfo fileinfo : fileInfos) {
            File file = new File(fileinfo.getPath());
            // 为文件时调用删除文件方法
            if (FileUtil.isFile(file)) {
                if (FileUtil.del(file)) {
                    i++;
                    System.out.println("删除成功");
                }
            }
        }
        return i;
    }

    public VideoInfo fillByMap(Map<String, String> map) {
        VideoInfo videoInfo = new VideoInfo();
        if (map.containsKey("id")) {
            videoInfo.setId(Integer.valueOf(map.get("id")));
        }
        if (map.containsKey("videoName")) {
            videoInfo.setVideoName(map.get("videoName"));
        }
        if (map.containsKey("state")) {
            videoInfo.setState(Integer.valueOf(map.get("state")));
        }
        if (map.containsKey("author")) {
            videoInfo.setAuthor(map.get("author"));
        }
        if (map.containsKey("description")) {
            videoInfo.setDescription(map.get("description"));
        }
        if (map.containsKey("type")) {
            videoInfo.setType(map.get("type"));
        }
        if (map.containsKey("watchTimes")) {
            videoInfo.setWatchTimes(map.get("watchTimes"));
        }
        return videoInfo;
    }

    /**
     * 添加一条视频信息到数据库
     *
     * @param jwtUser
     * @param videoInfo
     * @return
     */
    public ObjectRestResponse<VideoInfo> addVideoInfo2Server(JwtUser jwtUser, VideoInfo videoInfo) {
        ObjectRestResponse<VideoInfo> objectRestResponse;
        //获取前端传给我的扩展名
        String extension = videoInfo.getExtension();
        if (extension != null) {
            //将上传状态设置为0
            videoInfo.setState(0);
            //当前上传时间
            Date date = new Date();
            videoInfo.setPubTime(date);
            //生成UUID
            String uuid = GenerateUUID();
            //url为视频文件在服务器上的真实名称 uuid + extension即为服务器上真实视频名称
            videoInfo.setPath(uuid + extension);
            try {
                //在数据库插入一条视频信息
                insertSelective(videoInfo);
                objectRestResponse = new ObjectRestResponse<>(StatusCode.SUCCESS, videoInfo, true);
                return objectRestResponse;
            } catch (Exception e) {
                objectRestResponse = new ObjectRestResponse<>(StatusCode.FAIL, "插入一条数据出错");
                return objectRestResponse;
            }
        } else {
            //未传入文件后缀
            objectRestResponse = new ObjectRestResponse<>(StatusCode.FAIL, ",原因：未传入文件后缀。");
            return objectRestResponse;
        }
    }

    /**
     * 生成UUID
     *
     * @return
     */
    public String GenerateUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public ObjectRestResponse getVideoListByPage(Map<String, Object> params, List<String> typeList) {
        Query query = new Query(params);

        Map<String,Iterable> inMap=new HashMap<>();
        inMap.put("type",typeList);
        Example example = getExampleByConditions(null,null,query,null,null,
                null,null,null,null,null,null,
                null,null,inMap,null,null,null,
                null,null,null,null,null,
                null,null,null,null,null);
        Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
        List<VideoInfo> list = mapper.selectByExample(example);
        //获取本机ip
//        String host = null;
//        try {
//            host = InetAddress.getLocalHost().getHostAddress();
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
        String s = ip + ":" + port;
        String thumbPath;
        for (VideoInfo videoInfo : list) {
            if (videoInfo.getPath().equals("null")) {
                videoInfo.setPath(null);
                videoInfo.setThumbnail(null);
            } else {
                thumbPath = videoInfo.getPath().replace(VIDEO_PATH, THUMB_PATH);
                thumbPath = thumbPath.replace(".mp4", ".jpg");
                File thumbFile = new File(ROOT_PATH + thumbPath);
                if (thumbFile.exists()) {
                    videoInfo.setThumbnail(s + thumbPath);
                } else {
                    // 尝试生成缩略图
                    boolean bSuc = thumbnailService.generateThumByRelativePath(ROOT_PATH + videoInfo.getPath(), ROOT_PATH + thumbPath);
                    if (bSuc) {
                        videoInfo.setThumbnail(s + thumbPath);
                    }
                }
                videoInfo.setPath(s + videoInfo.getPath());
            }
        }
        return new ObjectRestResponse(StatusCode.SUCCESS, CommonUtil.successPage(list, (int) result.getTotal()));
    }


}
