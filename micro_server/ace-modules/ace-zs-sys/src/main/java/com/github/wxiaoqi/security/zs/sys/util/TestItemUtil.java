package com.github.wxiaoqi.security.zs.sys.util;

import com.github.wxiaoqi.security.zs.sys.entity.BetweenTestItem;
import com.github.wxiaoqi.security.zs.sys.entity.ItemInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

public class TestItemUtil {
    /**
     * 通过检验_项目集合获得项目id集合
     */
    public static List<Integer> getItemIdsByItems(List<BetweenTestItem> list) {
        if (list != null) {
            List<Integer> itemIds = list.stream().map(BetweenTestItem::getItemId).distinct().collect(Collectors.toList());
            if (itemIds != null && itemIds.size() > 0) {
                return itemIds;
            }
        }
        return null;
    }

    /**
     * 给相关表设置检验_项目、项目
     */
    public static <T> List<T> setListTestItem(String btMN, List<BetweenTestItem> betweenTestItems,
                                              String iMN, List<ItemInfo> itemInfos,
                                              List<T> data, String testType) {
        if (data != null && betweenTestItems != null && testType != null) {
            boolean isExistItem = itemInfos != null;
            for (int i = 0, len = data.size(); i < len; i++) {
                try {
                    T obj = data.get(i);
                    Class clazz = obj.getClass();
                    Method m1 = clazz.getMethod("getId");
                    Object id0 = m1.invoke(obj);
                    if (id0 instanceof Integer) {
                        Integer id = (Integer) id0;
                        List<BetweenTestItem> btList = betweenTestItems.stream()
                                .filter(bt -> id.equals(bt.getTestId()) && testType.equals(bt.getTestType()))
                                .collect(Collectors.toList());
                        Method m2 = clazz.getMethod(btMN, List.class);
                        //设置检验_项目
                        m2.invoke(obj, btList);
                        List<Integer> iIds = getItemIdsByItems(btList);
                        if (isExistItem && iIds != null) {
                            List<ItemInfo> iList = itemInfos.stream()
                                    .filter(item -> iIds.contains(item.getId())).collect(Collectors.toList());
                            //设置项目
                            Method m3 = clazz.getMethod(iMN, List.class);
                            m3.invoke(obj, iList);
                        }
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
        return data;

    }
}
