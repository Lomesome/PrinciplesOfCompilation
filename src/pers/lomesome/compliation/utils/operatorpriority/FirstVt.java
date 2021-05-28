package pers.lomesome.compliation.utils.operatorpriority;

import java.util.*;

public class FirstVt {
    private Map<String, Set<String>> firstVt;
    private Map<String, List<List<String>>> produce;
    private Set<String> vtSet;  //终结符
    private Set<String> vnSet;  //非终结符

    public FirstVt(Map<String, List<List<String>>> produce, Set<String> vtSet, Set<String> vnSet){
        this.produce = produce;
        this.vtSet = vtSet;
        this.vnSet = vnSet;
    }

    public Map<String, Set<String>> getFirstVt() {
        firstVt = new HashMap<>();
        produce.forEach((k, v)->{
            Set<String> fvt = new HashSet<>();
            getFirstVT(k, fvt);
            firstVt.put(k, fvt);
        });
        return firstVt;
    }

    private void getFirstVT(String vn, Set<String> fvt) {

        List<List<String>> lists = produce.get(vn);
        for (List<String> list : lists){
            if (vtSet.contains(list.get(0))){
                fvt.add(list.get(0));
            }
            if (list.size() > 1) {
                if (vnSet.contains(list.get(0)) && vtSet.contains(list.get(1))) {
                    fvt.add(list.get(1));
                }
            }
            if (vnSet.contains(list.get(0))){
                if (vn.equals(list.get(0))){
                    continue;
                }
                getFirstVT(list.get(0),fvt);
            }
        }
    }

}
