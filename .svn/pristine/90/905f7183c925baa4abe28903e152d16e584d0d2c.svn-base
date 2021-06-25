package com.github.wxiaoqi.security.zs.sys.biz;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.IAcsClient;
import com.github.wxiaoqi.security.com.sys.constatns.CommonConstants;
import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.common.util.StringHelper;
import com.github.wxiaoqi.security.common.util.URLUtil;
import com.github.wxiaoqi.security.zs.sys.config.MyWebMvcConfigurer;
import com.github.wxiaoqi.security.zs.sys.entity.*;
import com.github.wxiaoqi.security.zs.sys.mapper.DeviceSensorDataMapper;
import com.github.wxiaoqi.security.zs.sys.util.SendSmsUtil;
import com.github.wxiaoqi.security.zs.sys.util.WLUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * 物联网设备传感器数据
 */
@Service
public class DeviceSensorDataBiz extends BaseBiz<DeviceSensorDataMapper, DeviceSensorData> {
    @Autowired
    private DeviceSensorBiz deviceSensorBiz;
    @Autowired
    private EnterpriseWlUserBiz enterpriseWlUserBiz;
    @Autowired
    private DeviceSensorDataBiz deviceSensorDataBiz;

    @Autowired
    private WarningInfoBiz warningInfoBiz;
    @Autowired
    private WarningThresholdBiz warningThresholdBiz;
    @Autowired
    private DeviceGroupBiz deviceGroupBiz;
    @Autowired
    private EnterpriseWLManagerBiz wlManagerBiz;

    @Autowired
    private MyWebMvcConfigurer myWebMvcConfigurer;

    @Autowired
    private IAcsClient iAcsClient;


    private long lastTime = 0;
    private boolean flag = true;//只有上一次执行完成才能执行下一次

    //控制同步事务
    @Autowired
    DataSourceTransactionManager dataSourceTransactionManager;
    @Autowired
    TransactionDefinition transactionDefinition;

    /**
     * 定时任务，同步物联网设备数据到传感器
     */
    @Scheduled(cron = "${Scheduled.DeviceSensorData.cron}")
    public void refreshUserPubKey() {
        System.out.println("定时任务开始");
        long interval = myWebMvcConfigurer.getInterval();
        long currentTime = System.currentTimeMillis();
        long item = currentTime - lastTime;
        System.out.println(interval + "--" + item);
        if (lastTime == 0 || interval < item) {
            System.out.println("调用同步接口");
            lastTime = currentTime;
            System.out.println("接口调用时间：" + lastTime);
            syncWLData();

        }
    }

    /**
     * 同步传感器数据,并确保执行完一次同步才执行下一次同步
     *
     * @throws Exception
     */
    public void syncWLData() {
        System.out.println(flag);
        if (!flag) {
            return;
        }
        //开启事务
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        String msg = null;
        try {
            flag = false;
            syncWLData0();
            dataSourceTransactionManager.commit(transactionStatus);//事务提交
            System.out.println("定时任务结束");
        } catch (Exception e) {
            msg = "异常信息：" + e.getMessage();
        } catch (Error error) {//内存溢出时处理变量
            msg = "错误信息：" + error.getMessage();
        } finally {
            if (msg != null) {
                dataSourceTransactionManager.rollback(transactionStatus);// 事务回滚
            }
            flag = true;
        }

    }


