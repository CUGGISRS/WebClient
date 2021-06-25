package com.github.wxiaoqi.security.zs.sys.rest;

import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.zs.sys.biz.DeviceVideoBiz;
import com.github.wxiaoqi.security.zs.sys.entity.DeviceVideo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 物联网设备视频
 */
@RestController
@RequestMapping("DeviceVideo")
public class DeviceVideoController extends BaseController<DeviceVideoBiz, DeviceVideo> {

    /**
     * @api {get} /DeviceVideo/pageByEId 分页展示某一企业的物联网设备视频(id倒序)
     * @apiDescription 无
     * @apiParam {Integer} page 页码
     * @apiParam {Integer} limit 行数
     * @apiParam {Integer} id 编号
     * @apiParam {Integer} enterpriseId 所属企业id(非空，=)
     * @apiParam {String} serialNum 设备序列号
     * @apiParam {String} passNum 通道号
     * @apiParam {String} accessToken 权限token
     * @apiParam {String} verifyCode 验证码
     * @apiParam {Integer} isAutoPlay 是否自动播放(1是,0否)（=）
     * @apiParam {Integer} isOpenAudio 是否开启音频(1是,0否)（=）
     * @apiParam {String} title 标题
     * @apiGroup 物联网设备视频
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("pageByEId")
    public TableResultResponse<DeviceVideo> pageByGId(@RequestParam Map<String, Object> params) throws Exception {
        String orderBy = "id desc";
        Object enterpriseId = params.get("enterpriseId");
        if (MyObjectUtil.noNullOrEmpty(enterpriseId)) {
            Map<String, Object> toMap = new HashMap<>();
            toMap.put("enterpriseId", enterpriseId);
            toMap.put("isAutoPlay", params.get("isAutoPlay"));
            toMap.put("isOpenAudio", params.get("isOpenAudio"));
            TableResultResponse<DeviceVideo> data = baseBiz.selectByQuery(null, orderBy, params, null, null, null,
                    toMap, null, null, null, null, null, null, null,
                    null, null, null, null, null, null,
                    null, null, null, null, null, null);
            List<DeviceVideo> list = data.getData().getRows();
            if (list != null) {
                return data;
            }
        }
        return new TableResultResponse<>(0, new ArrayList<>());
    }
}
