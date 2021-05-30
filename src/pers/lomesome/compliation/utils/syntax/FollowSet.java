package pers.lomesome.compliation.utils.syntax;

import pers.lomesome.compliation.tool.finalattr.FinalAttribute;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class FollowSet {

    private final Map<String, Set<String>> followSet = new LinkedHashMap<>();
    private final Map<String, List<List<String>>> grammars;
    private final Map<String, Set<String>> firstSet;
    private int nowLen = 0;

    public FollowSet(){
        this.grammars = FinalAttribute.getAllGrammer().getGrammarMap();
        this.firstSet = FinalAttribute.getFirstmap();
    }

    public Map<String, Set<String>> getFollowSet() {
        do {
            grammars.forEach((k, v) -> getFollow(k));
        } while (followChange());
        return followSet;
    }

    public boolean followChange(){
        AtomicInteger len = new AtomicInteger();
        followSet.forEach((k, v)->{
            len.addAndGet(v.size());
        });
        if (len.get() == nowLen){
            return false;
        }
        nowLen = len.get();
        return true;
    }
    private void getFollow(String c){
        List<List<String>> list = grammars.get(c);
        Set<String> setA = followSet.containsKey(c) ? followSet.get(c) : new LinkedHashSet<>();
        if (c == grammars.keySet().toArray()[0]) {  //如果是开始符 添加 #
            setA.add("#");
        }
        for (String ch : grammars.keySet()) {   //查找输入的所有产生式，确定c的后跟 终结符
            List<List<String>> l = grammars.get(ch);
            for (List<String> s : l)
                for (int i = 0; i < s.size(); i++)
                    if (s.get(i).equals(c) && i + 1 < s.size() && !grammars.containsKey(s.get(i + 1)))
                        setA.add(s.get(i + 1));
        }
        followSet.put(c, setA);
        for (List<String> s : list) {  //处理c的每一条产生式
            int i = s.size() - 1;
            while (i >= 0 ) {
                String tn = s.get(i);
                if(grammars.containsKey(tn)){  //只处理非终结符 （都按 A->αBβ  形式处理）
                    if (s.size() - i - 1 > 0) {  //若β存在
                        List<String> right = s.subList(i + 1, s.size());
                        Set<String> setF = new LinkedHashSet<>(); //把β的非空first集  加入followB
                        String s1 = right.get(0);
                        if (grammars.containsKey(right.get(0))){
                            setF = firstSet.get(s1);
                        }else {
                            setF.add(s1);
                        }
                        Set<String> setX = followSet.containsKey(tn) ? followSet.get(tn) : new LinkedHashSet<>();
                        for (String var : setF)
                            if (!var.equals("ε"))
                                setX.add(var);
                        followSet.put(tn, setX);
                        if(setF.contains("ε")){  // 若first(β)包含空串   followA 加入 followB
                            if(!tn.equals(c)){
                                Set<String> setB = followSet.containsKey(tn) ? new HashSet<>(followSet.get(tn)) : new LinkedHashSet<>();
                                setB.addAll(setA);
                                followSet.put(tn, setB);
                            }
                        }
                    }
                    else{  //若β不存在   followA 加入 followB
                        if(!tn.equals(c)){  // A和B相同不添加
                            Set<String> setB = followSet.containsKey(tn) ? followSet.get(tn) : new LinkedHashSet<>();
                            setB.addAll(setA);
                            followSet.put(tn, setB);
                        }
                    }
                }
                i--;
            }
        }
    }
}
