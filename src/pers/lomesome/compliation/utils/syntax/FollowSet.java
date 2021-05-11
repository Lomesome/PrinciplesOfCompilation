package pers.lomesome.compliation.utils.syntax;

import pers.lomesome.compliation.tool.finalattr.FinalAttribute;
import java.util.*;

public class FollowSet {
    
    private final Map<String, Set<String>> followSet = new LinkedHashMap<>();
    private final Map<String, List<List<String>>> grammars;
    private final Map<String, Set<String>> firstSet;

    public FollowSet() {
        this.grammars = FinalAttribute.getAllGrammer().getGrammarMap();
        this.firstSet = FinalAttribute.getFirstmap();
    }

    public Map<String, Set<String>> getFollowSet() {
        getFollow();
        return followSet;
    }

    public void getFollow() {
        Set<String> set = new LinkedHashSet<>();//给开始符号的follow集加#
        set.add("#");
        followSet.put((String) grammars.keySet().toArray()[0], set);

        for (int z = 0; z < FinalAttribute.getAllVn().size(); z++) {
            grammars.forEach((k, v) -> {
                for (List<String> t : v) {
                    if (!t.get(0).equals("ε")) {
                        for (int i = 0; i < t.size(); i++) {
                            if (FinalAttribute.getAllVn().contains(t.get(i))) {//遇到非终结符

                                if (i == (t.size() - 1)) {//如果该非终结符在句尾
                                    if (followSet.containsKey(k)) {
                                        if (followSet.containsKey(t.get(i))) {
                                            Set<String> list_t1 = followSet.get(k);
                                            Set<String> list_t2 = followSet.get(t.get(i));
                                            list_t2.addAll(list_t1);
                                            followSet.put(t.get(i), list_t2);
                                        } else {
                                            Set<String> list_t1 = followSet.get(k);
                                            followSet.put(t.get(i), list_t1);
                                        }
                                    }
                                } else if (!FinalAttribute.getAllVn().contains(t.get(i + 1))) {//如果后面接的是终结符

                                    if (followSet.containsKey(t.get(i))) {
                                        Set<String> list_t1 = followSet.get(t.get(i));
                                        Set<String> list_t2 = new LinkedHashSet<>();
                                        list_t2.add(t.get(i + 1));
                                        list_t1.addAll(list_t2);
                                        followSet.put(t.get(i), list_t1);
                                    } else {
                                        Set<String> list_t2 = new LinkedHashSet<>();
                                        list_t2.add(t.get(i + 1));
                                        followSet.put(t.get(i), list_t2);
                                    }
                                } else if (FinalAttribute.getAllVn().contains(t.get(i + 1))) {//后面是非终结符
                                    if (followSet.containsKey(t.get(i))) {//先将其first集加入前一个非终结符的follow集
                                        Set<String> list_t1 = followSet.get(t.get(i));
                                        Set<String> list_t2 = firstSet.get(t.get(i + 1));
                                        list_t1.addAll(list_t2);
                                        followSet.put(t.get(i), list_t1);
                                    } else {
                                        Set<String> list_t1 = firstSet.get(t.get(i + 1));
                                        followSet.put(t.get(i), list_t1);
                                    }
                                    if (firstSet.get(t.get(i + 1)).contains("ε")) {//含空则将往后扫描
                                        int flag = 0;//为0表示直到扫描完都没遇到终结符或者说遇到的非终结符的first集都含空
                                        for (int i2 = i + 2; i2 < t.size(); i2++) {
                                            if (!FinalAttribute.getAllVn().contains(t.get(i2))) {//如果碰到终结符
                                                if (followSet.containsKey(t.get(i))) {
                                                    Set<String> list_t1 = followSet.get(t.get(i));
                                                    Set<String> list_t2 = new LinkedHashSet<>();
                                                    list_t2.add(t.get(i2));
                                                    list_t1.addAll(list_t2);
                                                    followSet.put(t.get(i), list_t1);
                                                } else {
                                                    Set<String> list_t2 = new LinkedHashSet<>();
                                                    list_t2.add(t.get(i2));
                                                    followSet.put(t.get(i), list_t2);
                                                }
                                                flag = 1;
                                                i2 = t.size();//跳出
                                            } else {//遇到非终结符先放first集
                                                if (followSet.containsKey(t.get(i))) {
                                                    Set<String> list_t1 = followSet.get(t.get(i));
                                                    Set<String> list_t2 = firstSet.get(t.get(i2));
                                                    list_t1.addAll(list_t2);
                                                    followSet.put(t.get(i), list_t1);
                                                } else {
                                                    Set<String> list_t1 = firstSet.get(t.get(i2));
                                                    followSet.put(t.get(i), list_t1);
                                                }
                                                if (!firstSet.get(t.get(i2)).contains("ε")) {//如果不含空，则停止往后
                                                    flag = 1;
                                                    i2 = t.size();
                                                }
                                            }
                                        }

                                        if (flag == 0) {
                                            if (followSet.containsKey(k)) {
                                                if (followSet.containsKey(t.get(i))) {
                                                    Set<String> list_t1 = followSet.get(k);
                                                    Set<String> list_t2 = followSet.get(t.get(i));
                                                    list_t2.addAll(list_t1);
                                                    followSet.put(t.get(i), list_t2);
                                                } else {
                                                    Set<String> list_t1 = followSet.get(k);
                                                    followSet.put(t.get(i), list_t1);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            });
        }
        grammars.forEach((k, v) -> {
            if (followSet.containsKey(k)) {
                Set<String> t = followSet.get(k);
                t.remove("ε");
            }
        });
    }
}
