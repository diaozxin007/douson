package com.xilidou;

import java.util.HashMap;
import java.util.Map;

public class JsonObject {

    private final Map<String,Object> map;

    public JsonObject(){
        this.map = new HashMap<String, Object>();
    }

    public Object getVal(String key){
        return map.get(key);
    }

    public void setMap(String key,Object o){
        map.put(key,o);
    }

}
