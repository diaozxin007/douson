package com.xilidou;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonArray implements Iterable<Object>{

    private final List<Object> array;

    public JsonArray() {
        this.array = new ArrayList<Object>();
    }

    public void add(Object o){
        array.add(o);
    }

    public Object get(int i) {
        return array.get(i);
    }

    public Iterator<Object> iterator() {
        return array.iterator();
    }
}
