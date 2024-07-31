package com.hoangphi.utils;

import java.util.List;

public class ListUtils {
    public static Boolean includes(List<String> array,String itemCondition){
        int size=array.stream().filter(itemCondition::equals).toList().size();
        return size>0;
    }
}
