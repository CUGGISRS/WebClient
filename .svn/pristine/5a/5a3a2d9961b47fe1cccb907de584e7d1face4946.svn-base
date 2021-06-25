package com.emacsist.upload.demo.biz;

import com.emacsist.upload.demo.config.FileUploadConfig;
import com.emacsist.upload.demo.entity.DtVideo;
import com.emacsist.upload.demo.mapper.DtVideoMapper;
import com.emacsist.upload.demo.service.ThumbnailService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.wxiaoqi.security.auth.common.util.jwt.IJWTInfo;
import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.util.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * @author TsaiJun
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DtVideoBiz extends BaseBiz<DtVideoMapper, DtVideo> {

    @Autowired
    private FileUploadConfig fileUploadConfig;

    @Autowired
    private ThumbnailService thumbnailService;

    /**
     * 添加一条视频信息到数据库
     *
     * @param ijwtInfo
     * @param dtVideo
     * @return
     */
    public ObjectRestResponse<DtVideo> addVideoInfo2Server(IJWTInfo ijwtInfo, DtVideo dtVideo) {
        ObjectRestResponse<DtVideo> objectRestResponse;
        //获取前端传给我的扩展名
        String extension = dtVideo.getExtension();
        if (extension != null) {
            //设置上传者 即为当前登录用户
            dtVideo.setUserid(Integer.parseInt(ijwtInfo.getId()));
            //将上传状态设置为0
            dtVideo.setState(0);
            //当前上传时间
            Date date = new Date();
            dtVideo.setUploaddate(date);
            //生成UUID
            String uuid = GenerateUUID();
            //url为视频文件在服务器上的真实名称 uuid + extension即为服务器上真实视频名称
            dtVideo.setUrl(uuid + extension);
            try {
                //在数据库插入一条视频信息
                insertSelective(dtVideo);
                objectRestResponse = new ObjectRestResponse<>(StatusCode.SUCCESS, dtVideo, true);
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
     * 通过视频id删除视频信息同时删除缩略图
     *
     * @param id
     * @return
     */
    public boolean deletefileById(int id) {
        DtVideo dtVideo = selectById(id);
        if (dtVideo == null) {
            return false;
        }
        String fileName = dtVideo.getUrl();
        String thumbnailName = fileName.substring(0, fileName.lastIndexOf(".")) + ".jpg";
        File thumbnailFile = new File(fileUploadConfig.getThumbnailSavePath() + thumbnailName);
        File videoFile = new File(fileUploadConfig.getVideoSavePath() + fileName);
        //如果文件存在，就删除该文件。
        if (videoFile.exists()) {
            videoFile.delete();
            //如果缩略图也存在，同时也删除。
            if (thumbnailFile.exists()) {
                thumbnailFile.delete();
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 通过id列表实现多选删除
     *
     * @param idList
     * @return
     */
    public int deleteByIdList(List<Integer> idList) {
        int count = 0;
        for (Integer i : idList) {
            //删除服务器上的视频文件和缩略图
            deletefileById(i);
            //删除数据库中的记录
            count += deleteById(i);
        }
        return count;
    }

    /**
     * 通过Example查找视频列表
     *
     * @param property
     * @param value
     * @return
     */
    public ObjectRestResponse<List<DtVideo>> selectByExample(String property, String value, HttpServletRequest request) {
        Example example = new Example(DtVideo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(property, value);
        List<DtVideo> dtVideoList = generateThumbnailList(selectByExample(example), request);
        return new ObjectRestResponse<>(StatusCode.SUCCESS, dtVideoList);
    }

    /**
     * 获取所有视频列表和视频的缩略图
     *
     * @return
     */
    public ObjectRestResponse<List<DtVideo>> selectAllVideoList(HttpServletRequest request) {
        List<DtVideo> dtVideoList = generateThumbnailList(selectListAll(), request);
        return new ObjectRestResponse<>(StatusCode.SUCCESS, dtVideoList);
    }

    /**
     * 为单个视频生成缩略图
     * @param id
     * @param request
     * @return
     */
    public ObjectRestResponse<DtVideo> selectOneVideo(int id,HttpServletRequest request){
        DtVideo dtVideo=generateThumbnail(selectById(id),request);
        return new ObjectRestResponse<>(StatusCode.SUCCESS,dtVideo);
    }

    public DtVideo generateThumbnail(DtVideo dtVideo, HttpServletRequest request) {
        //网络协议
        String netProtocol = request.getScheme();
        //网络ip
        String ip = request.getServerName();
        //端口号
        int port = request.getServerPort();
        String videoUrlPrefix = netProtocol + "://" + ip + ":" + port + "/play/";
        String thumbnailPrefix = netProtocol + "://" + ip + ":" + port + "/show/";
        String fileRelName = dtVideo.getUrl();
        //生成缩略图
        String thumbnailName = thumbnailService.getThumbnail(fileRelName);
        dtVideo.setUrl(videoUrlPrefix + fileRelName);
        if (thumbnailName == null) {
            dtVideo.setThumbnail(null);
        } else {
            dtVideo.setThumbnail(thumbnailPrefix + thumbnailName);
        }
        return dtVideo;
    }

    /**
     * 为dtVideoList生成缩略图,为缩略图和视频添加ip和端口号
     *
     * @param dtVideoList
     * @return
     */
    public List<DtVideo> generateThumbnailList(List<DtVideo> dtVideoList, HttpServletRequest request) {
        //网络协议
        String netProtocol = request.getScheme();
        //网络ip
        String ip = request.getServerName();
        //端口号
        int port = request.getServerPort();
        for (DtVideo dtVideo : dtVideoList) {
            String videoUrlPrefix = netProtocol + "://" + ip + ":" + port + "/play/";
            String thumbnailPrefix = netProtocol + "://" + ip + ":" + port + "/show/";
            String fileRelName = dtVideo.getUrl();
            //生成缩略图
            String thumbnailName = thumbnailService.getThumbnail(fileRelName);
            dtVideo.setUrl(videoUrlPrefix + fileRelName);
            if (thumbnailName == null) {
                dtVideo.setThumbnail(null);
            } else {
                dtVideo.setThumbnail(thumbnailPrefix + thumbnailName);
            }
        }
        return dtVideoList;
    }

    /**
     * 分页展示
     *
     * @param params
     * @return
     */
    public TableResultResponse<DtVideo> getVideoListByPage(Map<String, Object> params, HttpServletRequest request) {
        Query query = new Query(params);
        Example example = getExampleByQuery(query);
        Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
        List<DtVideo> list = mapper.selectByExample(example);
        List<DtVideo> resultDtVideoList = generateThumbnailList(list, request);
        return new TableResultResponse<>(result.getTotal(), resultDtVideoList);
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
}