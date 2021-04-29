package pers.lomesome.compliation.utils.grammatical;

import java.util.*;

public class Predict {
    AllGrammer allGrammer;
    Map<String, Set<String>> firstSet;
    Map<String, Set<String>> followSet;

    public Predict(AllGrammer allGrammer, Map<String, Set<String>> firstSet, Map<String, Set<String>> followSet){
        this.allGrammer = allGrammer;
        this.firstSet = firstSet;
        this.followSet = followSet;
    }

    public LinkedHashMap predictTable(){
        LinkedHashMap<String, LinkedHashMap<String, List<List<String>>>> linkedHashMap = new LinkedHashMap<>();
        Set<String> allVn = new LinkedHashSet<>();
        Set<String> allVt = new LinkedHashSet<>();
        allGrammer.getGrammarMap().forEach((k, v)->{
            allVn.add(k);
        });
        allGrammer.getGrammarMap().forEach((k, v)->{
            for (List<String> stringList:v){
                allVt.addAll(stringList);
            }
        });
        allVt.removeAll(allVn);
        allVt.remove("ε");
        allVt.add("#");
        LinkedHashMap<String, List<List<String>>> stringLinkedHashMap = null;
        for (String vn: allVn){
            if (linkedHashMap.get(vn) == null){
                stringLinkedHashMap = new LinkedHashMap<>();
            }else {
                stringLinkedHashMap = linkedHashMap.get(vn);
            }
            for (String vt : allVt){
                stringLinkedHashMap.put(vt, new LinkedList<>());
            }
            linkedHashMap.put(vn, stringLinkedHashMap);
        }
//        allGrammer.getGrammarMap().forEach((k, v)->{
//            for (String s : allVt){
//                if (firstSet.get(k).contains(s)){
//                    linkedHashMap.get(k).get(s).addAll(v);
//                }
//                else if (firstSet.get(k).contains("ε")){
//                    for (String s1:followSet.get(k)){
//                        linkedHashMap.get(k).get(s1).addAll(v);
//                    }
//                }
//                else {
//                    List<String> set = new LinkedList<>();
//                    set.add("error");
//                    linkedHashMap.get(k).get(s).add(set);
//                }
//            }
//        });

        for (String vn : allVn){
            for (String s : allVt){
                if (firstSet.get(vn).contains(s)){
                    List<List<String>> r = allGrammer.copyGrammarMap().get(vn);
                    r.remove(Collections.singletonList("ε"));
                    linkedHashMap.get(vn).get(s).addAll(r);
                }
                else if (firstSet.get(vn).contains("ε")) {
                    for (String s1 : followSet.get(vn)) {
                        if (linkedHashMap.get(vn).get(s1).size() == 0) {
                            List<String> list = new LinkedList<>();
                            list.add("ε");
                            linkedHashMap.get(vn).get(s1).add(list);
                        }
                    }
                    if (linkedHashMap.get(vn).get(s).size() == 0){
                        List<String> list = new LinkedList<>();
                        list.add("error");
                        linkedHashMap.get(vn).get(s).add(list);
                    }
                }
                else {
                    List<String> list = new LinkedList<>();
                    list.add("error");
                    linkedHashMap.get(vn).get(s).add(list);
                }
            }
        }
        return linkedHashMap;
    }
}