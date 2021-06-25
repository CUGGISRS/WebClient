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
@RequestMapping("news")
public class NewsController extends BaseController<NewsBiz, News> {

}
