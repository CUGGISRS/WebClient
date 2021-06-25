package com.github.wxiaoqi.security.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

public class MyObjectUtil {
    private static Calendar calendar = new GregorianCalendar();

    /**
     * 校验对象(除集合中的属性外的)属性是否都为null
     */
    public static   boolean isAllFieldNullExceptArray(Object obj, String[] filedNameArr) throws Exception{
        boolean flag = true;
        if(obj!=null){
            Class stuCla = obj.getClass();// 得到类对象
            Field[] fs = stuCla.getDeclaredFields();//得到属性集合
            for (Field f : fs) {//遍历属性
                List<String> filedNames= Arrays.asList(filedNameArr);
                if(filedNames.contains(f.getName())){
                    continue;
                }
                f.setAccessible(true); // 设置属性是可以访问的(私有的也可以)
                Object val = f.get(obj);// 得到此属性的值
                if(val!=null) {//只要有1个属性不为空,那么就不是所有的属性值都为空
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }


    /**
     * 判断对象是否不为null或空，是则返回true
     * @param obj
     * @return
     */
    public static boolean noNullOrEmpty(Object obj){
        return obj != null && obj.toString().length() > 0;
    }

    /**
     * 获取集合中元素个数，为null则返回0
     * @param i
     * @return
     */
    public static int iterableCount(Iterable i) {
        int count = 0;
        if(i!=null){
            if(i instanceof List){
                return ((List) i).size();
            }
            if(i instanceof  Set){
                return ((Set) i).size();
            }
            Iterator it = i.iterator();
            while (it.hasNext()) {
                it.next();
                count++;
            }
        }
        return count;
    }


    /**
     * 集合相加求和
     * @param list
     * @return
     */
    public static BigDecimal sumBigDecimalList(List<BigDecimal> list){
        BigDecimal sum=BigDecimal.ZERO;
        if(list!=null){
            for (BigDecimal i:list){
                if(i!=null){
                    sum=sum.add(i);
                }

            }
        }
        return sum;
    }


    /**
     * 获得当前时间加减年后的时间
     * @param year
     * @return
     */
    public static Date getByNowHandleDateYear(int year){
        Date now=new Date();
        return handleDateYear(now,-year);
    }

    /**
     * 根据出生日期计算年龄
     * @param birthDay
     * @return
     * @throws Exception
     */
    public static Integer getAge(Date birthDay){
        if(birthDay==null){
            return null;
        }
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)){
            throw new IllegalArgumentException("出生日期大于当前时间!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;
        if (monthNow <= monthBirth){
            if (monthNow == monthBirth){
                if (dayOfMonthNow < dayOfMonthBirth){
                    age--;
                }
            }else {
                age--;
            }
        }
        return age;
    }



    /**
     * 原时间加减   年
     * @param oldData
     * @param year
     * @return
     */
    public  static Date handleDateYear(Date oldData,int year){
        calendar.setTime(oldData);
        calendar.add(Calendar.YEAR,year);
        return calendar.getTime();
    }

    /**
     * 原时间加减  月
     * @param oldData
     * @param month
     * @return
     */
    public  static Date handleDateMonth(Date oldData,int month){
        calendar.setTime(oldData);
        calendar.add(Calendar.MONTH,month);
        return calendar.getTime();
    }

    /**
     * 原时间加减    日
     * @param oldData
     * @param day
     * @return
     */
    public  static Date handleDateDay(Date oldData,int day){
        calendar.setTime(oldData);
        calendar.add(Calendar.DATE,day);
        return calendar.getTime();
    }

    /**
     * 原时间加减    时
     * @param oldData
     * @param hour
     * @return
     */
    public  static Date handleDateHour(Date oldData,int hour){
        calendar.setTime(oldData);
        calendar.add(Calendar.HOUR,hour);
        return calendar.getTime();
    }

    /**
     * 原时间加减    分
     * @param oldData
     * @param minute
     * @return
     */
    public  static Date handleDateMinute(Date oldData,int minute){
        calendar.setTime(oldData);
        calendar.add(Calendar.MINUTE,minute);
        return calendar.getTime();
    }

    /**
     * 原时间加减    秒
     * @param oldData
     * @param second
     * @return
     */
    public  static Date handleDateSecond(Date oldData,int second){
        calendar.setTime(oldData);
        calendar.add(Calendar.SECOND,second);
        return calendar.getTime();
    }

    /**
     * 通过一年的起始时间获得结束时间
     * @param oldData
     * @return
     */
    public static  Date getOneYearEndDateByStartDate(Date oldData){
        calendar.setTime(oldData);
        calendar.add(Calendar.YEAR,1);
        calendar.add(Calendar.SECOND,-1);
        return calendar.getTime();
    }

    /**
     * 通过一个月的起始时间获得结束时间
     * @param oldData
     * @return
     */
    public static  Date getOneMonthEndDateByStartDate(Date oldData){
        calendar.setTime(oldData);
        calendar.add(Calendar.MONTH,1);
        calendar.add(Calendar.SECOND,-1);
        return calendar.getTime();
    }

    /**
     * 获得3位小数来返回
     */
    public static BigDecimal getThreeDecimal(double num){
        return getPatternDecimal(num,"0.000");
    }
    /**
     * 获得6位小数来返回
     */
    public static BigDecimal getSixDecimal(double num){
        return getPatternDecimal(num,"0.000000");
    }

    /**
     * 获得2位小数来返回
     */
    public static BigDecimal getTwoDecimal(double num){
        return getPatternDecimal(num,"0.00");
    }

    /**
     * 获得1位小数来返回
     */
    public static BigDecimal getOneDecimal(double num){
        return getPatternDecimal(num,"0.0");
    }
    /**
     * 返回某一格式的小数，如:"0.000"
     */
    public static BigDecimal getPatternDecimal(double num,String pattern){
        DecimalFormat df = new DecimalFormat(pattern);
        String numStr=df.format(num);
        return new BigDecimal(numStr);
    }

    /**
     * 获得存在id属性的对象集合的id集合
     */
    public static  <T>  List<Integer> getIdsByList(List<T> list) {
        List<Integer> ids=null;
        if(list!=null){
            for (int i=0,len=list.size();i<len;i++){
                try {
                    T obj=list.get(i);
                    Method m = obj.getClass().getMethod("getId");
                    Object v=  m.invoke(obj);
                    if(v instanceof Integer){
                        Integer id= (Integer) v;
                        if(ids==null){
                            ids=new ArrayList<>();
                        }
                        ids.add(id);
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

            }
        }
        return ids;
    }


    /**
     * JSONArray按某一字符串排序
     */
    public static JSONArray sortByDateStr(JSONArray array,String sortKey){
        array.sort(Comparator.comparing(a -> ((JSONObject) a).getString(sortKey)));

        // 方式二：单字段排序
//        array.sort((a, b) -> ((JSONObject) a).getString("id").compareTo(((JSONObject) b).getString("id")));
        // 方式二：多字段排序
      /*  array.sort((a, b) -> {
            int i = ((JSONObject) a).getString(dateStrKey).compareTo(((JSONObject) b).getString(dateStrKey));
            if (i == 0) {
                int j = ((JSONObject) a).getInteger("score").compareTo(((JSONObject) b).getInteger("score"));
                return j;
            }
            return i;
        });*/
        return array;
    }

    /**
     * 对象集合(包括为null的属性)转化为json数组
     * @param list
     * @return
     */
    public  static <T> JSONArray listToJsonArray(List<T> list){
        //包括值为null的属性 ，2不设置则默认舍去为null的属性 1不设置则时间格式变为时间戳
        return JSONArray.parseArray(JSON.toJSONStringWithDateFormat(
                list,
                "yyyy-MM-dd HH:mm:ss",
                SerializerFeature.WriteDateUseDateFormat,SerializerFeature.WriteMapNullValue));
    }

    /**
     * 对象(包括为null的属性)转化为json对象
     * @param t
     * @return
     */
    public  static <T> JSONObject objectToJsonObject(T t){
        //包括值为null的属性 ，2不设置则默认舍去为null的属性 1不设置则时间格式变为时间戳
        return JSONObject.parseObject( JSON.toJSONStringWithDateFormat(
                t,
                "yyyy-MM-dd HH:mm:ss",
                SerializerFeature.WriteDateUseDateFormat,SerializerFeature.WriteMapNullValue)
        );
    }

}
