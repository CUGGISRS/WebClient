package com.github.wxiaoqi.security.dzsw.sys.biz;

import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.dzsw.sys.entity.QAInfo;
import com.github.wxiaoqi.security.dzsw.sys.entity.QAReplyInfo;
import com.github.wxiaoqi.security.dzsw.sys.mapper.QAReplyInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class QAReplyInfoBiz extends BaseBiz<QAReplyInfoMapper, QAReplyInfo> {

    @Autowired
    private QAInfoBiz qaInfoBiz;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 通过留言id删除回复
     */
    public int delByQaId(Integer qaId){
        if(qaId!=null){
            Map<String,Object> toMap=new HashMap<>();
            toMap.put("qaId",qaId);
            return delByToMap(toMap);
        }
        return 0;
    }


    public QAReplyInfo fillReply(QAReplyInfo qaReplyInfo, Map<String, String> body) throws ParseException {
        if (body.containsKey("content")) {
            qaReplyInfo.setContent(body.get("content"));
        }
        if (body.containsKey("replyTime")) {
            qaReplyInfo.setReplyTime(simpleDateFormat.parse(body.get("replyTime")));
        }
        if (body.containsKey("responder")) {
            qaReplyInfo.setResponder(body.get("responder"));
        }
        return qaReplyInfo;
    }

    public ObjectRestResponse replyQAByID(Map<String, String> body) throws ParseException {
        try {
            // 如果body中有id,表示已回复过，需要更新回复留言内容
            if (body.containsKey("id")) {
                QAReplyInfo qaReplyInfo = fillReply(selectById(body.get("id")), body);
                updateSelectiveById(qaReplyInfo);
                return new ObjectRestResponse(StatusCode.SUCCESS);
            } else {
                // 如果body中没有id,表示新增留言回复
                QAReplyInfo qaReplyInfo = new QAReplyInfo();
                if (!body.containsKey("qaId")) {
                    return new ObjectRestResponse(StatusCode.REQUEST_PARAM_ERROR, "未指定回复的留言id");
                } else {
                    QAInfo qaInfo = qaInfoBiz.selectById(Integer.valueOf(body.get("qaId")));
                    if (qaInfo == null)
                        return new ObjectRestResponse<>(StatusCode.FAIL, "留言信息不存在");
                    qaInfo.setIsReply(1);
                    qaInfoBiz.updateById(qaInfo);
                    qaReplyInfo.setQaId(Integer.valueOf(body.get("qaId")));
                }
                qaReplyInfo = fillReply(qaReplyInfo, body);
                qaReplyInfo.setIsDeleted(0);
                int effect = mapper.insert(qaReplyInfo);
                if (effect > 0) {
                    return new ObjectRestResponse<>(StatusCode.SUCCESS, qaReplyInfo);
                } else {
                    return new ObjectRestResponse<>(StatusCode.FAIL);
                }
            }
        } catch (Exception ex) {
            return new ObjectRestResponse(StatusCode.FAIL);
        }
    }

    // 通过留言id获取回复内容
    public ObjectRestResponse getReplyByqaId(int qaId) {
        try {
            Example example = new Example(QAReplyInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("qaId", qaId);
            List<QAReplyInfo> list = selectByExample(example);
            return new ObjectRestResponse<>(StatusCode.SUCCESS, list);
        } catch (Exception ex) {
            return new ObjectRestResponse<>(StatusCode.FAIL);
        }
    }

}
