package com.github.wxiaoqi.security.info.sys.rest;

import cn.hutool.core.util.PageUtil;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.info.sys.biz.CarouselBiz;
import com.github.wxiaoqi.security.info.sys.entity.CarouselInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @description: 轮播图相关
 * @author: gsy
 * @create: 2020-09-16 17:54
 **/
@Api(tags = "轮播图")
@RestController
@RequestMapping("carousel")
public class CarouselController {

    @Resource
    private CarouselBiz carouselBiz;

    @ApiOperation(value = "新增轮播图")
    @PostMapping("/add")
    public ObjectRestResponse<T> add(@RequestParam String targetSystem,
                                     Integer articleId,
                                     String articleTypeid,
                                     String status,
                                     String publisher,
                                     String url,
                                     Integer orderId,
                                     @RequestParam MultipartFile file) {
        CarouselInfo carouselInfo = new CarouselInfo();
        carouselInfo.setTargetSystem(targetSystem);
        carouselInfo.setArticleId(articleId);
        carouselInfo.setArticleTypeid(articleTypeid);
        carouselInfo.setStatus(status);
        carouselInfo.setPublisher(publisher);
        carouselInfo.setUrl(url);
        //排序序号
        carouselInfo.setOrderId(orderId);
        carouselInfo.setIsDeleted(0);

        return carouselBiz.add(carouselInfo, file);
    }

    @ApiOperation(value = "修改轮播图")
    @PutMapping("/update")
    public ObjectRestResponse<T> update(@RequestParam Integer id,
                                        Integer imgId,
                                        String targetSystem,
                                        Integer articleId,
                                        String articleTypeid,
                                        String status,
                                        String publisher,
                                        String url,
                                        Integer orderId,
                                        MultipartFile file) {
        CarouselInfo carouselInfo = new CarouselInfo();
        carouselInfo.setId(id);
        carouselInfo.setImg(imgId);
        carouselInfo.setTargetSystem(targetSystem);
        carouselInfo.setArticleId(articleId);
        carouselInfo.setArticleTypeid(articleTypeid);
        carouselInfo.setStatus(status);
        carouselInfo.setPublisher(publisher);
        carouselInfo.setUrl(url);
        carouselInfo.setOrderId(orderId);
        return carouselBiz.update(carouselInfo, file);
    }

    @ApiOperation(value = "分页查询轮播图")
    @GetMapping("/page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页个数", required = true, paramType = "query"),
            @ApiImplicitParam(name = "targetSystem", value = "目标系统", required = true, paramType = "query"),
    })
    public ObjectRestResponse<Map<String, Object>> page(@RequestParam Integer page,
                                                        @RequestParam Integer size,
                                                        @RequestParam String targetSystem) {
        int[] ints = PageUtil.transToStartEnd(page - 1, size);
        ints[1] = size;
        return carouselBiz.page(ints[0], ints[1], targetSystem);
    }

    @ApiOperation(value = "删除轮播图")
    @DeleteMapping("/del")
    public ObjectRestResponse<T> del(@RequestParam Integer id) {
        return carouselBiz.del(id);
    }

    @ApiOperation(value = "更新轮播图发布状态")
    @PutMapping("/updateStatus")
    public ObjectRestResponse<T> updateStatus(@RequestParam int id, String status) {
        return carouselBiz.updateStatus(id, status);
    }
}
