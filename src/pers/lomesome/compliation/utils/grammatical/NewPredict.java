package pers.lomesome.compliation.utils.grammatical;

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
        LinkedHashMap<String, LinkedHashMap<String, List<MakeJson>>> linkedHashMap = new LinkedHashMap<>();
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
        LinkedHashMap<String, List<MakeJson>> stringLinkedHashMap = null;
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


        firstSet.forEach((k, v) -> {
            for (String s : v){
                if (!s.equals("ε")) {
                    for (String s1 : k.split("->")[1].replace("[", "").replace("]", "").replace(" ","").split(",")){
                        linkedHashMap.get(k.split("->")[0]).get(s).add(new MakeJson(s1, new ArrayList<>()));
                    }

//                    linkedHashMap.get(k.split("->")[0]).get(s).addAll(Arrays.asList(k.split("->")[1].replace("[", "").replace("]", "").replace(" ","").split(",")));
                }else {
                    if (k.split("->")[0].equals("declaration_list'")){
                        System.out.println("************");
                    }
                    for (String s1 : followSet.get(k.split("->")[0])) {
                        if (linkedHashMap.get(k.split("->")[0]).get(s1).size() == 0) {
                            linkedHashMap.get(k.split("->")[0]).get(s1).add(new MakeJson("ε", new ArrayList<>()));
                        }
                    }
                    if (k.split("->")[0].equals("declaration_list'")){
                        System.out.println(linkedHashMap.get(k.split("->")[0]));
                    }
                }
            }
        });

//        for (String vn : allVn){
//            for (String s : allVt){
//                if (firstSet.get(vn).contains(s)){
//                    System.out.println(firstSet.get(vn));
//                    List<List<String>> r = allGrammer.copyGrammarMap().get(vn);
//                    r.remove(Collections.singletonList("ε"));
//                    linkedHashMap.get(vn).get(s).addAll(r);
//                }
//                else if (firstSet.get(vn).contains("ε")) {
//                    for (String s1 : followSet.get(vn)) {
//                        if (linkedHashMap.get(vn).get(s1).size() == 0) {
//                            List<String> list = new LinkedList<>();
//                            list.add("ε");
//                            linkedHashMap.get(vn).get(s1).add(list);
//                        }
//                    }
//                    if (linkedHashMap.get(vn).get(s).size() == 0){
//                        List<String> list = new LinkedList<>();
//                        list.add("error");
//                        linkedHashMap.get(vn).get(s).add(list);
//                    }
//                }
//                else {
//                    List<String> list = new LinkedList<>();
//                    list.add("error");
//                    linkedHashMap.get(vn).get(s).add(list);
//                }
//            }
//        }

        System.out.println("***************");
        System.out.println(linkedHashMap.get("declaration_list'"));
        return linkedHashMap;
    }
}
