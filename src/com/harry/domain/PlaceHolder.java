package com.harry.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class PlaceHolder {
    private static HashMap<String, String> dict;
    private static int customIndex = 1;

    static {
        dict = new HashMap<>();
//        dict.put("1", "?l?d");
        dict.put("Numeric", "?d");
        dict.put("Uppercase", "?u");
        dict.put("Lowercase", "?l");
        dict.put("Special", "?s");
        dict.put("AllCharacter", "?a");
    }

    public static HashMap<String, String> getDict() {
        return dict;
    }

    public static int getCustomIndex() {
        return customIndex;
    }

    public String getPlaceHolder(ArrayList<String> key){
        //将集合排序
        key.sort(Comparator.naturalOrder());
        //单key直接返回key对应得placeholder
        if(key.size() == 1){
            return dict.get(key.get(0));
        }
        //如果是空列表，说明用户没有指定密码结构，返回?a
        if(key.isEmpty()){
            return "?a";
        }
        //key等于四时，说明每种占位符都选择了，返回?a
        if(key.size() == 4){
            return "?a";
        }
        //如果传入的key中的元素与现有的自定义占位符相同，则返回该占位符
        //将刚刚添加进dict中的自定义占位符返回
        //更新：要返回?1而不是-1，因此需要做些修改
        //将以上三点封装成了一个方法
        return checkKeyInDict(key);
    }

    //判断当前用户输入的key是否包含在dict中
    private String checkKeyInDict(ArrayList<String> key){
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : key) {
            stringBuilder.append(dict.get(s));
        }
        for (String s : dict.keySet()) {
            //如果dict中有值与stringBuilder一样的，返回键
            if(dict.get(s).equals(stringBuilder.toString())){
                return "?"+s;
            }
        }
        return "?"+addCustomPlaceHolder(key);
    }

    //添加用户自定义的占位符并添加进dict中
    private String addCustomPlaceHolder(ArrayList<String> key){
        if (customIndex <= 4){
            StringBuilder customPlaceHolder = new StringBuilder();
            for(String s : key){
                customPlaceHolder.append(dict.get(s));
            }
            String customKey = String.valueOf(customIndex++);//自定义索引减一，如果小于-4则需要提醒报错
            dict.put(customKey, customPlaceHolder.toString());
            return customKey;
        }else {
            System.out.println("The custom placeholder has reached its limit.");
            return "a";
        }
    }
}
