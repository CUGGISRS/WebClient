package com.github.wxiaoqi.security.info.sys.rest;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.util.Query;
import com.github.wxiaoqi.security.info.sys.biz.SuggestBiz;
import com.github.wxiaoqi.security.info.sys.entity.Suggestion;
import com.github.wxiaoqi.security.info.sys.feign.IUserService;
import com.github.wxiaoqi.security.info.sys.utils.VerifyCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.HashMap;

/**
 * @description:
 * @author: gsy
 * @create: 2020-09-12 10:52
 **/


@RestController
@RequestMapping("/suggest")
@Api(tags = {"投诉建议相关接口"})
public class SuggestController {

    @Resource
    private SuggestBiz suggestBiz;

    @Resource
    IUserService iUserService;

    //根路径
    @Value("${UploadFile.ROOT_PATH}")
    private String ROOT_PATH;

    @ApiOperation(value = "获取图片验证码")
    @GetMapping("/ValidateCode")
    public void ValidateCode(HttpServletResponse response) throws IOException {
        productValidateImg(response);
//        HttpServletResponse httpServletResponse =
//        return httpServletResponse;
    }

    @ApiOperation(value = "提交投诉建议")
    @PostMapping("/add")
    public ObjectRestResponse<T> addSuggest(@RequestBody @Validated Suggestion suggestion) {

        return suggestBiz.addSuggest(suggestion);
    }

    @ApiOperation(value = "分页查询所有提交投诉")
    @PostMapping("/listByType")
    public TableResultResponse<Suggestion> listByType(Integer page, Integer limit, String type, String theme, Date before,Date after) {
        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("page", page);
        queryMap.put("limit", limit);
        Query query = new Query(queryMap);
        return suggestBiz.listByType(query, type, theme,before,after);
    }

    @ApiOperation(value = "标记投诉建议为已处理")
    @GetMapping("/deal")
    public ObjectRestResponse<T> deal(@RequestParam Integer id, @RequestParam String updName) {
        return suggestBiz.deal(id, updName);
    }

    public HttpServletResponse productValidateImg(HttpServletResponse response) throws IOException {
        VerifyCode verifyCode = new VerifyCode();

        //保存图片到指定的输出流
        verifyCode.output(verifyCode.getImage(), new FileOutputStream(ROOT_PATH + "\\UploadFile\\code.png"));
        //verifyCode.BufferedImageToBase64(verifyCode.getImage());
        String str = verifyCode.getText();
        boolean flag = iUserService.setCacheObject("verifyCode", str);

        //使用字节流读取本地图片
        ServletOutputStream out = null;
        BufferedInputStream buf = null;
        File file = new File(ROOT_PATH + "\\UploadFile\\code.png");
        //输入流读取文件
        buf = new BufferedInputStream(new FileInputStream(file));
        //获取字节流输出对象
        out = response.getOutputStream();
        try {
            IOUtils.copy(buf, out);
        } finally {
            //关闭流
            IOUtils.closeQuietly(buf);
            IOUtils.closeQuietly(out);
        }
        return response;
    }

}
