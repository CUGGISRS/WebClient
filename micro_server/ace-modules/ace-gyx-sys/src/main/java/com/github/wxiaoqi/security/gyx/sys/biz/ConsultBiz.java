package com.github.wxiaoqi.security.gyx.sys.biz;


import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.dzsw.sys.constants.CommonConstants;
import com.github.wxiaoqi.security.gyx.sys.entity.ConsultationInfo;
import com.github.wxiaoqi.security.gyx.sys.entity.ExpertInfo;
import com.github.wxiaoqi.security.gyx.sys.mapper.ConsultationInfoMapper;
import com.github.wxiaoqi.security.gyx.sys.mapper.ExpertInfoMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class ConsultBiz {

    @Value("${server.port}")
    private String port;

    @Value("${server.ip}")
    private String ip;

    //url前缀
    @Value("${UploadFile.SON_PATH}")
    private String SON_PATH;

    @Resource
    private ConsultationInfoMapper consultationInfoMapper;
    @Resource
    private ExpertInfoMapper expertInfoMapper;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public ObjectRestResponse<Map<String, Object>> getPageBy(int[] ints, Map<String, Object> params) {
        String state = null; // 回复状态  0表未回复，1表示已回复
        String title = null; //标题
        String expertName = null; //专家名称
        String userName = null; // 留言人
        if (params.containsKey("state")) {
            state = (String) params.get("state");
        }
        if (params.containsKey("title")) {
            title = "%" + params.get("title") + "%";
        }
        if (params.containsKey("expertName")) {
            expertName = "%" + params.get("expertName") + "%";
        }
        if (params.containsKey("userName")) {
            userName = "%" + params.get("userName") + "%";
        }
        try {
            List<JSONObject> consultationInfo = consultationInfoMapper.getPageByState(ints[0], ints[1], state, title, expertName, userName);
            int i = consultationInfoMapper.getPageByStateCount(ints[0], ints[1], state, title, expertName, userName);

            Map<String, Object> map = new HashMap<>();
            map.put("rows", consultationInfo);
            map.put("total", i);
            return new ObjectRestResponse<>(StatusCode.SUCCESS, map);
        } catch (Exception ex) {
            return new ObjectRestResponse(StatusCode.FAIL, ex);
        }
    }

    public ObjectRestResponse<Integer> add(ConsultationInfo consultationInfo) {
        consultationInfo.setTime(new Date());
        int insertSelective = consultationInfoMapper.insertSelective(consultationInfo);
        if (insertSelective > 0) {
            return new ObjectRestResponse<>(StatusCode.SUCCESS, insertSelective);
        }
        return new ObjectRestResponse<>(StatusCode.FAIL);
    }

    public ObjectRestResponse<HashMap<String, Object>> getByUserId(Integer page, Integer size, Integer userId) {
        //分页
        PageHelper.startPage(page, size);
        //过滤
        Example example = new Example(ConsultationInfo.class);
        example.createCriteria().andEqualTo("userId", userId);
        example.orderBy("time");
        List<ConsultationInfo> consultationInfos = consultationInfoMapper.selectByExample(example);
        //数量
//        Example example1 = new Example(ConsultationInfo.class);
//        example1.createCriteria().andEqualTo("userId",userId);
        int i = consultationInfoMapper.selectCountByExample(example);
        HashMap<String, Object> map = new HashMap<>();
        map.put("rows", consultationInfos);
        map.put("total", i);
        return new ObjectRestResponse<>(StatusCode.SUCCESS, map);
    }

    public ObjectRestResponse<Map<String, Object>> getOneById(Integer id) {
        //问答详情
        ConsultationInfo consultationInfo = consultationInfoMapper.selectByPrimaryKey(id);
        //专家信息
        ExpertInfo expertInfo = expertInfoMapper.selectByPrimaryKey(consultationInfo.getExpertId());
        if (ObjectUtil.isNotNull(expertInfo)) {
            expertInfo.setPhoto("http://" + ip + ":" + port + SON_PATH + expertInfo.getPhoto());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("askDetail", consultationInfo);
        map.put("expertInfo", expertInfo);
        if (ObjectUtil.isNotNull(consultationInfo)) {
            return new ObjectRestResponse<>(StatusCode.SUCCESS, map);
        }
        return new ObjectRestResponse<>(StatusCode.FAIL);
    }

    // 更新问答详情
    public ObjectRestResponse update(ConsultationInfo consultationInfo) {
        try {
            int affect = consultationInfoMapper.updateByPrimaryKeySelective(consultationInfo);
            if (affect > 0) {
                return new ObjectRestResponse(StatusCode.SUCCESS);
            } else {
                return new ObjectRestResponse(StatusCode.FAIL);
            }
        } catch (Exception ex) {
            return new ObjectRestResponse(StatusCode.FAIL);
        }
    }

    // 删除问答详情
    public ObjectRestResponse delete(List<Integer> ids) {
        try {
            int affect = 0;
            for (Integer id : ids) {
                affect = consultationInfoMapper.deleteByPrimaryKey(id);
            }
            if (affect > 0) {
                return new ObjectRestResponse(StatusCode.SUCCESS);
            } else {
                return new ObjectRestResponse(StatusCode.FAIL);
            }
        } catch (Exception ex) {
            return new ObjectRestResponse(StatusCode.FAIL);
        }
    }

}
