package com.xyw.spbodemo.model;

import java.util.HashMap;
import java.util.Map;

// to convenient to show data
public class ViewObject {

    private Map<String,Object> objs = new HashMap<String, Object>();
    public void set(String key,Object value){
        objs.put(key,value);
    }

    public Object get(String key){
        return objs.get(key);
    }



}

 class Test{
     public static void main(String[] args) {
         ViewObject vo = new ViewObject();
         vo.set("news",new News());
         vo.get("news");
     }
 }