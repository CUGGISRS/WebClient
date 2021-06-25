package com.lomoye.easy.backend;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.Strings;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lomoye.easy.ListPatternProcessor;
import com.lomoye.easy.component.DefaultHttpClientDownloader;
import com.lomoye.easy.constants.CoreConstant;
import com.lomoye.easy.constants.JobStatus;
import com.lomoye.easy.domain.ConfigurableSpider;
import com.lomoye.easy.domain.Job;
import com.lomoye.easy.domain.ProxyChannel;
import com.lomoye.easy.proxy.ProxyProviderFactory;
import com.lomoye.easy.service.ConfigurableSpiderService;
import com.lomoye.easy.service.JobService;
import com.lomoye.easy.service.ProxyChannelService;
import com.lomoye.easy.service.impl.ExportService;
import com.lomoye.easy.utils.LocalDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.proxy.ProxyProvider;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;

/**
 *
 */
@Slf4j
@Service
public class JobBackendService extends Thread {

    /**
     * 爬虫链接和进度持久化文件夹
     */
    @Value("${spider.file-cache-queue-dir}")
    private String fileCacheQueueDir;

    @Autowired
    private JobService jobService;

    @Autowired
    private ProxyChannelService proxyChannelService;

    @Autowired
    private ConfigurableSpiderService configurableSpiderService;

    @Autowired
    private ExportService exportService;

    /**
     * 运行爬虫任务线程池,线程池之ThreadPoolExecutor
     */
    private ExecutorService spiderExecutor = new ThreadPoolExecutor(
            8,//核心线程池大小
            8,//最大线程池大小
            0,//线程最大空闲时间
            TimeUnit.SECONDS,//时间单位
            new LinkedBlockingQueue<>(),//线程等待队列
            new ThreadFactoryBuilder().setNameFormat("spider-pool-%d").build(),//线程创建工厂
            new ThreadPoolExecutor.AbortPolicy());//拒绝策略
    /**
     * 统计爬虫线程池
     */
    private ExecutorService statExecutor = new ThreadPoolExecutor(
            8,
            8,
            0,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(),
            new ThreadFactoryBuilder().setNameFormat("stat-spider-pool-%d").build(),
            new ThreadPoolExecutor.AbortPolicy());

    @Override
    public void run() {
        //重启将进行中的任务设置为等待中
        jobService.updateRunningStatusToWaitStatus();
        //一直执行
        while (true) {
//            System.out.println("查找所有待执行的任务");
            QueryWrapper<Job> queryWrapper = new QueryWrapper<>();
            //查找所有待执行的任务
            List<Job> jobs = jobService.list(queryWrapper.lambda().eq(Job::getStatus, JobStatus.WAIT));
            if (CollectionUtils.isEmpty(jobs)) {
                try {
                    log.info("JobBackendService empty job sleep 1s");
                    //当前线程睡眠五秒
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                    log.info("job backend sleep error", e);
                }
                //不执行剩余的部分，直接进入下一次循环
                continue;
            }
            System.out.printf(Locale.CHINA, "遍历所有待执行任务:%d%n", jobs.size());
            for (final Job job : jobs) {
                log.info("job queuing uuid={}", job.getId());
                job.setStatus(JobStatus.QUEUING);//排队中
                job.setStartTime(LocalDateTime.now());
                job.setModifyTime(LocalDateTime.now());
                jobService.updateById(job);//修改任务状态
                //线程池中开启线程
                spiderExecutor.submit(() -> runJob(job));
            }
        }
    }