    /**
     * 发送报警短信 ${date}，${name}的数据：${data}，报警信息：${info}，请尽快处理！
     */
    public boolean sendWarningSms(JSONObject obj) {
        if (obj == null) {
            return false;
        }
        String phone = obj.getString("phone");
        if (StringHelper.isNullOrEmpty(phone)) {
            return false;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("date", StringHelper.dateYMdHMS2String(obj.getDate("date")));
        params.put("name", obj.getString("name"));
        params.put("data", obj.getString("data"));
        params.put("info", obj.getString("info"));
        boolean r = SendSmsUtil.sendSms(phone, myWebMvcConfigurer.getSignName(),
                myWebMvcConfigurer.getTemplateCode(), myWebMvcConfigurer.getAction()
                , myWebMvcConfigurer.getVersion(), myWebMvcConfigurer.getRegionId(), myWebMvcConfigurer.getDomain()
                , iAcsClient, params);
        return r;
    }


    /**
     * 通过传感器id删除数据
     */
    public int delBySId(Integer sId) {
        if (sId == null) {
            return 0;
        }
        Map<String, Object> toMap = new HashMap<>();
        toMap.put("sensorId", sId);
        return delByToMap(toMap);
    }

    /**
     * 通过传感器id集合删除数据
     */
    public int delBySIds(List<Integer> sIds) {
        String inFiled = "sensorId";
        return delByInFiledMayToMap(sIds, inFiled, null);
    }

    /**
     * 通过传感器id获得最小记录时间
     */
    public Date getMinTimeBySId(Integer sId) {
        if (sId == null) {
            return null;
        }
        return mapper.getMinTimeBySId(sId);
    }

    /**
     * (记录时间起点>=，记录时间终点<=，记录时间间隔)查询某一传感器的总数
     */
    public int getTotalBySId(Integer sId, Date start, Date end, Integer interval) {
        if (sId == null) {
            return 0;
        }
        return mapper.getTotalBySId(sId, start, end, interval);
    }

    /**
     * 查询某些传感器（记录时间起点>=，记录时间终点<=）的数据(记录时间倒序)
     */
    public List<DeviceSensorData> getByIdsMayOther(List<Integer> sIds, Date start, Date end) {
        String inFiled = "sensorId";
        String orderBy = "record_time desc";
        Map<String, Object> gtMap = new HashMap<>();
        Map<String, Object> ltMap = new HashMap<>();
        ltMap.put("recordTime", end);
        gtMap.put("recordTime", start);
        return getByInFiledMayCondition(sIds, inFiled, null, orderBy, null,
                null, null, null,
                null, null, null, gtMap, null, ltMap, null,
                null, null, null, null, null,
                null, null, null, null, null, null,
                null, null);
    }

    /**
     * （sql注入排序语句后）(记录时间起点>=，记录时间终点<=，记录时间间隔)某一传感器分页
     */
    public List<DeviceSensorData> pageBySId(Integer sId, Date start, Date end, Integer interval,
                                            Integer page, Integer limit, String orderBy) {
        if (sId == null) {
            return new ArrayList<>();
        }
        if (page != null && limit != null) {
            if (page > 0) {
                page = (page - 1) * limit;
            } else {
                page = null;
            }
            if (limit < 0) {
                limit = 0;
            }
        }
        List<DeviceSensorData> list = mapper.pageBySId(sId, start, end, interval, page, limit, orderBy);
        return list;
    }


    /**
     * 同步数据方法
     */
    private void syncWLData0() throws Exception {
        //存储预警信息集合
        List<WarningInfo> wis = new ArrayList<>();
        //存储需要发生短信的预警信息的分组id集合
        Set<Integer> gIds = new HashSet<>();
        //存储需要发生短信的企业物联网管理员id集合
        Set<Integer> eWlMIds = new HashSet<>();
        //存储发送短信的阈值id集合
        Set<Integer> wtIds = new HashSet<>();

        //最近需要发短信的集合
        JSONArray sendArr = new JSONArray();

        //分页查设备数据时时间条件必须都在，不然时间条件不起作用，故将当前时间设为结束时间
        Date endZero = new Date();
        Date startZero = new Date(0);

        //用来添加传感器数据的集合
        List<DeviceSensorData> dsdList = new ArrayList<>();
        //用来修改传感器记录时间的集合
        List<DeviceSensor> dsList = new ArrayList<>();
        //获得所有传感器
        List<DeviceSensor> list = deviceSensorBiz.selectListAll();
        if (MyObjectUtil.iterableCount(list) == 0) {
            return;
        }
        //获得所有传感器的预警阈值
        List<Integer> dsIds = MyObjectUtil.getIdsByList(list);
        List<WarningThreshold> wts = warningThresholdBiz.getBySIds(dsIds);
        //按照物联网用户id和设备id分组
        Map<Integer, Map<Integer, List<DeviceSensor>>> result = list.stream()
                //过滤null元素和相关属性
                .filter(e -> e != null && e.getEnterpriseWlUserId() != null && e.getFacId() != null && e.getGroupId() != null)
                .collect(Collectors.groupingBy(DeviceSensor::getEnterpriseWlUserId,
                        Collectors.groupingBy(DeviceSensor::getFacId)));

        //放在外面防止new过多对象
        Map<Integer, List<DeviceSensor>> value0;
        EnterpriseWlUser wlUser;
        List<DeviceSensor> deviceSensors;

        List<WarningThreshold> wtList0;
        DeviceSensor item0;

        JSONArray data;
        JSONObject dd;
        DeviceSensor item1;

        for (Map.Entry<Integer, Map<Integer, List<DeviceSensor>>> entry : result.entrySet()) {
            Integer ewlId = entry.getKey();
            //同一物联网用户
            value0 = entry.getValue();
            if (value0 == null || value0.size() == 0) {
                continue;
            }
            //获取token
            wlUser = enterpriseWlUserBiz.selectById(ewlId);
            String token = null;
            String type = null;
            Integer eId = null;

            //物联网设备通道记录时间键值
            String recordDateKey = null;
            if (wlUser != null) {
                eId = wlUser.getEnterpriseId();//获得企业id
                type = wlUser.getType();
                if (CommonConstants.WL_XPH.equals(type)) {//新普惠
                    recordDateKey = "dataTime";
                }
                token = WLUtil.getWLToken(wlUser.getUsername(), wlUser.getPassword(), type);
            }
            if (token == null || recordDateKey == null) {
                continue;
            }

            for (Map.Entry<Integer, List<DeviceSensor>> listEntry : value0.entrySet()) {
                Integer dId = listEntry.getKey();
                //同一物联网用户同一设备
                deviceSensors = listEntry.getValue();
                int len = MyObjectUtil.iterableCount(deviceSensors);
                if (len == 0) {
                    continue;
                }
                Date start = null;
                //获得最小记录时间  和当前传感器预警阈值
                for (int i = 0; i < len; i++) {
                    item0 = deviceSensors.get(i);
                    Integer dsId = item0.getId();
                    //获得阈值，存入传感器
                    if (wts != null) {
                        wtList0 = wts.stream()
                                .filter(e -> dsId.equals(e.getSensorId()) && e.getLevel() != null)
                                .sorted(Comparator.comparing(WarningThreshold::getLevel))//按照级别正序
                                .collect(Collectors.toList());
                        item0.setWtList(wtList0);
                    }

                    Date lastTime = item0.getLastRecordTime();
                    if (lastTime != null) {
                        if (start == null || lastTime.getTime() < start.getTime()) {
                            //加一秒防止重复查询数据
                            start = MyObjectUtil.handleDateSecond(lastTime, 1);
                        }
                    } else {
                        start = startZero;
                        continue;
                    }
                }

                long total = WLUtil.getSupplyDeviceDataTotal(token, dId, start, endZero, type);
                if (total == 0) {
                    continue;
                }
                long limit = myWebMvcConfigurer.getLimit();
                //分几次查询设备数据
                long countPage = (long) Math.ceil((double) total / limit);//总页数 向上取整
                long size = countPage + 1;
                //本次同步中最大记录时间
                Date lastDataTime = null;
                //本次同步中最大记录时间对应的设备通道数据
                JSONObject lastData = null;


                //多次分页插入全部数据
                for (long a = 1; a < size; a++) {
                    data = WLUtil.getSupplyDeviceData(token, dId, start, endZero,
                            String.valueOf(a), String.valueOf(limit), type);
                    if (data == null) {
                        continue;
                    }
                    int dataSize = data.size();
                    for (int k = 0; k < dataSize; k++) {
                        dd = data.getJSONObject(k);
                        Date recordDate = StringHelper.stringYMdHMS2Date(dd.getString(recordDateKey));
                        if (recordDate == null) {
                            continue;
                        }
                        long recordDateNum = recordDate.getTime();
                        if (lastDataTime == null || lastDataTime.getTime() < recordDateNum) {
                            lastDataTime = recordDate;
                            lastData = dd;
                        }

                        for (int j = 0; j < len; j++) {
                            DeviceSensor item = deviceSensors.get(j);
                            String pass = item.getFacPass();
                            String preC = item.getPreC();
                            Integer id = item.getId();
                            Date lastTime = item.getLastRecordTime();

                            long lastTimeNum = lastTime == null ? 0 : lastTime.getTime();
                            double preCD = Double.parseDouble(preC);
                            String passStr = dd.getString(pass);
                            if (StringHelper.isNullOrEmpty(passStr)) {
                                System.out.println("该设备通道数据为空：" + dd.toJSONString());
                                continue;
                            }
                            int passData = Integer.parseInt(passStr);

                            double itemData;
                            if (passData == 32767) {
                                //异常数据 跳过
                                continue;
                                //   itemData=passData;
                            } else {
                                //数值乘以分辨率,保留6位小数
                                itemData = passData * preCD;
                            }
                            BigDecimal recordData = MyObjectUtil.getSixDecimal(itemData);

                            if (lastTimeNum < recordDateNum) {//添加未记录数据
                                DeviceSensorData dsd = new DeviceSensorData(id, recordDate, recordData, endZero);
                                dsdList.add(dsd);

                                //判断是否预警
                                List<WarningThreshold> wtList = item.getWtList();
                                int wtLen = MyObjectUtil.iterableCount(wtList);
                                boolean isWarning = false;
                                String warningInfo = null;
                                Integer level = null;
                                if (wtLen > 0) {
                                    for (int i = 0; i < wtLen; i++) {
                                        WarningThreshold wt = wtList.get(i);
                                        BigDecimal min = wt.getMinValue();
                                        BigDecimal max = wt.getMaxValue();
                                        if (max == null && min == null) {
                                            continue;
                                        }
                                        //无最大值或小于等于最大值
                                        boolean maxF = max == null || recordData.compareTo(max) < 1;
                                        //无最小值或大于等于最小值
                                        boolean minF = min == null || recordData.compareTo(min) > -1;
                                        if (maxF && minF) {
                                            isWarning = true;
                                            level = wt.getLevel();
                                            warningInfo = wt.getWarningInfo();
                                            break;
                                        }
                                    }
                                }
                                if (isWarning) {
                                    Integer status = 0;//默认未读，批量添加必须设置
                                    WarningInfo wi = new WarningInfo(id, eId, recordDate, level, warningInfo, status, recordData);
                                    wis.add(wi);
                                    //判断是否发送短信
                                  /*  if (Integer.valueOf(1).equals(isSend)) {
                                        Integer gId = item.getGroupId();
                                        //Set集合内不允许有重复的数据元素,防止重复
                                        gIds.add(gId);
                                        eWlMIds.add(eWlMId);
                                        wtIds.add(wtId);

                                        String unit = item.getUnit();
                                        String name = item.getName();
                                        JSONObject obj = new JSONObject();
                                        //发短信时只保留2位小数
                                        String objectData = MyObjectUtil.getTwoDecimal(itemData).toString();
                                        if (unit != null) {
                                            objectData += unit;
                                        }
                                        obj.put("gId", gId);
                                        obj.put("eWlMId", eWlMId);
                                        obj.put("sName", name);
                                        obj.put("date", recordDate);
                                        obj.put("data", objectData);
                                        obj.put("info", warningInfo);
                                        obj.put("wtId", wtId);
                                        sendArr.add(obj);
                                    }*/
                                }

                            }
                        }
                    }

                    int dsdLen = dsdList.size();
                    //最后一次循环或集合存入元素超过限制
                    if (a == countPage || dsdLen > limit) {
                        //批量添加传感器数据
                        int addIndex = deviceSensorDataBiz.batchInsertByList(dsdList);
                        if (addIndex != dsdLen) {
                            throw new Exception("同步传感器数据失败");
                        }
                        //clear方法清空集合中数据的时候，这时候对象在系统中分配的内存还是只有一个，不会重新去创建分配新的内存，这样可以极大的优化程序的性能
                        dsdList.clear();
                    }

                    int wisLen = wis.size();
                    if (a == countPage || wisLen > limit) {
                        //批量添加报警信息
                        int addWIndex = warningInfoBiz.batchInsertByList(wis);
                        if (addWIndex != wisLen) {
                            throw new Exception("添加报警信息失败");
                        }
                        wis.clear();
                    }

                }

                if (lastDataTime == null) {
                    continue;
                }
                long lastDataTimeNum = lastDataTime.getTime();
                for (int j = 0; j < len; j++) {
                    item1 = deviceSensors.get(j);
                    Integer id = item1.getId();
                    Date lastTime = item1.getLastRecordTime();
                    String preC = item1.getPreC();
                    double preCD = Double.parseDouble(preC);
                    long lastTimeNum = lastTime == null ? 0 : lastTime.getTime();
                    if (lastDataTimeNum == lastTimeNum) {
                        continue;
                    }
                    String pass = item1.getFacPass();
                    int passData = Integer.parseInt(lastData.getString(pass));
                    double itemData;
                    if (passData == 32767) {
                        //异常数据 跳过
                        continue;
                    } else {
                        //数值乘以分辨率,保留6位小数
                        itemData = passData * preCD;
                    }
                    BigDecimal recordData = MyObjectUtil.getSixDecimal(itemData);

                    //根据最后设备通道数据判断
                    List<WarningThreshold> wtList = item1.getWtList();
                    int wtLen = MyObjectUtil.iterableCount(wtList);
                    if (wtLen > 0) {
                        for (int i = 0; i < wtLen; i++) {
                            WarningThreshold wt = wtList.get(i);

                            BigDecimal min = wt.getMinValue();
                            BigDecimal max = wt.getMaxValue();
                            //判断是否发送短信
                            if (!Integer.valueOf(1).equals(wt.getIsSend())||(max == null && min == null)) {
                                continue;
                            }
                            //无最大值或小于等于最大值
                            boolean maxF = max == null || recordData.compareTo(max) < 1;
                            //无最小值或大于等于最小值
                            boolean minF = min == null || recordData.compareTo(min) > -1;
                            if (maxF && minF  ) {
                                String warningInfo = wt.getWarningInfo();
                                Integer eWlMId = wt.getEnterpriseWLManagerId();//接收短信的企业物联网管理人员
                                Integer wtId = wt.getId();
                                Integer gId = item1.getGroupId();
                                //Set集合内不允许有重复的数据元素,防止重复
                                gIds.add(gId);
                                eWlMIds.add(eWlMId);
                                wtIds.add(wtId);

                                String unit = item1.getUnit();
                                String name = item1.getName();
                                JSONObject obj = new JSONObject();
                                //发短信时只保留2位小数
                                String objectData = MyObjectUtil.getTwoDecimal(itemData).toString();
                                if (unit != null) {
                                    objectData += unit;
                                }
                                obj.put("gId", gId);
                                obj.put("eWlMId", eWlMId);
                                obj.put("sName", name);
                                obj.put("date", lastDataTime);
                                obj.put("data", objectData);
                                obj.put("info", warningInfo);
                                obj.put("wtId", wtId);
                                sendArr.add(obj);
                                break;
                            }
                        }
                    }


                    //修改最后记录时间
                    dsList.add(new DeviceSensor(id, lastDataTime, recordData));

                }


            }

        }


        //批量修改传感器最后记录时间和最后记录数据(传感器数量不会过多无需限制集合中个数)
        int updIndex = deviceSensorBiz.batchUpdateSelectiveByList(dsList);
        if (updIndex != dsList.size()) {
            throw new Exception("同步传感器最后记录时间失败");
        }


        List<JSONObject> value;
        DeviceGroup dg;
        EnterpriseWLManager em;
        //发送短信(只判断最近的数据无需限制集合中个数)
        int len = sendArr.size();
        if (len > 0) {
            //Set转化List
            List<Integer> gIdList = new ArrayList<>(gIds);
            List<Integer> eWLMIdList = new ArrayList<>(eWlMIds);
            List<Integer> wtIdList = new ArrayList<>(wtIds);
            //获得需要报警的所有分组、企业物联网管理员、阈值
            List<DeviceGroup> dgList = deviceGroupBiz.batchSelectByIds(gIdList);
            int dgLen = MyObjectUtil.iterableCount(dgList);
            List<EnterpriseWLManager> eWLMList = wlManagerBiz.batchSelectByIds(eWLMIdList);
            int eWLMLen = MyObjectUtil.iterableCount(eWLMList);
            List<WarningThreshold> wtList = warningThresholdBiz.batchSelectByIds(wtIdList);
            int wtLen = MyObjectUtil.iterableCount(wtList);
            if (wtLen > 0) {
                //按照物联网阈值wtId分组
                Map<Integer, List<JSONObject>> map = new HashMap<>();
                Integer wtIdKey;
                List<JSONObject> itemList;
                for (int i = 0; i < len; i++) {
                    JSONObject item = sendArr.getJSONObject(i);
                    wtIdKey = item.getInteger("wtId");
                    if (wtIdKey == null) {
                        continue;
                    }
                    if (map.containsKey(wtIdKey)) {
                        itemList = map.get(wtIdKey);
                        itemList.add(item);
                    } else {
                        itemList = new ArrayList<>();
                        itemList.add(item);
                        map.put(wtIdKey, itemList);
                    }
                }
                JSONArray array = new JSONArray();//确定发送短信数组
                //修改阈值集合
                List<WarningThreshold> updWTList = new ArrayList<>();
                for (Map.Entry<Integer, List<JSONObject>> entry : map.entrySet()) {
                    Integer key = entry.getKey();
                    value = entry.getValue();
                    int size = value.size();
                    if (size == 0) {
                        continue;
                    }

                    Integer sendInterval = null;
                    long next = 0;//最后发短信阈值时间戳
                    Date lastRecordTime = null;
                    for (int j = 0; j < wtLen; j++) {
                        WarningThreshold wt = wtList.get(j);
                        if (key.equals(wt.getId())) {
                            sendInterval = wt.getSendInterval();
                            lastRecordTime = wt.getLastRecordTime();
                            break;
                        }
                    }
                    if (sendInterval == null) {
                        continue;
                    }

                    Date updDate = null;
                    if (lastRecordTime != null) {
                        next = MyObjectUtil.handleDateMinute(lastRecordTime, sendInterval).getTime();
                    }

                    //时间正序
                    Collections.sort(value, (o1, o2) -> (int) (o1.getDate("date").getTime() - o2.getDate("date").getTime()));
                    for (int i = 0; i < size; i++) {
                        JSONObject item = value.get(i);
                        Integer gId = item.getInteger("gId");
                        Integer eWlMId = item.getInteger("eWlMId");
                        String objectName = item.getString("sName");//传感器名称
                        //时间
                        Date date = item.getDate("date");
                        long dateL = date == null ? 0 : date.getTime();
                        //只添加大于时间间隔的数据
                        if (dateL <= next) {
                            continue;
                        }
                        updDate = date;
                        next = MyObjectUtil.handleDateMinute(date, sendInterval).getTime();

                        if (gId != null && dgLen > 0) {
                            for (int j = 0; j < dgLen; j++) {
                                dg = dgList.get(j);
                                if (gId.equals(dg.getId())) {
                                    objectName = dg.getName() + objectName;
                                    break;
                                }
                            }
                        }
                        item.put("name", objectName);
                        String phone = null;//接受方手机号
                        if (eWlMId != null && eWLMLen > 0) {
                            for (int j = 0; j < eWLMLen; j++) {
                                em = eWLMList.get(j);
                                if (eWlMId.equals(em.getId())) {
                                    phone = em.getPhone();
                                    break;
                                }
                            }
                        }
                        item.put("phone", phone);
                        array.add(item);
                    }

                    if (updDate != null) {
                        updWTList.add(new WarningThreshold(key, updDate));
                    }

                }
                //   array.forEach(e->System.out.println("短信发送暂时用打印替代："+JSONObject.toJSONString(e)));
                //    System.out.println(array.size());
                //发送短信
                array.forEach(e -> sendWarningSms((JSONObject) e));

                //修改阈值(传感器阈值数量不会过多无需限制集合中个数)
                int index = warningThresholdBiz.batchUpdateSelectiveByList(updWTList);
                if (index != updWTList.size()) {
                    throw new Exception("报警阈值修改失败");
                }
            }
        }
        //  System.out.println(sendArr.size());
    }


    /**
     * 将分页查到的数据集合与传感器信息结合，返回新的分页集合
     */
    public List<JSONObject> getJsonByList(List<DeviceSensorData> list) {
        List<JSONObject> objects = new ArrayList<>();
        int len = MyObjectUtil.iterableCount(list);
        if (len == 0) {
            return objects;
        }
        //获得所有传感器id
        List<Integer> sIds = list.stream()
                .map(DeviceSensorData::getSensorId).distinct()
                .filter(Objects::nonNull).collect(Collectors.toList());

        List<DeviceSensor> dsList = deviceSensorBiz.getByIds(sIds);
        int size = MyObjectUtil.iterableCount(dsList);
        for (int i = 0; i < len; i++) {
            DeviceSensorData dsd = list.get(i);
            Integer sId = dsd.getSensorId();
            //对象转换
            JSONObject obj = MyObjectUtil.objectToJsonObject(dsd);
            if (size > 0) {
                for (int j = 0; j < size; j++) {
                    DeviceSensor item = dsList.get(j);
                    Integer id = item.getId();
                    if (id.equals(sId)) {
                        obj.put("dsName", item.getName());
                        obj.put("dsUnit", item.getUnit());
                    }
                }
            }
            objects.add(obj);
        }
        return objects;
    }

    /**
     * 获得某一分组下传感器、传感器数据(记录时间倒序)
     */
    public JSONObject getByGId(Integer groupId, Integer interval, Date recordTimeS, Date recordTimeE) {
        List<DeviceSensor> dsList = deviceSensorBiz.getByGId(groupId);
        int size = MyObjectUtil.iterableCount(dsList);
        if (size == 0) {
            return null;
        }
        String orderBy = "record_time desc";
        for (int i = 0; i < size; i++) {
            DeviceSensor item = dsList.get(i);
            Integer id = item.getId();

            Date start = recordTimeS;
            //如何时间间隔存在，开始时间不存在则将最小记录时间赋值给开始时间
            if (interval != null && start == null) {
                start = getMinTimeBySId(id);
            }
            //获得每个传感器的间隔数据
            List<DeviceSensorData> list = pageBySId(id, start, recordTimeE, interval,
                    null, null, orderBy);
            item.setDsdList(list);
        }
        JSONObject obj = new JSONObject(true);
        obj.put("dsList", dsList);
        return obj;
    }

}
