package com.github.wxiaoqi.security.com.sys.util;

import com.github.wxiaoqi.security.com.sys.entity.FileInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件工具类
 */
public class FileInfoUtil {


    /**
     * 给相关表设置文件信息
     */
    public static <T> List<T> setListField(String mN, List<FileInfo> fileInfos, List<T> data, Integer type) {
        if (fileInfos != null && data != null && type != null) {
            for (int i = 0, len = data.size(); i < len; i++) {
                try {
                    T obj = data.get(i);
                    Class clazz = obj.getClass();
                    Method m1 = clazz.getMethod("getId");
                    Object id0 = m1.invoke(obj);
                    if (id0 instanceof Integer) {
                        Integer id = (Integer) id0;
                        List<FileInfo> list = fileInfos.stream()
                                .filter(vf -> id.equals(vf.getFormId()) && type.equals(vf.getType()))
                                .collect(Collectors.toList());
                        Method m2 = clazz.getMethod(mN, List.class);
                        m2.invoke(obj, list);
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
