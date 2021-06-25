package com.github.wxiaoqi.security.jd.sys.rest;

import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.jd.sys.biz.NoticeInfoBiz;
import com.github.wxiaoqi.security.jd.sys.entity.NoticeInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公告信息（无系统使用）
 */
@RestController
@RequestMapping("NoticeInfo")
public class NoticeInfoController extends BaseController<NoticeInfoBiz, NoticeInfo> {

    /**
     * @api {get} /NoticeInfo/pageByCondition 分页展示（优先展示未处理的数据，然后再根据时间排序，时间为降序排列）
     * @apiDescription 分页展示（优先展示未处理的数据，然后再根据时间排序，时间为降序排列）
     * @apiParam {Integer} page 页码
     * @apiParam {Integer} limit 行数
     * @apiParam {Integer} id 编号
     * @apiParam {Date} date 时间
     * @apiParam {Integer} type 类型(1水产，2种植)(=)
     * @apiParam {Integer} status 状态(0未发布，1已发布)(=)
     * @apiParam {String} content 内容
     * @apiParam {String} title 标题
     * @apiParam {Integer} creatorUserId 创建者用户id(=)
     * @apiParam {String} creatorName 创建者名称
     * @apiGroup 公告信息
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("pageByCondition")
    public TableResultResponse<NoticeInfo> pageByCondition(@RequestParam Map<String, Object> params) throws Exception {
        String orderBy = "status asc,date desc";
        Map<String, Object> toMap = new HashMap<>();
        toMap.put("type", params.get("type"));
        toMap.put("status", params.get("status"));
        toMap.put("creatorUserId", params.get("creatorUserId"));
        TableResultResponse<NoticeInfo> data = baseBiz.selectByQuery(null, orderBy, params, null, null, null,
                toMap, null, null, null, null, null, null, null,
                null, null, null, null, null, null,
                null, null, null, null, null, null);
        List<NoticeInfo> list = data.getData().getRows();
        if (list != null) {
            return data;
        }
        return new TableResultResponse<>(0, new ArrayList<>());
    }
}
