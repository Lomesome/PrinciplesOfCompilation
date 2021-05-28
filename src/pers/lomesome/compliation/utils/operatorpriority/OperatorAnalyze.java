package pers.lomesome.compliation.utils.operatorpriority;

import pers.lomesome.compliation.model.Word;
import pers.lomesome.compliation.tool.filehandling.StringAlign;

import java.util.*;

public class OperatorAnalyze {

    private Map<String, Set<String>> firstVt = new HashMap<>();  //FIRSTVT集合

    private Map<String, Set<String>> lastVt = new HashMap<>();  //LASTVT集合

    private List<String> input = new ArrayList<>();  //输入的文法

    private Set<String> vtSet = new LinkedHashSet<>();  //终结符

    private Set<String> vnSet = new LinkedHashSet<>();  //非终结符


    private Map<String, Map<String, String>> matrixMap = new LinkedHashMap<>();  //算符矩阵

    private Map<String, List<List<String>>> produce = new LinkedHashMap<>();  //文法的左右分割一一对应

    private StringAlign align5 = new StringAlign(5, StringAlign.Alignment.LEFT);

    private StringAlign align10 = new StringAlign(15, StringAlign.Alignment.LEFT);

    private StringAlign align30 = new StringAlign(30, StringAlign.Alignment.LEFT);

    private List<String> result = new ArrayList<>();

    private List<String> error = new ArrayList<>();

    private void getEndAndNoEnd() {
        vnSet.addAll(produce.keySet());
        produce.forEach((k, v)->{
            v.forEach(s -> {
                vtSet.addAll(s);
            });
        });
        vtSet.removeAll(vnSet);
        vtSet.add("#");
    }

    private void getProduce() {
        for (String s : input) {
            String[] splitProduce = s.split("->");
            String left = splitProduce[0];
            String right = splitProduce[1];
            String[] splitRight = right.split("\\|");
            List<List<String>> list = new ArrayList<>();
            for (String single : splitRight){
                List<String> sList = new ArrayList<>(Arrays.asList(single.split(" ")));
                list.add(sList);
            }
            produce.put(left, list);
        }
    }

    private void makeMatrix() {
        produce.forEach((k, v)->{
            for (List<String> list : v) {
                int len = list.size();
                for (int j = 0; j < len; j++) {
                    if (vtSet.contains(list.get(j))) {
                        if (j < len - 1) {
                            if (vtSet.contains(list.get(j + 1))) {
                                setMatrixMap(list.get(j), list.get(j + 1), "=");
                            }
                            if (vnSet.contains(list.get(j + 1))) {
                                for (String s : firstVt.get(list.get(j + 1))) {
                                    setMatrixMap(list.get(j), s, "<");
                                }
                            }
                        }
                        if (j < len - 2 && vnSet.contains(list.get(j + 1)) && vtSet.contains(list.get(j + 2))) {
                            setMatrixMap(list.get(j), list.get(j + 2), "=");
                        }
                    }
                    if (vnSet.contains(list.get(j))) {
                        if (j < len - 1) {
                            if (vtSet.contains(list.get(j + 1))) {
                                for (String s : lastVt.get(list.get(j))) {
                                    setMatrixMap(s, list.get(j + 1), ">");
                                }
                            }
                        }
                    }
                }
            }
        });

        Set<String> coll = firstVt.get(produce.keySet().toArray()[0]);
        for (String value : coll) {
            setMatrixMap("#", value, "<");
        }

        Set<String> coll1 = lastVt.get(produce.keySet().toArray()[0]);
        for (String value : coll1) {
            setMatrixMap(value, "#", ">");
        }

        setMatrixMap("#", "#", "=");

        for (String value : vtSet) {
            for (String value1 : vtSet) {
                if (matrixMap.get(value).get(value1) == null)
                    setMatrixMap(value, value1, "");
            }
        }
    }

    private void setMatrixMap(String s1, String s2, String op){
        Map<String, String> map ;
        if (matrixMap.get(s1)== null){
            map = new LinkedHashMap<>();
        }else {
            map = matrixMap.get(s1);
        }
        map.put(s2, op);
        matrixMap.put(s1, map);
    }

