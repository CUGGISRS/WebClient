package com.lomoye.easy.controller;

import com.alibaba.fastjson.JSONPObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Preconditions;
import com.lomoye.easy.domain.ConfigurableSpider;
import com.lomoye.easy.exception.BusinessException;
import com.lomoye.easy.exception.ErrorCode;
import com.lomoye.easy.model.ListPatternModel;
import com.lomoye.easy.model.SpiderContentTestModel;
import com.lomoye.easy.model.SpiderSettingModel;
import com.lomoye.easy.model.SpiderTestModel;
import com.lomoye.easy.model.common.ResultData;
import com.lomoye.easy.model.common.ResultList;
import com.lomoye.easy.model.common.ResultPagedList;
import com.lomoye.easy.model.search.ConfigurableSpiderSearchModel;
import com.lomoye.easy.service.ConfigurableSpiderService;
import com.lomoye.easy.utils.HttpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 2019/8/28 15:25
 * yechangjun
 * {
 * "entryUrl": "http://www.dianshangleida.com/list/appeal/1",
 * "listRegex": "http://www\\.dianshangleida\\.com/list/appeal/\\d+",
 * "fields": {"nick":"//*[@id='tam_newlist']/li/a/p[2]/span/text()",
 * "search_num":"//*[@id='tam_newlist']/li/a/p[3]/span/text()" },
 * "tableName": "ww_black_member"
 * }
 * <p>
 * {
 * "entryUrl": "http://www.xntk.net/mlist.php?t_id=0&page=0",
 * "listRegex": "http://www\\.xntk\\.net/mlist\\.php\\?t_id=0&page=\\d+",
 * "fields": {"book_name":"//*[@id='list']/tbody/tr/td[2]/a/text()",
 * "last_chapter":"//*[@id='list']/tbody/tr/td[3]/a/text()",
 * "type":"//*[@id='list']/tbody/tr/td[4]/font/text()",
 * "author":"//*[@id='list']/tbody/tr/td[5]/a/text()",
 * "update_time":"//*[@id='list']/tbody/tr/td[6]/small/text()" },
 * "tableName": "book"
 * }
 */
@RestController
@RequestMapping("/configurable-spider")
@Api(tags = "??????????????????", description = "?????????????????? lomoye")
public class ConfigurableSpiderController {

    @Autowired
    private ConfigurableSpiderService configurableSpiderService;

    @PostMapping
    @ApiOperation("??????????????????????????????")
    @ResponseBody
    public ResultData<ConfigurableSpider> addConfigurableSpider(@RequestBody ListPatternModel model) {
        if (null == model) {
            throw new BusinessException(ErrorCode.PARAMETER_ILLEGAL, "????????????????????????");
        }
        if (Strings.isEmpty(model.getEntryUrl())) {
            throw new BusinessException(ErrorCode.PARAMETER_ILLEGAL, "?????????????????????");
        }
        if (Strings.isEmpty(model.getListRegex())) {
            throw new BusinessException(ErrorCode.PARAMETER_ILLEGAL, "????????????????????????????????????");
        }
        if (Strings.isEmpty(model.getTableName())) {
            throw new BusinessException(ErrorCode.PARAMETER_ILLEGAL, "??????????????????????????????");
        }

        if (Strings.isEmpty(model.getFieldsJson()) || "[]".equals(model.getFieldsJson())) {
            throw new BusinessException(ErrorCode.PARAMETER_ILLEGAL, "????????????????????????????????????");
        }

        if (Strings.isNotEmpty(model.getContentXpath())) {
            if (Strings.isEmpty(model.getContentFieldsJson()) || "[]".equals(model.getContentFieldsJson())) {
                throw new BusinessException(ErrorCode.PARAMETER_ILLEGAL, "????????????????????????????????????");
            }
        }

        //??????????????????????????????
        QueryWrapper<ConfigurableSpider> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("table_name", model.getTableName());
        if (configurableSpiderService.count(queryWrapper) > 0) {
            throw new BusinessException(ErrorCode.PARAMETER_ILLEGAL, "??????????????????: " + model.getTableName());
        }

        ConfigurableSpider spider = ConfigurableSpider.valueOf(model);
        configurableSpiderService.save(spider);

        return new ResultData<>(spider);
    }

    @PutMapping("/settings")
    @ApiOperation("????????????????????????")
    @ResponseBody
    public ResultData<ConfigurableSpider> configConfigurableSpiderRunParam(@RequestBody SpiderSettingModel model) {
        if (model == null) {
            throw new BusinessException(ErrorCode.PARAMETER_ILLEGAL, "????????????????????????");
        }
        if (model.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMETER_ILLEGAL, "id????????????");
        }

