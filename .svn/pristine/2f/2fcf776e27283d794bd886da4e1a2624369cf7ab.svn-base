package com.github.wxiaoqi.security.news.rest;


import com.github.wxiaoqi.security.auth.client.annotation.IgnoreClientToken;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.common.util.Query;
import com.github.wxiaoqi.security.news.biz.NewsBiz;
import com.github.wxiaoqi.security.news.entity.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("simple")
public class SimpleNewsController {
    @Autowired
    protected HttpServletRequest request;
    @Autowired
    protected NewsBiz baseBiz;

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    @ResponseBody
    @IgnoreClientToken
    public TableResultResponse<News> simpleList(@RequestParam Map<String, Object> params) {
        String type = null;
        if (params.containsKey("type")) {
            type = params.get("type").toString();
            params.remove("type");
        }
        //查询列表数据
        Query query = new Query(params);
        return baseBiz.selectSimpleByQuery(query, type);
    }
}
