package pers.lomesome.compliation.utils.semantic;

import pers.lomesome.compliation.utils.syntax.AllGrammer;
import java.util.*;

public class NewPredict {
    AllGrammer allGrammer;
    Map<List<List<String>>, Set<String>> firstSet;
    Map<String, Set<String>> followSet;

    public NewPredict(AllGrammer allGrammer, Map<List<List<String>>, Set<String>> firstSet, Map<String, Set<String>> followSet) {
        this.allGrammer = allGrammer;
        this.firstSet = firstSet;
        this.followSet = followSet;
    }

    public LinkedHashMap<String, LinkedHashMap<String, List<String>>> predictTable() {
        LinkedHashMap<String, LinkedHashMap<String, List<String>>> linkedHashMap = new LinkedHashMap<>();
        Set<String> allVn = new LinkedHashSet<>();
        Set<String> allVt = new LinkedHashSet<>();
        allGrammer.getGrammarMap().forEach((k, v) -> allVn.add(k));
        allGrammer.getGrammarMap().forEach((k, v) -> {
            for (List<String> stringList : v) {
                allVt.addAll(stringList);
            }
        });
        allVt.removeAll(allVn);
        allVt.remove("ε");
        allVt.add("#");
        LinkedHashMap<String, List<String>> stringLinkedHashMap;
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
            for (String s : v){
                if (!s.equals("ε")) {
                    for (String s1  : k.get(1)){
                        linkedHashMap.get(k.get(0).get(0)).get(s).add(s1);
                    }

                }else {
                    for (String s1 : followSet.get(k.get(0).get(0))) {
                        if (linkedHashMap.get(k.get(0).get(0)).get(s1).size() == 0) {
                            linkedHashMap.get(k.get(0).get(0)).get(s1).add("ε");
                        }
                    }
                }

            }
        });

        firstSet.forEach((k, v) -> {
            for (String s : v){
                if (!s.equals("ε")) {
                    for (String s1 : followSet.get(k.get(0).get(0))) {
                        if (linkedHashMap.get(k.get(0).get(0)).get(s1).size() == 0) {
                            linkedHashMap.get(k.get(0).get(0)).get(s1).add("synch");
                        }
                    }
                }
            }
        });

        return linkedHashMap;
    }
}