    private String findLeft(List<String> list) {
        String ch = null;
        for (Map.Entry<String, List<List<String>>> map : produce.entrySet()) {
            ch = map.getKey();
            for (List<String> value : map.getValue()) {

                if (value.size() != list.size()) {
                    continue;
                }
                boolean flag = false;
                for (int i = 0; i < value.size(); i++){
                    if (vtSet.contains(value.get(i))){
                        if (!value.get(i).equals(list.get(i))){
                            flag = true;
                        }
                    }
                }
                if (!flag){
                    return ch;
                }
            }
        }
        return ch;
    }

    public void analysis(List<Word> wordList) {
        int status = 0;
        int count = 0;
        int top = 0;
        int j = 0;
        int index = 0;
        String statute = null;
        wordList.add(new Word("#"));
        List<String> listStack = new ArrayList<>();
        listStack.add("#");
        String a = String.valueOf(wordList.get(index++).getWord());
        do {
            if (status == 0) {
                if (count != 0) {
                   result.add(align10.format("移进") + "\n" + align5.format(count) +  align30.format(listStack) + align10.format(a) + align10.format(wordList.subList(index, wordList.size())));
                } else {
                   result.add(align5.format(count) +  align30.format(listStack) + align10.format(a) + align10.format(wordList.subList(index, wordList.size())));
                }
            } else {
               result.add(align10.format(statute) + "\n" + align5.format(count) +  align30.format(listStack) + align10.format(a) + align10.format(wordList.subList(index, wordList.size())));
            }
            String s = listStack.get(top);
            if (vtSet.contains(s)) {
                j = top;
            } else if (j >= 1) {
                j = top - 1;
            }
            String temp;
            if (matrixMap.get(listStack.get(j)).get(a) != null) {
                //规约
                while (matrixMap.get(listStack.get(j)).get(a).equals(">")) {
                    if (listStack.size() == 2 && a.equals("#")) {
                        break;
                    }
                    List<String> judge = new ArrayList<>();
                    do {
                        temp = listStack.get(j);
                        if (vtSet.contains(listStack.get(j - 1))) {
                            j = j - 1;
                        } else {
                            j = j - 2;
                        }
                    } while (!matrixMap.get(listStack.get(j)).get(temp).equals("<"));
                    for (int i = j + 1; i < top + 1; i++) {
                        judge.add(listStack.get(i));
                    }
                    if (top > j) {
                        listStack.subList(j + 1, top + 1).clear();
                    }
                    String res = findLeft(judge);
                    if (res != null) {
                        count++;
                        top = j + 1;
                        listStack.add(res);
                        status = 1;
                        statute = "用" + res + "->" + judge.toString() + "规约";
                        result.add(align10.format(statute) + "\n" + align5.format(count) +  align30.format(listStack) + align10.format(a) + align10.format(wordList.subList(index, wordList.size())));
                    }
                }
            }
            if (matrixMap.get(listStack.get(j)).get(a).equals("<") || matrixMap.get(listStack.get(j)).get(a).equals("=")) {
                count++;
                top++;
                status = 0;
                listStack.add(a);
            } else {
                if(matrixMap.get(listStack.get(j)).get(a).equals("")){
                    result.add(align10.format("错误! \n"));
                }
            }
            if (listStack.size() == 2 && a.equals("#")) {
                break;
            }
            if (index < wordList.size()) {
                a = String.valueOf(wordList.get(index++).getWord());
            } else {
                break;
            }
        } while (listStack.size() != 2 || !a.equals("#"));
        result.add(align10.format("分析成功"));
    }

    public void doAna(List<Word> wordList) {
        input.add("E->E + T|T");
        input.add("T->T * F|F");
        input.add("F->( E )|i");
        getProduce();
        getEndAndNoEnd();
        firstVt = new FirstVt(produce, vtSet, vnSet).getFirstVt();
        lastVt = new LastVt(produce, vtSet, vnSet).getLastVt();
        makeMatrix();
        analysis(wordList);
    }
    /*
    E->E+T|T
    T->T*F|F
    F->(E)|i
    */
    public List<String> getResult() {
        return result;
    }

    public static void main(String[] args) {
        OperatorAnalyze OperatorAnalyze = new OperatorAnalyze();
        List<Word> list = new ArrayList<>();
        list.add(new Word(")"));
        list.add(new Word("("));
        list.add(new Word("i"));
        list.add(new Word("+"));
        list.add(new Word("i"));
        list.add(new Word(")"));
        OperatorAnalyze.doAna(list);
        System.out.println();
        for (String s : OperatorAnalyze.getResult()){
            System.out.print(s);
        }
    }
}
