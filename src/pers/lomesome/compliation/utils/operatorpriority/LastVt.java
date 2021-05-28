package pers.lomesome.compliation.utils.operatorpriority;

import java.util.*;

public class LastVt {
    private Map<String, Set<String>> lastVt ;
    private Map<String, List<List<String>>> produce;
    private Set<String> vtSet = new LinkedHashSet<>();  //终结符
    private Set<String> vnSet = new LinkedHashSet<>();  //非终结符

    public LastVt(Map<String, List<List<String>>> produce, Set<String> vtSet, Set<String> vnSet){
        this.produce = produce;
        this.vtSet = vtSet;
        this.vnSet = vnSet;
    }

    public Map<String, Set<String>> getLastVt() {
        lastVt = new HashMap<>();
        produce.forEach((k, v)->{
            Set<String> fvt = new HashSet<>();
            getLastVT(k, fvt);
            lastVt.put(k, fvt);
        });
        return lastVt;
    }

    private void getLastVT(String vn, Set<String> lvt) {
        List<List<String>> lists = produce.get(vn);
        for (List<String> list : lists){
            if (vtSet.contains(list.get(list.size() - 1))){
                lvt.add(list.get(list.size() - 1));
            }
            if (list.size() > 1) {
                if (vnSet.contains(list.get(list.size() - 1)) && vtSet.contains(list.get(list.size() - 2))) {
                    lvt.add(list.get(list.size() - 2));
                }
            }
            if (vnSet.contains(list.get(list.size() - 1))){
                if (vn.equals(list.get(list.size() - 1))){
                    continue;
                }
                getLastVT(list.get(list.size() - 1),lvt);
            }
        }
    }

}
