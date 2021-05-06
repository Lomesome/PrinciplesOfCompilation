package pers.lomesome.compliation.utils.grammatical;


import pers.lomesome.compliation.model.MakeJson;

import java.util.*;

public class NewPredict {
    AllGrammer allGrammer;
    Map<String, Set<String>> firstSet;
    Map<String, Set<String>> followSet;

    public NewPredict(AllGrammer allGrammer, Map<String, Set<String>> firstSet, Map<String, Set<String>> followSet) {
        this.allGrammer = allGrammer;
        this.firstSet = firstSet;
        this.followSet = followSet;
    }

    public LinkedHashMap predictTable() {
        LinkedHashMap<String, LinkedHashMap<String, List<String>>> linkedHashMap = new LinkedHashMap<>();
        Set<String> allVn = new LinkedHashSet<>();
        Set<String> allVt = new LinkedHashSet<>();
        allGrammer.getGrammarMap().forEach((k, v) -> {
            allVn.add(k);
        });
        allGrammer.getGrammarMap().forEach((k, v) -> {
            for (List<String> stringList : v) {
                allVt.addAll(stringList);
            }
        });
        allVt.removeAll(allVn);
        allVt.remove("ε");
        allVt.add("#");
        LinkedHashMap<String, List<String>> stringLinkedHashMap = null;
        for (String vn : allVn) {
            if (linkedHashMap.get(vn) == null) {
                stringLinkedHashMap = new LinkedHashMap<>();
            } else {
                stringLinkedHashMap = linkedHashMap.get(vn);
            }
            for (String vt : allVt) {
                stringLinkedHashMap.put(vt, new LinkedList<>());
            }
            linkedHashMap.put(vn, stringLinkedHashMap);
        }

        firstSet.forEach((k, v) -> {
            System.out.println(k);
            for (String s : v){
                if (!s.equals("ε")) {
                    for (String s1 : k.split("->")[1].split("@#@")){
                        linkedHashMap.get(k.split("->")[0]).get(s).add(s1);
                    }

                }else {
                    for (String s1 : followSet.get(k.split("->")[0])) {
                        if (linkedHashMap.get(k.split("->")[0]).get(s1).size() == 0) {
                            linkedHashMap.get(k.split("->")[0]).get(s1).add("ε");
                        }
                    }
                }

            }
        });

        firstSet.forEach((k, v) -> {
            System.out.println(k);
            for (String s : v){
                if (!s.equals("ε")) {
                    for (String s1 : followSet.get(k.split("->")[0])) {
                        if (linkedHashMap.get(k.split("->")[0]).get(s1).size() == 0) {
                            linkedHashMap.get(k.split("->")[0]).get(s1).add("synch");
                        }
                    }
                }
            }
        });
        return linkedHashMap;
    }
}