        ConfigurableSpider spider = configurableSpiderService.getById(model.getId());
        if (spider == null) {
            throw new BusinessException(ErrorCode.PARAMETER_ILLEGAL, "???????????????");
        }
        if (model.getThreadNum() == null) {
            throw new BusinessException(ErrorCode.PARAMETER_ILLEGAL, "?????????????????????");
        }
        if (model.getSleepTime() == null) {
            throw new BusinessException(ErrorCode.PARAMETER_ILLEGAL, "?????????????????????????????????????????????");
        }
        if (model.getRetryTimes() == null) {
            throw new BusinessException(ErrorCode.PARAMETER_ILLEGAL, "??????????????????????????????????????????");
        }
        if (model.getRetrySleepTime() == null) {
            throw new BusinessException(ErrorCode.PARAMETER_ILLEGAL, "??????????????????????????????");
        }
        if (model.getCycleRetryTimes() == null) {
            throw new BusinessException(ErrorCode.PARAMETER_ILLEGAL, "??????????????????????????????????????????????????????");
        }
        if (model.getTimeOut() == null) {
            throw new BusinessException(ErrorCode.PARAMETER_ILLEGAL, "????????????????????????????????????");
        }

        spider.setThreadNum(model.getThreadNum());
        spider.setSleepTime(model.getSleepTime());
        spider.setRetryTimes(model.getRetryTimes());
        spider.setRetrySleepTime(model.getRetrySleepTime());
        spider.setCycleRetryTimes(model.getCycleRetryTimes());
        spider.setTimeOut(model.getTimeOut());
        spider.setModifyTime(LocalDateTime.now());
        configurableSpiderService.updateById(spider);

