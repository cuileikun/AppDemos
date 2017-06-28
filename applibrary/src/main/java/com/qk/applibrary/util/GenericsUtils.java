package com.qk.applibrary.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by acer on 2016-9-21.
 * 泛型工具
 */
public class GenericsUtils {
    /**
     * 获取泛型参数类型
     * @param object 此class
     * @param i
     * @param <T>
     * @return
     */
    public static <T> T getParameterizedType(Object object,int i) {
        try {
            Type type = object.getClass().getGenericSuperclass(); //返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type
            return ((Class<T>) ((ParameterizedType)type).getActualTypeArguments()[i])
                    .newInstance();//返回表示此类型实际类型参数的 Type 对象的数组
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }
}
