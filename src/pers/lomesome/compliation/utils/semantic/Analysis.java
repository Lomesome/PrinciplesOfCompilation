package pers.lomesome.compliation.utils.semantic;

import pers.lomesome.compliation.model.LiveStatu;
import pers.lomesome.compliation.model.Word;
import pers.lomesome.compliation.tool.finalattr.FinalAttribute;
import pers.lomesome.compliation.utils.toasm.ToAsmCode;
import java.io.IOException;
import java.util.*;

public class Analysis {
    public static int i;
    public static Boolean flag;
    public static List<String> errorMsg;

    public static Object[] analysis(List<Word> list, SymbolTable table) throws IOException {
        Object[] results = new Object[3];
        errorMsg = new ArrayList<>();
        flag = true;
        i = 0;
        LiveStatu liveStatu = new LiveStatu();
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
                    SemanticAnalysis.call(X, IP, list.get(IP - 1).getWord(), table, liveStatu);
            }
            X = stringStack.pop();
        }

        if (flag){
            results[0] = liveStatu;
            SemanticAnalysis.printQuaternary(liveStatu);
            ToAsmCode asm = new ToAsmCode();
            asm.cToAsm(table, liveStatu);
            List<String> asmString = new ArrayList<>();
            for (int i = 0; i < asm.preAsmCode.size(); i++) {
                System.out.println(asm.preAsmCode.get(i));
                asmString.add(asm.preAsmCode.get(i) + "\n");
            }
            for (int i = 0; i < asm.asmCode.size(); i++) {
                System.out.println(asm.asmCode.get(i));
                asmString.add(asm.asmCode.get(i) + "\n");
            }
            for (int j = 0; j < asm.asmJump.length; j++) {
                System.out.println(j + "  " + asm.asmJump[j]);
            }
            results[1] = asmString;
        }
        results[2] = errorMsg;
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
        copyA.removeAll(FinalAttribute.getActionMap().keySet());
        copyB.removeAll(FinalAttribute.getActionMap().keySet());
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