        return new ResultData<>(spider);
    }

    @PutMapping
    @ApiOperation("??????????????????????????????")
    @ResponseBody
    public ResultData<ConfigurableSpider> updateConfigurableSpider(@RequestBody ConfigurableSpider model) {
        if (model == null) {
            throw new BusinessException(ErrorCode.PARAMETER_ILLEGAL, "????????????????????????");
        }
        if (Strings.isEmpty(model.getEntryUrl())) {
            throw new BusinessException(ErrorCode.PARAMETER_ILLEGAL, "?????????????????????");
        }
        if (Strings.isEmpty(model.getListRegex())) {
            throw new BusinessException(ErrorCode.PARAMETER_ILLEGAL, "????????????????????????????????????");
        }
        if (Strings.isEmpty(model.getTableName())) {
            throw new BusinessException(ErrorCode.PARAMETER_ILLEGAL, "??????????????????????????????");
        }
        if (Strings.isEmpty(model.getFieldsJson()) || "[]".equals(model.getFieldsJson())) {
            throw new BusinessException(ErrorCode.PARAMETER_ILLEGAL, "????????????????????????????????????");
        }

        if (Strings.isNotEmpty(model.getContentXpath())) {
            if (Strings.isEmpty(model.getContentFieldsJson()) || "[]".equals(model.getContentFieldsJson())) {
                throw new BusinessException(ErrorCode.PARAMETER_ILLEGAL, "????????????????????????????????????");
            }
        }

        if (model.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMETER_ILLEGAL, "??????id????????????");
        }
        ConfigurableSpider spider = configurableSpiderService.getById(model.getId());
        if (spider == null) {
            throw new BusinessException(ErrorCode.PARAMETER_ILLEGAL, "???????????????");
        }
        if (!model.getTableName().equals(spider.getTableName())) {
            throw new BusinessException(ErrorCode.PARAMETER_ILLEGAL, "?????????????????????");
        }

        model.setModifyTime(LocalDateTime.now());
        configurableSpiderService.updateById(model);
        return new ResultData<>(spider);
    }

    @PostMapping("/config-proxy")
    @ApiOperation("????????????")
    @ResponseBody
    public ResultData<ConfigurableSpider> configSpiderProxy(@RequestBody ConfigurableSpider model) {
        ConfigurableSpider spider = configurableSpiderService.getById(model.getId());
        if (spider == null) {
            throw new BusinessException(ErrorCode.PARAMETER_ILLEGAL, "???????????????");
        }
        spider.setProxyChannelId(Strings.isEmpty(model.getProxyChannelId()) ? "" : model.getProxyChannelId());
        spider.setModifyTime(LocalDateTime.now());
        configurableSpiderService.updateById(spider);
        return new ResultData<>(spider);
    }

    @PostMapping("/search")
    @ApiOperation("????????????")
    @ResponseBody
    public ResultPagedList<ConfigurableSpider> searchConfigurableSpider(@RequestBody ConfigurableSpiderSearchModel searchModel) {
        IPage<ConfigurableSpider> page = new Page<>(searchModel.getPageNo(), searchModel.getPageSize());
        QueryWrapper<ConfigurableSpider> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(Strings.isNotEmpty(searchModel.getName()), ConfigurableSpider::getName, searchModel.getName())
                .like(Strings.isNotEmpty(searchModel.getTableName()), ConfigurableSpider::getTableName, searchModel.getTableName())
                .orderByDesc(ConfigurableSpider::getCreateTime);


        page = configurableSpiderService.page(page, queryWrapper);
        return new ResultPagedList<>(page.getRecords(), page.getTotal(), searchModel);
    }

    @PostMapping("/export/{id}")
    @ApiOperation("????????????")
    @ResponseBody
    public ResultData<ConfigurableSpider> export(@PathVariable String id) {
        //????????????,id????????????
        Preconditions.checkArgument(id != null);
        ConfigurableSpider configurableSpider = configurableSpiderService.getById(id);
        if (configurableSpider != null) {
            configurableSpider.setCreateTime(null);
            configurableSpider.setId(null);
            configurableSpider.setModifyTime(null);
            return new ResultData<>(configurableSpider);
        } else {
            throw new BusinessException(ErrorCode.PARAMETER_ILLEGAL, "????????????????????????????????????");
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("????????????")
    @ResponseBody
    public ResultData<Boolean> deleteConfigurableSpider(@PathVariable String id) {
        Preconditions.checkArgument(id != null);
        configurableSpiderService.removeById(id);
        return new ResultData<>(true);
    }

    @PostMapping("/test-xpath")
    @ApiOperation("??????xpath")
    @ResponseBody
    public ResultList<String> testXpath(@RequestBody SpiderTestModel testModel) {
        if (StringUtils.isBlank(testModel.getUrl())) {
            throw new BusinessException(ErrorCode.PARAMETER_ILLEGAL, "???????????????");
        }
        if (StringUtils.isBlank(testModel.getXpath())) {
            throw new BusinessException(ErrorCode.PARAMETER_ILLEGAL, "?????????XPATH");
        }
        Html html = HttpUtil.download(testModel.getUrl());
        return new ResultList<>(html.xpath(testModel.getXpath()).all());
    }

    @PostMapping("/test-content-xpath")
    @ApiOperation("???????????????xpath")
    @ResponseBody
    public ResultList<String> testContentXpath(@RequestBody SpiderContentTestModel testModel) {
        if (StringUtils.isBlank(testModel.getEntryUrl())) {
            throw new BusinessException(ErrorCode.PARAMETER_ILLEGAL, "?????????????????????");
        }
        if (StringUtils.isBlank(testModel.getContentXpath())) {
            throw new BusinessException(ErrorCode.PARAMETER_ILLEGAL, "??????????????????XPATH");
        }
        if (StringUtils.isBlank(testModel.getXpath())) {
            throw new BusinessException(ErrorCode.PARAMETER_ILLEGAL, "???????????????XPATH");
        }
        Html html = HttpUtil.download(testModel.getEntryUrl());
        String contentXpath = testModel.getContentXpath();
        List<String> links = html.xpath(testModel.getContentXpath()).all();
        if (CollectionUtils.isEmpty(links)) {
            return new ResultList<>();
        }
        String testLink = links.get(0);
        if (testLink.startsWith("//")) {
            testLink = "http:" + testLink;
        }else if (!StringUtils.isEmpty(testModel.getContentUrl())){
            testLink = testModel.getContentUrl() + testLink;
        }

        Html testPage = HttpUtil.download(testLink);
        List<String> values = testPage.xpath(testModel.getXpath()).all();
        values.add(0, testLink);
        return new ResultList<>(values);
    }

    @PostMapping("/test-regex")
    @ApiOperation("????????????,??????a???????????????url????????????")
    @ResponseBody
    public ResultList<String> testRegex(@RequestBody SpiderTestModel testModel) {
        if (StringUtils.isBlank(testModel.getUrl())) {
            throw new BusinessException(ErrorCode.PARAMETER_ILLEGAL, "???????????????");
        }
        if (StringUtils.isBlank(testModel.getRegex())) {
            throw new BusinessException(ErrorCode.PARAMETER_ILLEGAL, "???????????????");
        }
        Html html = HttpUtil.download(testModel.getUrl());
        Selectable links = html.links();
        Selectable regex = links.regex(testModel.getRegex());
        List<String> all = regex.all();
        return new ResultList<>(html.links().regex(testModel.getRegex()).all());

    }

    @PostMapping("/test-page")
    @ApiOperation("????????????")
    @ResponseBody
    public ResultData<String> testPage(@RequestBody SpiderTestModel testModel) {
        if (StringUtils.isBlank(testModel.getUrl())) {
            throw new BusinessException(ErrorCode.PARAMETER_ILLEGAL, "???????????????");
        }
        Html html = HttpUtil.download(testModel.getUrl());
        return new ResultData<>(html.toString());
    }

    @GetMapping("getDataById")
    @ApiOperation("??????id????????????????????????")
    public ResultData getDataById(@RequestParam String id) throws Exception {
        //????????????
        ConfigurableSpider byId = configurableSpiderService.getById(id);
        String tableName = byId.getTableName();

        //???????????????
        List<JSONPObject> dataById = configurableSpiderService.getDataById(tableName);

        return null;
    }

}
