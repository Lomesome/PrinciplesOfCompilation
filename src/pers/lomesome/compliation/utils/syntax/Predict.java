package pers.lomesome.compliation.utils.syntax;

import pers.lomesome.compliation.model.MakeJson;
import java.util.*;

public class Predict {
    AllGrammer allGrammer;
    Map<List<List<String>>, Set<String>> firstSet;
    Map<String, Set<String>> followSet;

    public Predict(AllGrammer allGrammer, Map<List<List<String>>, Set<String>> firstSet, Map<String, Set<String>> followSet) {
        this.allGrammer = allGrammer;
        this.firstSet = firstSet;
        this.followSet = followSet;
    }

    public List predictTable() {
        List linkedHashMaps = new ArrayList<>();
        LinkedHashMap<String, LinkedHashMap<String, List<MakeJson>>> linkedHashMap = new LinkedHashMap<>();
        LinkedHashMap<String, LinkedHashMap<String, List<String>>> linkedHashMap2 = new LinkedHashMap<>();
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
        LinkedHashMap<String, List<MakeJson>> makeJsonLinkedHashMap;
        for (String vn : allVn) {
            if (linkedHashMap.get(vn) == null) {
                makeJsonLinkedHashMap = new LinkedHashMap<>();
            } else {
                makeJsonLinkedHashMap = linkedHashMap.get(vn);
            }
            for (String vt : allVt) {
                makeJsonLinkedHashMap.put(vt, new LinkedList<>());
            }
            linkedHashMap.put(vn, makeJsonLinkedHashMap);
        }

        LinkedHashMap<String, List<String>> stringLinkedHashMap;
        for (String vn : allVn) {
            if (linkedHashMap2.get(vn) == null) {
                stringLinkedHashMap = new LinkedHashMap<>();
            } else {
                stringLinkedHashMap = linkedHashMap2.get(vn);
            }
            for (String vt : allVt) {
                stringLinkedHashMap.put(vt, new LinkedList<>());
            }
            linkedHashMap2.put(vn, stringLinkedHashMap);
        }


        firstSet.forEach((k, v) -> {
            for (String s : v){
                if (!s.equals("ε")) {
                    if (linkedHashMap.get(k.get(0).get(0)).get(s).size() == 0) {
                        for (String s1 : k.get(1)) {
                            linkedHashMap.get(k.get(0).get(0)).get(s).add(new MakeJson(s1, new ArrayList<>()));
                            linkedHashMap2.get(k.get(0).get(0)).get(s).add(s1);
                        }
                    }

                }else {
                    for (String s1 : followSet.get(k.get(0).get(0))) {
                        if (linkedHashMap.get(k.get(0).get(0)).get(s1).size() == 0) {
                            linkedHashMap.get(k.get(0).get(0)).get(s1).add(new MakeJson("ε", new ArrayList<>()));
                            linkedHashMap2.get(k.get(0).get(0)).get(s1).add("ε");
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
                            linkedHashMap.get(k.get(0).get(0)).get(s1).add(new MakeJson("synch", new ArrayList<>()));
                            linkedHashMap2.get(k.get(0).get(0)).get(s1).add("synch");
                        }
                    }
                }
            }
        });

        linkedHashMaps.add(linkedHashMap);
        linkedHashMaps.add(linkedHashMap2);
        return linkedHashMaps;
    }
}
