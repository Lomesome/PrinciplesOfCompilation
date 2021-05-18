package pers.lomesome.compliation.utils.semantic;

import pers.lomesome.compliation.model.Word;
import pers.lomesome.compliation.tool.finalattr.FinalAttribute;
import java.util.*;

public class Analysis {
    public static int i;
    public static Boolean flag;
    public static List<String> errorMsg;
    public static List<Word> list;
    public static String nowFunc;
    public static int scope;
    public static Stack<Integer> actionscope;

    public static void init(List<Word> list){
        i = 0;
        flag = true;
        errorMsg = new ArrayList<>();
        Analysis.list = list;
        nowFunc = "global";
        actionscope = new Stack<>();
        scope = 0;
        actionscope.push(scope);
    }

    public static Object[] analysis(List<Word> list) {
        init(list);
        FinalAttribute.clearSymbolTableMap();
        Object[] results = new Object[4];
        SymbolTable rootSymbolTable = new SymbolTable("global", "void");
        FinalAttribute.addSymbolTable(nowFunc, rootSymbolTable);
        FinalAttribute.getSymbolTable(nowFunc);
        LinkedHashMap<String, LinkedHashMap<String, List<String>>> map = FinalAttribute.getSemPredictMap();
        Stack<String> stringStack = new Stack<>();
        stringStack.push("#");
        stringStack.push((String) FinalAttribute.getAllGrammer().getGrammarMap().keySet().toArray()[0]);
        int IP = 0;
        Word a = list.get(IP);
        String X = stringStack.pop();
        while (!X.equals("#")) {
            if (X.equals(a.getName())) {
                IP++;
                a = list.get(IP);
                FinalAttribute.getSymbolTable(nowFunc).setTable(a);
            } else if (FinalAttribute.getAllVt().contains(X)) {

            } else if (FinalAttribute.getAllVn().contains(X)) {
                if (map.get(X).get(a.getName()).size() == 0) {
                    X = stringStack.pop();
                    continue;
                } else if (map.get(X).get(a.getName()).get(0).equals("synch")) {
                    X = stringStack.pop();
                    continue;
                } else {
                    List<String> stringList = map.get(X).get(a.getName());
                    List<String> actionList = findAction(stringList, X);
                    List<String> copyaction = new ArrayList<>(actionList.subList(0, actionList.size()));
                    if (FinalAttribute.getAllVn().contains(X) && copyaction.size() == 0) {
                        copyaction.add("ε");
                    }
                    Collections.reverse(copyaction);
                    for (String s : copyaction) {
                        if (!s.equals("ε")) {
                            if (!s.equals("synch"))
                                stringStack.push(s);
                        }
                    }
                }
            } else {
                if (!X.equals("ε"))
                    NewSemanticAnalysis.call(X, IP, list.get(IP - 1), FinalAttribute.getSymbolTable(nowFunc),  FinalAttribute.getSymbolTable(nowFunc).getLiveStatu());
            }
            X = stringStack.pop();
        }
        if (flag){
            FinalAttribute.getSymbolTableMap().forEach((k, v)->{
                System.out.println(k);
                results[1] = v.printTable().get(0);
                results[2] = v.printTable().get(1);
                NewSemanticAnalysis.printQuaternary(v.getLiveStatu());
            });

            results[0] =  FinalAttribute.getSymbolTable(nowFunc).getLiveStatu();
        }
        results[3] = errorMsg;
        return results;
    }

    public static List<String> findAction(List<String> list, String vn) {
        List<String> find = new ArrayList<>();
        for (List<String> list1 : FinalAttribute.getAllGrammerWithAction().getGrammarMap().get(vn)) {
            if (equalsList(list, list1)) {
                find = list1;
                break;
            }
        }
        return find;
    }

    public static boolean equalsList(List<String> A, List<String> B) {
        List<String> copyA = new ArrayList<>(A.subList(0, A.size()));
        List<String> copyB = new ArrayList<>(B.subList(0, B.size()));
        copyA.removeAll(Arrays.asList(FinalAttribute.getSem()));
        copyB.removeAll((Arrays.asList(FinalAttribute.getSem())));
        int len = copyA.size();
        if (copyA.size() == copyB.size()) {
            for (int i = 0; i < len; i++) {
                if (!copyA.get(i).equals(copyB.get(i))) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }
}
