package pers.lomesome.compliation.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Counter {
    private Map<String, Integer> map = new HashMap();

    public void addS(String s){
        map.merge(s, 1, Integer::sum);
    }

    public List<String> getRepeat(){
        List<String> list = new ArrayList<>();
        map.forEach((k, v)->{
            if (v > 1){
                list.add(k);
            }
        });
        return list;
    }
}
