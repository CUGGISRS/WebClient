package com.lomoye.easy;

import com.lomoye.easy.domain.ConfigurableSpider;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


public class ListPatternProcessor implements PageProcessor {

    private ConfigurableSpider metaInfo;

    private Site site = Site
            .me()
            .setUserAgent(
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");

    @Override
    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    public void process(Page page) {
        //列表页
        // 部分二：定义如何抽取页面信息，并保存下来
        //如果不满足正则条件，则该url为详情页的url
        boolean match = page.getUrl().regex(metaInfo.getListRegex()).match();

        Selectable url = page.getUrl();
        String listRegex = metaInfo.getListRegex();
        Selectable regex = url.regex(listRegex);
        //index.aspx?id=15&amp;Page=2
        //获取页面所有的链接
        Selectable links = page.getHtml().links();

        List<String> all = page.getHtml().links().regex(metaInfo.getListRegex()).all();
        boolean b = all.size() == 0;
        //改造 end;;
        boolean match1 = regex.match();
        System.out.println("判断是列表页还是正文页,判断页面链接与列表正则是否符合");
        if (page.getUrl().regex(metaInfo.getListRegex()).match()) {
            System.out.println("列表页");
            // 部分三：从页面发现后续的url地址来抓取
            List<String> linksUrls = page.getHtml().links().regex(metaInfo.getListRegex()).all();
            //添加后续url  农机技术必需修改此处
            //ArrayList<String> arrayList = new ArrayList();
            /*for (String link:linksUrls){
                link = "http://www.camn.agri.gov.cn/sites/MainSite/"+link;
                arrayList.add(link);
            }*/
            //page.addTargetRequests(arrayList);
            page.addTargetRequests(page.getHtml().links().regex(metaInfo.getListRegex()).all());
            LinkedHashMap<String, String> fields = metaInfo.getFields();
            metaInfo.getFields().forEach((k, v) -> {
                List<String> all1 = page.getHtml().xpath(v).all();
                //向resultItems添加数据,供Pipeline回调方法使用
                //添加列表页正文url
                page.putField(k,page.getHtml().xpath(v).all());
            });
        } else {
            //正文页
            LinkedHashMap<String, String> contentFields = metaInfo.getContentFields();
            contentFields.forEach((k, v) -> {
                Html html = page.getHtml();
                Selectable xpath = html.xpath(v);
                List<String> all1 = xpath.all();
                List<String> content = page.getHtml().xpath(v).all();
                System.out.println("正文内容"+content);
                page.putField(k, page.getHtml().xpath(v).all());
                Object k1 = page.getResultItems().get(k);
//                System.out.println(k1);
            });
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public void setMetaInfo(ConfigurableSpider metaInfo) {
        this.metaInfo = metaInfo;
        // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
        getSite().setSleepTime(metaInfo.getSleepTime() == null ? 5000 : metaInfo.getSleepTime() * 1000)
                 .setRetryTimes(metaInfo.getRetryTimes() == null ? 0 : metaInfo.getRetryTimes())
                 .setRetrySleepTime(metaInfo.getRetrySleepTime() == null ? 1000 : metaInfo.getRetrySleepTime())
                 .setCycleRetryTimes(metaInfo.getCycleRetryTimes() == null ? 0 : metaInfo.getCycleRetryTimes())
                 .setTimeOut(metaInfo.getTimeOut() == null ? 5000 : metaInfo.getTimeOut() * 1000);
    }
}