    private void runJob(final Job job) {
        log.info("job start run uuid={}", job.getId());
        //更新任务状态为运行中
        System.out.println("更新任务状态为运行中");
        updateJobStatusAsRunning(job);
        //通过job，获取配置信息
        ConfigurableSpider spider = configurableSpiderService.getById(job.getSpiderId());
        //将配置信息设置到spider中
        setSpiderFields(spider);
        //PageProcessor(链接提取和页面分析)
        System.out.println("新建PageProcessor页面处理器");
        ListPatternProcessor processor = new ListPatternProcessor();
        System.out.println("配置site");
        processor.setMetaInfo(spider);
        Map<String/*内容页链接*/, LinkedHashMap<String, String> /*列表页提取的数据*/> itemHolder = new ConcurrentHashMap<>();

        //构建正文页爬虫
        Spider contentWorker = buildContentWorker(spider, job, itemHolder, processor);
        //构建列表页爬虫
        Spider spiderWorker = buildListSpiderWorker(contentWorker, spider, job, itemHolder, processor);
        //将爬虫保存起来 用于控制其生命周期 比如停止爬虫
        SpiderHolder.putSpider(spiderWorker, contentWorker);

        //异步启动正文页和内容页的爬虫
        System.out.println("异步执行Spider");
        spiderWorker.runAsync();//列表页开启
        contentWorker.runAsync();//正文页开启

        //统计线程开启 统计任务爬取速度
        startStat(job);

        //阻塞当前线程 直到列表页爬虫没有任务
        blockCurrentThread(spiderWorker);

        //列表页爬虫停止了，唤醒通知可能休眠的内容页爬虫
        wakeupContentSpider(contentWorker);

        //阻塞当前线程 直到内容页爬虫没有任务
        blockCurrentThread(contentWorker);

        //爬虫容器移除uuid对应的爬虫
        SpiderHolder.removeSpider(spiderWorker.getUUID());

        //任务完成 跟新job
        finishJob(job);
    }

    private void finishJob(Job job) {
        Job selectedJob = jobService.getById(job.getId());
        if (selectedJob == null) {
            return;
        }
        //暂停状态返回
        if (Objects.equals(selectedJob.getStatus(), JobStatus.PAUSED)) {
            log.info("job paused");
            return;
        }

        selectedJob.setStatus(JobStatus.SUCCESS);
        selectedJob.setEndTime(LocalDateTime.now());
        selectedJob.setTimeCost(LocalDateUtil.getSecondInterval(selectedJob.getStartTime(), selectedJob.getEndTime()));
        jobService.updateById(selectedJob);
        log.info("job end run uuid={}", job.getId());
    }

    private void wakeupContentSpider(Spider contentWorker) {
        //内容页爬虫设置没有新任务时退出
        contentWorker.setExitWhenComplete(true);
        //不添加链接 间接唤醒线程
        contentWorker.addUrl();
    }

    private void blockCurrentThread(Spider spiderWorker) {
        while (spiderWorker.getStatus() != Spider.Status.Stopped) {
            try {
                sleep(1000);
                log.info("spiderWorker running sleep");
            } catch (InterruptedException e) {
                log.info("spiderWorker interruptedException", e);
            }
        }

        log.info("spiderWorker stoped|uuid={}", spiderWorker.getUUID());
    }

    private void startStat(Job job) {
        JobStater jobStater = new JobStater(job, jobService);
        statExecutor.submit(jobStater);
    }


