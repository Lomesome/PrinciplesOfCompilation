package pers.lomesome.compliation.utils.semantic;

import pers.lomesome.compliation.model.Quaternary;
import pers.lomesome.compliation.model.Word;
import pers.lomesome.compliation.tool.finalattr.FinalAttribute;
import pers.lomesome.compliation.utils.toasm.ObjectCode;
import pers.lomesome.compliation.utils.toasm.ToNasmCode;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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
        Object[] results = new Object[6];
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
                    SemanticAnalysis.call(X, IP, list.get(IP - 1), FinalAttribute.getSymbolTable(nowFunc),  FinalAttribute.getSymbolTable(nowFunc).getLiveStatu());
            }
            X = stringStack.pop();
        }
        if (flag){
            Quaternary q = new Quaternary();
            q.setOperator(new Word("sys"));
            q.setArg1(new Word("_"));
            q.setArg2(new Word("_"));
            q.setResult(new Word("_"));
            FinalAttribute.getSymbolTable("main").getLiveStatu().getQt().add(q);
            SymbolTable s = FinalAttribute.getSymbolTableMap().get("main");

            ObjectCode.Blocked(FinalAttribute.getSymbolTable("main").getLiveStatu());

            FinalAttribute.getSymbolTable("main").getLiveStatu().getActiveLable().forEach(System.out::println);
            AtomicInteger id1 = new AtomicInteger();
            List<Quaternary> list1 =  FinalAttribute.getSymbolTable("main").getLiveStatu().getQt();
            list1.forEach(quaternary -> {
                quaternary.setId(id1.getAndIncrement());
            });

            List<List<Object>> listList = s.printTable();
            results[1] = listList.get(0);
            results[2] = listList.get(1);
            results[3] = listList.get(2);
            SemanticAnalysis.printQuaternary(s.getLiveStatu());
            results[0] =  FinalAttribute.getSymbolTable("main").getLiveStatu();

//            ToAsm asm = new ToAsm();
            ToNasmCode asm = new ToNasmCode();
            asm.cToAsm(FinalAttribute.getSymbolTable("main"), FinalAttribute.getSymbolTable("main").getLiveStatu());
            try{
                results[4] = asm.getResults();
            }catch (Exception ignored){

            }
        }



//        DAG.doDAG(list1);
        List<String> newst= new ArrayList<>(new LinkedHashSet<>(errorMsg));
        results[5] = newst;
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
