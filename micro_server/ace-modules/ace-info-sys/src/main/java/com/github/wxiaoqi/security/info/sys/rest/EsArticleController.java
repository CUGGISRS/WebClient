package com.github.wxiaoqi.security.info.sys.rest;

import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.info.sys.biz.ArticleInfoBiz;
import com.github.wxiaoqi.security.info.sys.entity.EsArticleInfo;
import com.github.wxiaoqi.security.info.sys.service.EsArticleService;
import com.github.wxiaoqi.security.info.sys.vo.ArticleSearchParam;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @description: 文章搜索
 * @author: gsy
 * @create: 2020-10-10 15:19
 **/
@RestController
@RequestMapping("search/article")
public class EsArticleController {

    @Value("${zdlc.elasticsearch.key_Article}")
    private String key;

    @Resource
    private EsArticleService esArticleService;

    /*gyx_system zsxx_system dzsw_system*/
    @PostMapping("/list")
    public ObjectRestResponse<JSONObject> list(@RequestBody ArticleSearchParam articleSearchParam) {
        return esArticleService.list(articleSearchParam.getPage(), articleSearchParam.getSize(), articleSearchParam);
    }

   /* @DeleteMapping("/delOne")
    public DeleteResponse delOne(@RequestParam Integer id, @RequestParam String targetSys) {
        EsArticleInfo esArticleInfo = new EsArticleInfo(key, id);
        return esArticleService.delOne(esArticleInfo.getDocId(), targetSys);
    }


    @PostMapping("/addOne/{targetSys}")
    public IndexResponse addOne(@PathVariable String targetSys, @RequestBody Map<String, Object> jsonMap) {
        return esArticleService.addOne(targetSys, new EsArticleInfo(key, jsonMap));
    }

    @PutMapping("/updateOne/{targetSys}")
    public UpdateResponse updateOne(@PathVariable String targetSys, @RequestBody Map<String, Object> jsonMap) {
        return esArticleService.updateOne(targetSys, new EsArticleInfo(key, jsonMap));
    }*/

    @GetMapping("/refreshAll")
    public ObjectRestResponse<JSONObject> refreshAll() {
        return esArticleService.refreshAll();
    }

}