    private Spider buildListSpiderWorker(Spider contentWorker, ConfigurableSpider spider, Job job, Map<String, LinkedHashMap<String, String>> itemHolder, ListPatternProcessor processor) {
        //创建
        Spider spiderWorker = Spider.create(processor);
        spiderWorker.setDownloader(new DefaultHttpClientDownloader());

        //设置爬虫代理
        setSpiderProxy(spiderWorker, spider);
        System.out.println("配置Spider");
        spiderWorker
                //线程数量
                .thread(spider.getThreadNum() == null ? 1 : spider.getThreadNum())
                //为蜘蛛设置一个uuid
                .setUUID(job.getId())
                //从 "填写的入口页地址" 开始抓,添加初始url
                .addUrl(spider.getEntryUrl())
                //负责抽取结果的处理，包括计算、持久化到文件、数据库等。
                .addPipeline((resultItems, task) -> {
                    /**
                     * ResultItems 相当于一个Map，它保存PageProcessor处理的结果，供Pipeline使用。
                     *
                     */
                    String requestUrl = resultItems.getRequest().getUrl();
                    log.info("get list page|url={}|uuid={}|spiderId={}", requestUrl, job.getId(), job.getSpiderId());

                    final List<LinkedHashMap<String, String>> resultList = new ArrayList<>();
                    Map<String, Object> resultMap = resultItems.getAll();
                    resultMap.forEach((k, v) -> {
                        List<String> vstr = (ArrayList<String>) v;
                        if (resultList.isEmpty()) {
                            for (int i = 0; i < vstr.size(); i++) {
                                resultList.add(new LinkedHashMap<>());
                            }
                        }
                        for (int i = 0; i < resultList.size(); i++) {
                            Map<String, String> obj = resultList.get(i);
                            obj.put(k, vstr.get(i));
                        }
                    });

                    //判断有没有正文页
                    if (Strings.isNullOrEmpty(spider.getContentXpath())) {
                        //没有正文页面 直接保存数据
                        configurableSpiderService.saveData(resultList, spider, task.getUUID());
                    } else {
                        //如果有正文页面 先把数据存放到内存里 等待后续处理完正文页后再一起保存
                        //resultList.forEach((v) -> {
                        for (LinkedHashMap<String, String> v : resultList) {
                            String url = v.get(CoreConstant.PAGE_URL);
                            //************根据正文url地址查询，防止重复爬取文章*************
//                            Integer num = exportService.getDataNumById(spider.getTableName(), CoreConstant.PAGE_URL, String.format("'%s'", url));
//                            if (num > 0) {
//                                continue;
//                            }
                            //********************************************************
                            //***********测试代码，根据时间防止重复爬取文章*************
                            //long time1 = DateUtil.parse(pubTime).getTime();
                            //判断url是否已经存在，如果存在则不做后续操作
                            /*if (time1 < 1603468800000L) {
                                job.setStatus(JobStatus.SUCCESS);
                                jobService.updateById(job);
                                SpiderHolder.removeSpider(job.getId());
                                spiderWorker.stop();
                                break;
                            }*/
                            //********************************************************
                            if (!Strings.isNullOrEmpty(url)) {

                                //把正文页的链接添加到带爬取页面
                                if (url.startsWith("//")) {
                                    url = "http:" + url;
                                }
                                //农机做特殊处理截取
                                //../.
                                else if (url.startsWith("../../")) {
                                    url = url.substring(6);
                                    url = "http://www.camn.agri.gov.cn/" + url;
                                }
                                //手动处理正文页无完整的url地址，然后对其拼接 gsy
                                else if (!StringUtils.isEmpty(spider.getContentUrl())) {
                                    url = spider.getContentUrl() + url;
                                }

                                if (!itemHolder.containsKey(url)) {
                                    itemHolder.put(url, v);
                                    contentWorker.addUrl(url);
                                }
                            }

                        }
                    }
                })
                //负责管理待抓取的URL，以及一些去重的工作。一般无需自己定制Scheduler。
                .setScheduler(new FileCacheQueueScheduler(System.getProperty("user.home") + "/" + fileCacheQueueDir));

        return spiderWorker;
    }

