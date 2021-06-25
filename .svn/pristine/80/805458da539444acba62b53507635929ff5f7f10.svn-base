package com.zd.earth.manage.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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
     * 获得对象集合大小，为null则返回0
     * @param list
     * @param <T>
     * @return
     */
    public static  <T>  int noEmpty(List<T> list){
        if(list!=null){
            return list.size();
        }
        return 0;
    }

    /**
     * 获取Iterable中元素个数
     * @param i
     * @return
     */
    public static int iterableCount(Iterable i) {
        int count = 0;
        Iterator it = i.iterator();
        while (it.hasNext()) {
            it.next();
            count++;
        }
        return count;
    }

    /**
     * 判断对象是否不为null或空，是则返回true
     */
    public static boolean noNullOrEmpty(Object obj){
        return obj != null && obj.toString().length() > 0;
    }

    /**
     * 判断对象是否为null或空，是则返回true
     */
    public static boolean isNullOrEmpty(Object obj){
        return obj == null || obj.toString().length() == 0;
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
     * 获得年份
     * @param date
     * @return
     */
    public static int getYear(Date date){
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
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
     * json数组按对象中某一键值的大小排序，倒序
     */
    public static JSONArray reverseJSONArray(JSONArray arr, String key){
        if(arr==null||StringHelper.isNullOrEmpty(key)){
            return arr;
        }
        arr.sort(Comparator.comparing(obj -> ((JSONObject) obj).getString(key)).reversed());
        return arr;
    }

    public static  String obj2JsonStr(Object obj){
        return JSONObject.toJSONString(obj);
    }




    /**
     * 按四舍五入方式保留小数
     */
    public static BigDecimal retainBigDecimal(BigDecimal old,int digit){
        if(old!=null){
            return old.setScale(digit,BigDecimal.ROUND_HALF_UP);
        }
        return null;
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
            return ids;
        }
        return null;
    }
}
