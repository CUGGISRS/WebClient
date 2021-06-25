import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @description:
 * @author: gsy
 * @create: 2020-10-22 08:50
 **/

public class Mytest {

    public static void main(String[] args) {
        //将[]去除
        String str ="[2020-10-9]";
        String aa="aaa[你好]bbbb".replaceAll("(.+)\\[(.+?)\\](.+)", "$1"+"$2"+"$3");

        String regx = "\\[(.+?)\\]";
//      String bb=str.replaceAll("\\[(.+?)\\]", "$1");
        String bb=str.replaceAll(regx, "$1");

//       System.out.println(aa);
        System.out.println("转换后的日期"+bb);

        //转换时间测试
        DateTime parse = DateUtil.parse("2020年08月21日");
        System.out.println(parse);

        //将指定的数字替换为空" "， 将 "发布于：2020年08月21日" 转为  2020年08月21日
        String testS = "发布于：2020年08月21日";
        String replace = testS.replaceAll("[^[\\u4e00-\\u9fa5\\uff1a]{0,}$]{4}(.*)","$1");
        System.out.println(replace);

        //去调前三个数字
        String testS02 = "123nigulas";
        String $1 = testS02.replaceAll("[0-9]{3}(.*)", "$1");
        System.out.println($1);

        //比较string类型的日期
        String begTime = "2017-07-01 00:00:00";
        String endTime = "2017-07-02 00:00:00";

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            long time = dateFormat.parse(begTime).getTime();
            System.out.println(time);//1498838400000

            DateTime parse1 = DateUtil.parse(begTime);
            int millisecond = DateUtil.millisecond(parse1);//获取毫秒数
            System.out.println("时间戳"+parse1.getTime());


            DateTime parse2 = DateUtil.parse(endTime);
            long between = DateUtil.between(parse1, parse2, DateUnit.DAY);
            System.out.println(parse1);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        //集合
        //list
        Collection<String> list = new ArrayList<String>();
        List<String> list02 = new ArrayList<String>();
        //set
        Collection<String> set = new HashSet<>();
        Set<String> set02 = new HashSet<String>();

        //链表    Map
        //HashMap
        HashMap<String,String> map = new HashMap<>();

        //遍历的方式，获取每一个entry
        map.put("apple","gsy");
        map.put("pear","zy");
        //1.map转为set集合
        Set<Map.Entry<String, String>> entries = map.entrySet();
        for (Map.Entry<String, String> o:entries) {
            System.out.println(o.getKey());
            System.out.println(o.getValue());
        }

        //2.lambda表达式
        map.forEach((k,v)->{
            System.out.println(k);
        });

        //
        System.out.println(map.get("gsy"));
    }
}