    private Spider buildContentWorker(ConfigurableSpider spider, Job job, Map<String, LinkedHashMap<String, String>> itemHolder, ListPatternProcessor processor) {
        //创建Spider
        System.out.println("Spider创建PageProcessor页面处理器");
        //使用一个PageProcessor创建一个Spider对象
        Spider contentWorker = Spider.create(processor);
        //问题：0.7.3版本自带的无法解决只支持ssl tlsv1.2的网站，如豆瓣
        //此处DefaultHttpClientDownloader继承了Downloader，解决上述问题，手动设置到Spider里
        contentWorker.setDownloader(new DefaultHttpClientDownloader());
        //设置爬虫代理
        setSpiderProxy(contentWorker, spider);

        if (!Strings.isNullOrEmpty(spider.getContentXpath())) {
            contentWorker
                    .thread(spider.getThreadNum() == null ? 1 : spider.getThreadNum())
                    .setUUID(job.getId())
                    //使用Pipeline保存结果
                    .addPipeline((resultItems, task) -> {
                        System.out.println("PageProcessor处理完后的回调");
                        String requestUrl = resultItems.getRequest().getUrl();
                        log.info("get content page|url={}|uuid={}|spiderId={}", requestUrl, job.getId(), job.getSpiderId());

                        Map<String, Object> resultMap = resultItems.getAll();

                        //合并数据,此时只有列表页的数据，及 文章标题，时间等
                        LinkedHashMap<String, String> item = itemHolder.get(requestUrl);


                        //把暂存的数据删了 防止内存溢出
                        itemHolder.remove(requestUrl);
                        //此时将内容页和列表页合并。列表页：文章标题，时间；内容页：文章简短描述，正文
                        resultMap.forEach((k, v) -> item.put(k, ((List) v).get(0).toString()));

                        //处理时间 覆盖之前的时间---------------------------------
                        if (item.containsKey("time")) {
                            try {
                                String timeRegex = spider.getTimeRegex();
                                if (!StringUtils.isEmpty(timeRegex)) {
                                    String time = item.get("time");
                                    String $1 = time.replaceAll(timeRegex, "$1");
                                    DateTime parse = DateUtil.parse($1);
                                    item.put("time", String.valueOf(parse));
                                }
                            } catch (Exception e) {
                                log.info("时间正则异常");
                            }
                        }
                        //处理时间 覆盖之前的时间--------------END-------------------

                        //保存
                        final List<LinkedHashMap<String, String>> resultList = new ArrayList<>();
                        resultList.add(item);
                        configurableSpiderService.saveData(resultList, spider, task.getUUID());

                    }).setScheduler(new FileCacheQueueScheduler(System.getProperty("user.home") + "/" + fileCacheQueueDir + "/content"));

        }
        contentWorker.setExitWhenComplete(false);
        return contentWorker;
    }

    private void setSpiderFields(ConfigurableSpider spider) {
        //数据库 json列表字段 转为LinkedHashMap
        String fieldsJson = spider.getFieldsJson();
        LinkedHashMap<String, String> stringStringLinkedHashMap = configurableSpiderService.parseFields(fieldsJson);
        spider.setFields(stringStringLinkedHashMap);
        if (!Strings.isNullOrEmpty(spider.getContentXpath()) && !Strings.isNullOrEmpty(spider.getContentFieldsJson())) {
            //数据库 json内容字段 转为LinkedHashMap
            spider.setContentFields(configurableSpiderService.parseFields(spider.getContentFieldsJson()));
            //设置 新增map中 键page_url 值为 正文Xpath
            spider.getFields().put(CoreConstant.PAGE_URL, spider.getContentXpath());
        }
    }

    private void updateJobStatusAsRunning(Job job) {
        job.setStatus(JobStatus.RUNING);
        job.setStartTime(LocalDateTime.now());
        job.setModifyTime(LocalDateTime.now());
        jobService.updateById(job);
    }

    /**
     * 设置爬虫代理
     */
    private void setSpiderProxy(Spider worker, ConfigurableSpider spider) {
        if (Strings.isNullOrEmpty(spider.getProxyChannelId())) {
            return;
        }

        ProxyChannel proxyChannel = proxyChannelService.getById(spider.getProxyChannelId());

        ProxyProvider proxyProvider = ProxyProviderFactory.build(proxyChannel);

        DefaultHttpClientDownloader httpClientDownloader = new DefaultHttpClientDownloader();
        httpClientDownloader.setProxyProvider(proxyProvider);

        worker.setDownloader(httpClientDownloader);
    }
}
