package com.github.wxiaoqi.security.consultation.biz;

import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.consultation.entity.DtExpertduty;
import com.github.wxiaoqi.security.consultation.mapper.DtExpertdutyMapper;
import com.mysql.cj.Session;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.*;
import java.util.function.BiConsumer;


@Service
@Transactional(rollbackFor = Exception.class)
public class DtExpertdutyBiz extends BaseBiz<DtExpertdutyMapper, DtExpertduty> {

    public class ExpertdutyInDay {
        @Setter
        @Getter
        int day;
        @Setter
        @Getter
        String week;
        @Setter
        @Getter
        List<DtExpertduty> expertduties;

    }

    /**
     * 保存排班信息  cj  根据传入的排班信息list表，更新或新增排班信息。
     */
    public ObjectRestResponse<List<DtExpertduty>> saveByYearMonth(int year, int month, int day, List<DtExpertduty> dutyList) {
        //根据传入的日期查询当日排班信息
        ObjectRestResponse<List<ExpertdutyInDay>> expertDutyInDayList = selectByYearMonth(year, month, day);
        List<DtExpertduty> searchDutyList = expertDutyInDayList.getData().get(0).expertduties;
        ArrayList<DtExpertduty> delDutyList = new ArrayList<>();
        //找出需要已存在的排班信息
        ArrayList<DtExpertduty> existDutyList = new ArrayList<>();
        //找出需要删除的排班信息
        ArrayList<Integer> DutyExpertIdList = new ArrayList<>();
        for (DtExpertduty dtExpertduty : dutyList) {
            //待提交的专家id list
            DutyExpertIdList.add(dtExpertduty.getExpertid());
        }
        ArrayList<Integer> searchDutyExpertIdList = new ArrayList<>();
        for (DtExpertduty dtExpertduty : searchDutyList) {
            //查询到的专家id list
            searchDutyExpertIdList.add(dtExpertduty.getExpertid());
        }
        for (DtExpertduty dtExpertduty : searchDutyList) {
            Integer expertId = dtExpertduty.getExpertid();
            //如果待提交的专家id list包含查询到的专家信息
            if (DutyExpertIdList.contains(expertId)) {
                existDutyList.add(dtExpertduty);
            } else {
                delDutyList.add(dtExpertduty);
            }
        }
        ArrayList<DtExpertduty> listResult = new ArrayList<>();
        int index = 0;
        for (DtExpertduty duty : dutyList) {
            //如果查询到的id list包含专家id,什么都不做
            if (searchDutyExpertIdList.contains(duty.getExpertid())) {
                continue;
            } else {
                if (index < delDutyList.size()) {
                    DtExpertduty delDuty = delDutyList.get(index);
                    delDuty.setExpertid(duty.getExpertid());
                    delDuty.setExpertname(duty.getExpertname());
                    updateSelectiveById(delDuty);
                    index++;
                } else {
                    DtExpertduty dtExpertduty = new DtExpertduty();
                    dtExpertduty.setId(null);
                    dtExpertduty.setDate(duty.getDate());
                    dtExpertduty.setExpertid(duty.getExpertid());
                    dtExpertduty.setExpertname(duty.getExpertname());
                    insertSelective(dtExpertduty);
                }
            }
        }
        if (index < delDutyList.size()) {
            for (int i = index; i < delDutyList.size(); i++) {
                delete(delDutyList.get(i));
            }
        }
        ObjectRestResponse<List<DtExpertduty>> result = new ObjectRestResponse<>();
        result.setData(listResult);
        result.setRel(true);
        return result;
    }

    public ObjectRestResponse<List<ExpertdutyInDay>> selectByYearMonth(int year, int month, int day) {
        Example example = new Example(DtExpertduty.class);
        Example.Criteria criteria = example.createCriteria();
        Calendar c = Calendar.getInstance();
        if (day == -1) {
            c.set(year, month - 1, 1, 0, 0, 0);
        } else {
            c.set(year, month - 1, day, 0, 0, 0);
        }
        c.set(Calendar.MILLISECOND, 0);
        int dayOfMonth = c.getActualMaximum(Calendar.DATE);
        Date st = c.getTime();
        if (day == -1) {
            c.set(year, month - 1, dayOfMonth, 23, 59, 59);
        } else {
            c.set(year, month - 1, day, 23, 59, 59);
        }
        c.set(Calendar.MILLISECOND, 999);
        Date ed = c.getTime();
        ArrayList<ExpertdutyInDay> listResult = new ArrayList<ExpertdutyInDay>();
        if (day == -1) {
            for (int i = 0; i < dayOfMonth; i++) {
                ExpertdutyInDay edid = new ExpertdutyInDay();
                //当前多少号
                int currentDay = i + 1;
                edid.setDay(currentDay);
                String dateTime = year + "-" + month + "-" + currentDay;
                String week = dateToWeek(dateTime);
                edid.setWeek(week);
                edid.setExpertduties(new ArrayList<DtExpertduty>());
                listResult.add(edid);
            }
        } else {
            ExpertdutyInDay edid = new ExpertdutyInDay();
            edid.setDay(day);
            edid.setExpertduties(new ArrayList<DtExpertduty>());
            listResult.add(edid);
        }
        criteria.andBetween("date", st, ed);
        List<DtExpertduty> dutyList = super.selectByExample(example);
        for (DtExpertduty duty : dutyList) {
            if (day == -1) {
                c.setTime(duty.getDate());
                listResult.get(c.get(Calendar.DATE) - 1).getExpertduties().add(duty);
            } else {
                listResult.get(0).getExpertduties().add(duty);
            }
        }
        ObjectRestResponse<List<ExpertdutyInDay>> result = new ObjectRestResponse<>();
        result.setData(listResult);
        return result;
    }

    /**
     * 获取当前日期的星期
     * cj
     * @param datetime
     * @return
     */
    public String dateToWeek(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        Date date;
        try {
            date = f.parse(datetime);
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //一周的第几天
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }

    class DataConsumer implements BiConsumer<String, List<DtExpertduty>> {
        @Autowired
        DtExpertdutyBiz mapper;
        Map<Integer, List<DtExpertduty>> oldData = null;

        public DataConsumer(Map<Integer, List<DtExpertduty>> oldData) {
            this.oldData = oldData;
        }

        @Override
        public void accept(String key, List<DtExpertduty> value) {
            List<DtExpertduty> oldValue = oldData.get(Integer.parseInt(key));
            for (DtExpertduty val : value) {
                if (contains(oldValue, val)) {
                    mapper.updateById(val);
                } else {
                    mapper.insert(val);
                }
            }
            for (DtExpertduty val : oldValue) {
                mapper.deleteById(val);
            }
        }

        public boolean contains(List<DtExpertduty> oldValue, DtExpertduty val) {
            boolean result = false;
            for (DtExpertduty oldVal : oldValue) {
                if (oldVal.getId() == val.getId()) {
                    result = true;
                    break;
                }
            }
            return result;
        }
    }

    ;

    public void saveByYearMonth(int year, int month, Map<String, List<DtExpertduty>> newData) {
//        Map<Integer, List<DtExpertduty>> oldData = selectByYearMonth(year, month, -1);
//        BiConsumer<String, List<DtExpertduty>> biConsumer = new DataConsumer(oldData);
//        newData.forEach(biConsumer);
    }
}
