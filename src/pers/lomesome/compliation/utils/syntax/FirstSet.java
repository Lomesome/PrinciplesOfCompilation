package pers.lomesome.compliation.utils.syntax;

import pers.lomesome.compliation.tool.finalattr.FinalAttribute;
import java.util.*;

public class FirstSet {
    public Map<String, Set<String>> firstSet;
    private final Map<String, List<List<String>>> grammars;

    public FirstSet(){
        this.grammars = FinalAttribute.getAllGrammer().getGrammarMap();
    }

    public Map<String, Set<String>> getFirstSet() {
        firstSet = new LinkedHashMap<>();
        grammars.forEach((k, v)->{
            Set<String> vnFirst = new LinkedHashSet<>();
            getFirst(k, vnFirst);
            firstSet.put(k, vnFirst);
        });

        return firstSet;
    }

    private Set<String> getFirst(String vn, Set<String> vnFirst) {
        if (!grammars.containsKey(vn)) {
            vnFirst.add(vn);
            return vnFirst;
        }
        int count = 0;
        boolean nullFlag = false;
        for (List<String> infer : grammars.get(vn)) {
            for (String singleInfer : infer) {
                Set<String> newVnFirst = new LinkedHashSet<>();
                Set<String> checkNull = getFirst(singleInfer, newVnFirst);
                vnFirst.addAll(checkNull);
                vnFirst.remove("ε");
                if (singleInfer.equals("ε")){  //可以推导出空串
                    nullFlag = true;
                }
                if (checkNull.contains("ε") && grammars.containsKey(singleInfer)){ //判断推导内容全为非终结符
                    count++;
                }
                else if (!checkNull.contains("ε")) {  //若当前非终结符不包含空串,则不进行下一个非终结符的分析
                    break;
                }
            }
        }
        if (count == grammars.get(vn).size() || nullFlag) { //推导内容全为非终结符且都推导出空串
            vnFirst.add("ε");
        }
        return vnFirst;
    }
}
