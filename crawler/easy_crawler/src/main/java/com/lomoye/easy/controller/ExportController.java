package com.lomoye.easy.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jfinal.plugin.activerecord.Db;
import com.lomoye.easy.domain.Job;
import com.lomoye.easy.model.ExportParams;
import com.lomoye.easy.model.common.ResultPagedList;
import com.lomoye.easy.model.export.AllTable;
import com.lomoye.easy.model.search.JobSearchModel;
import com.lomoye.easy.service.impl.ExportService;
import com.lomoye.easy.utils.DBUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * @description:
 * @author: gsy
 * @create: 2020-10-21 10:33
 **/

@RestController
@RequestMapping("export")
@Api(tags = "导出数据", description = "导出数据")
@EnableAsync
public class ExportController {

    @Resource
    private ExportService exportService;

    @PostMapping("getDataById")
    @ApiOperation("根据表名，梭哈已经爬取的数据到生产服务中")
    @Async
    public String getDataById(@RequestBody ExportParams exportParams) {
        List<JSONObject> resultList = exportService.getDataById(exportParams.getTableName());
        DBUtil dbUtil = new DBUtil();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DBUtil.getConnection();
            //遍历处理
            for(JSONObject result:resultList){
                System.out.println(Thread.currentThread().getName()+"==========主线程名");
                dbUtil.insert(result,connection,preparedStatement,exportParams);
            }
        } catch (SQLException throwables) {
            System.out.println("建立连接异常-----------------------------");
            throwables.printStackTrace();
        }finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        System.out.println("全部处理完成");
        return "wait";
    }

    @PostMapping("insertMarket")
    @ApiOperation("市场行情专用插入方法")
    public String insertMarket(@RequestBody ExportParams exportParams) {
        List<JSONObject> resultList = exportService.getDataById(exportParams.getTableName());
        DBUtil dbUtil = new DBUtil();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DBUtil.getConnection();
            //遍历处理
            for(JSONObject result:resultList){
                System.out.println(Thread.currentThread().getName()+"==========主线程名");
                //调用方法
                dbUtil.insertMarket(result,connection,preparedStatement,exportParams);
            }
        } catch (SQLException throwables) {
            System.out.println("建立连接异常-----------------------------");
            throwables.printStackTrace();
        }finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        System.out.println("全部处理完成");
        return "wait";
    }


    @GetMapping("testDataSource")
    @ApiOperation("测试多数据源")
    public String testDataSource() {
        DBUtil dbUtil = new DBUtil();
        return "ok";
    }

}
