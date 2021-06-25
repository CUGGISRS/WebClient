package com.github.wxiaoqi.security.dzsw.sys.biz;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.common.util.Query;
import com.github.wxiaoqi.security.dzsw.sys.entity.QAInfo;
import com.github.wxiaoqi.security.dzsw.sys.entity.QAReplyInfo;
import com.github.wxiaoqi.security.dzsw.sys.mapper.QAInfoMapper;
import com.github.wxiaoqi.security.dzsw.sys.utils.CommonUtil;
import com.github.wxiaoqi.security.dzsw.sys.vo.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class QAInfoBiz extends BaseBiz<QAInfoMapper, QAInfo> {

    @Autowired
    private QAReplyInfoBiz qaReplyInfoBiz;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public ObjectRestResponse addQA(JwtUser jwtUser, Map<String, String> body) throws ParseException {
        int userID = jwtUser.getId(); // 用户id
        String userPhone = jwtUser.getPhone(); //用户手机号
        String userName = jwtUser.getName(); // 用户姓名
        QAInfo qaInfo = new QAInfo();
        if (body.containsKey("title")) {
            qaInfo.setTitle(body.get("title"));
        }
        if (body.containsKey("content")) {
            qaInfo.setContent(body.get("content"));
        }
        if (body.containsKey("messageTime")) {
            qaInfo.setMessageTime(simpleDateFormat.parse(body.get("messageTime")));
        }
        if (body.containsKey("status")) {
            qaInfo.setStatus(body.get("status"));
        }
        if (body.containsKey("targetSystem")) {
            qaInfo.setTargetSystem(body.get("targetSystem"));
        }
        if (body.containsKey("address")) {
            qaInfo.setAddress(body.get("address"));
        }
        if (body.containsKey("type")) {
            qaInfo.setType(body.get("type"));
        }
        qaInfo.setUserid(userID);
        qaInfo.setCommenterPhone(userPhone);
        qaInfo.setCommenter(userName);
        qaInfo.setIsDeleted(0);  // 设置删除状态
        qaInfo.setIsReply(0); // 设置为未回复状态
        int effect = mapper.insert(qaInfo);
        if (effect > 0) {
            return new ObjectRestResponse(StatusCode.SUCCESS, qaInfo);
        } else {
            return new ObjectRestResponse(StatusCode.FAIL);
        }
    }

    /**
     * 普通游客留言
     *
     * @return
     */
    public ObjectRestResponse addQA(Map<String, String> body) {
        try {
            QAInfo qaInfo = new QAInfo();
            qaInfo.setTitle(body.get("title"));
            qaInfo.setContent(body.get("content"));
            qaInfo.setCommenter(body.get("commenter"));
            qaInfo.setCommenterPhone(body.get("commenterPhone"));
            qaInfo.setTargetSystem(body.get("targetSystem"));
            Date curDate = new Date();
            qaInfo.setMessageTime(curDate); // 当前时间
            qaInfo.setStatus("待审核");
            qaInfo.setIsReply(0);   // 设置为未回复状态
            qaInfo.setIsDeleted(0); // 设置删除状态
            int effect = insert(qaInfo);
            if (effect > 0) {
                return new ObjectRestResponse<>(StatusCode.SUCCESS, qaInfo);
            } else {
                return new ObjectRestResponse(StatusCode.FAIL);
            }
        } catch (Exception ex) {
            return new ObjectRestResponse(StatusCode.FAIL);
        }
    }

    /**
     * 获取指定留言信息和回复信息
     *
     * @param qaId
     * @return
     */
    public ObjectRestResponse getQAById(int qaId) {
        QAInfo qaInfo = selectById(qaId);
        Example example = new Example(QAReplyInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("qaId", qaId);
        List<QAReplyInfo> list = qaReplyInfoBiz.selectByExample(example);
        qaInfo.setReplyInfos(list);
        return new ObjectRestResponse(StatusCode.SUCCESS, qaInfo);
    }

    /**
     * 按照时间倒序分页查询留言信息
     *
     * @param jsonObject
     * @return
     */
    public ObjectRestResponse listByTime(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        Example example = new Example(QAInfo.class);
        Example.Criteria criteria = example.createCriteria();
        if (jsonObject.containsKey("status")) {
            criteria.andEqualTo("status", jsonObject.get("status"));
        }
        criteria.andEqualTo("targetSystem", jsonObject.get("targetSystem"));
        criteria.andEqualTo("isDeleted", 0);
        example.setOrderByClause("message_time DESC");  // 按照留言时间倒序排序
        Page<Object> result = PageHelper.startPage(jsonObject.getIntValue("pageNum"), jsonObject.getIntValue("pageRow"));
        List<QAInfo> list = selectByExample(example);
        return new ObjectRestResponse<>(StatusCode.SUCCESS, CommonUtil.successPage(list, (int) result.getTotal()));
    }

    /**
     * 删除一条留言信息，同时删除回复信息
     */
    public ObjectRestResponse delQAById(int qaId) {
        try {
            Example example = new Example(QAReplyInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("qaId", qaId);
            List<QAReplyInfo> replyInfos = qaReplyInfoBiz.selectByExample(example);
            for (QAReplyInfo qaReplyInfo : replyInfos) {
                qaReplyInfoBiz.delete(qaReplyInfo);
            }
            QAInfo qaInfo = selectById(qaId);
            int affect = mapper.delete(qaInfo);
            if (affect > 0) {
                return new ObjectRestResponse(StatusCode.SUCCESS);
            } else {
                return new ObjectRestResponse(StatusCode.FAIL);
            }
        } catch (Exception ex) {
            return new ObjectRestResponse(StatusCode.FAIL, ex.getMessage());
        }
    }

    /**
     * 删除一条留言信息，同时删除回复信息
     */
    public ObjectRestResponse<QAInfo> delById(int qaId) throws Exception {
        QAInfo qaInfo = selectById(qaId);
        if(qaInfo!=null){
            int affect = deleteById(qaId);
            if (affect > 0) {
                //同时删除回复
                qaReplyInfoBiz.delByQaId(qaId);
                return new ObjectRestResponse(StatusCode.SUCCESS,true,qaInfo);
            }
            throw new Exception("删除失败");
        }
       return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR,false);
    }


    /**
     * 分页获取 留言信息
     *
     * @param params
     * @return
     */
    public ObjectRestResponse listByPage(Map<String, Object> params) {
        try {
            Query query = new Query(params);
            Example example = getExampleByQuery(query);
            example.setOrderByClause("message_time DESC");
            Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
            List<QAInfo> list = selectByExample(example);
            return new ObjectRestResponse<>(StatusCode.SUCCESS, CommonUtil.successPage(list, (int) result.getTotal()));
        } catch (Exception ex) {
            return new ObjectRestResponse<>(StatusCode.FAIL, ex);
        }

    }
}
