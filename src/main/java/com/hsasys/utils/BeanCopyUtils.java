package com.hsasys.utils;


import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils
{
    private BeanCopyUtils() {}

    public static <T> T copyBean(Object source,Class<T> clazz)
    {
        T result = null;
        try
        {
            result = clazz.newInstance();
            BeanUtils.copyProperties( source, result );
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return result;
    }

    public static <T> List<T> copyBeanList(List<?> list, Class<T> clazz)
    {
        return list.stream()
                .map( source -> copyBean( source, clazz ) )
                .collect( Collectors.toList() );
    }


}
