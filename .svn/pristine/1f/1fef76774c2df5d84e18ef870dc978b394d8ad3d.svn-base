package com.github.wxiaoqi.security.info.sys.rest;

import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.info.sys.biz.ArticleInfoBiz;
import com.github.wxiaoqi.security.info.sys.entity.ArticleAndSystem;
import com.github.wxiaoqi.security.info.sys.utils.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Api(tags = {"文章模块"})
@RestController
@RequestMapping("/article")
//@CrossOrigin
public class ArticleInfoController {

    @Resource
    private ArticleInfoBiz articleInfoBiz;

    @ApiOperation(notes = "", value = "分页查询文章")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageRow", value = "每页个数", required = true, paramType = "query"),
            @ApiImplicitParam(name = "sysType", value = "系统类型", required = true, paramType = "query"),
            @ApiImplicitParam(name = "articleType", value = "文章类型", required = true, paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态：0草稿箱 1已发布", required = true, paramType = "query"),
            @ApiImplicitParam(name = "title", value = "文章标题", required = false, paramType = "query")
    })
    @GetMapping("/list")
    public ObjectRestResponse<JSONObject> listArticle(HttpServletRequest request) {
        JSONObject jsonObject = CommonUtil.request2Json(request);
        //status 0草稿箱 1已发布
        CommonUtil.hasAllRequired(jsonObject, "pageNum,pageRow,sysType,articleType,status");
        return articleInfoBiz.listArticle(jsonObject);
    }

    @ApiOperation(notes = "只传id删除三系统，传sysType删除单系统", value = "假删除：根据文章id，系统类型;删除状态变为1")
    @DeleteMapping("/delStatus")
    public ObjectRestResponse<JSONObject> delStatusArticle(@RequestParam Integer id, String sysType) {
        return articleInfoBiz.delStatusArticle(id, sysType);
    }

    @ApiOperation(value = "真删除：根据文章id，系统类型;数据库移除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "query"),
            @ApiImplicitParam(name = "sysType", value = "系统类型", required = true, paramType = "query"),
    })
    @PostMapping("/delRealBySys")
    public ObjectRestResponse<JSONObject> delRealArticleBySys(@RequestParam int id, @RequestParam String sysType) {
        return articleInfoBiz.delRealArticleBySys(id, sysType);
    }

    @ApiOperation(value = "真删除：根据文章id，不分系统类型，数据库移除")
    @DeleteMapping("/delReal")
    public ObjectRestResponse<JSONObject> delRealArticle(@RequestParam int id) {
        return articleInfoBiz.delRealArticle(id);
    }

    @ApiOperation(value = "根据文章id，查询文章详情")
    @GetMapping("/id")
    public ObjectRestResponse<Map<String, Object>> get(@RequestParam int id) {
        return articleInfoBiz.getArticleById(id);
    }

    /**
     * @Description: JSONObject无法解析富文本
     */

    @ApiOperation(value = "新增文章")
    @PostMapping("/add")
    public ObjectRestResponse<T> addArticle(@RequestBody ArticleAndSystem article) {
        return articleInfoBiz.addNewArticle(article);
    }

    @ApiOperation(value = "修改文章")
    @PutMapping("/update")
    public ObjectRestResponse<T> updateArticle(@RequestBody ArticleAndSystem andSystem) {
        return articleInfoBiz.updateArticle(andSystem);
    }

    /*@ApiOperation(value = "传入文章标题，模糊搜索文章")
    @PutMapping("/search")
    public ObjectRestResponse<T> searchArticle(@RequestBody ArticleAndSystem andSystem) {
        return articleInfoBiz.updateArticle(andSystem);
    }*/

    @ApiOperation(value = "修改文章表的内容中网络图片变为本地图片，a标签跳转url变为空")
    @PutMapping("/updateAllContent")
    public ObjectRestResponse<Integer> updateAllArticleContent() {
        return new ObjectRestResponse<>(StatusCode.SUCCESS,articleInfoBiz.updateAllContent());
    }
}
