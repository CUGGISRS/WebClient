package com.github.wxiaoqi.security.dzsw.sys.rest;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.dzsw.sys.biz.QAReplyInfoBiz;
import com.github.wxiaoqi.security.dzsw.sys.entity.QAReplyInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Map;

@Api(tags = {"留言问答回复"})
@RestController
@RequestMapping("/reply")
@ResponseBody
public class QAReplyInfoController extends BaseController<QAReplyInfoBiz, QAReplyInfo> {

    @Autowired
    private QAReplyInfoBiz qaReplyInfoBiz;

    @GetMapping("/test")
    public String test() {
        return "Success";
    }

    @PostMapping("/replyQAById")
    @ApiOperation(value = "回复指定id的留言")
    @ResponseBody
    public ObjectRestResponse replyQAById(@RequestBody Map<String, String> body) throws ParseException {
        return qaReplyInfoBiz.replyQAByID(body);
    }

    @PostMapping("/getReplyByqaId")
    @ApiOperation(value = "获取指定留言id的回复消息")
    public ObjectRestResponse getReplyByqaId(@RequestParam int qaId) throws ParseException {
        return qaReplyInfoBiz.getReplyByqaId(qaId);
    }
}
