package pers.lomesome.compliation.utils.syntax;

import org.apache.commons.lang3.StringEscapeUtils;
import pers.lomesome.compliation.model.MakeJson;
import pers.lomesome.compliation.model.MyStack;
import pers.lomesome.compliation.model.Word;
import pers.lomesome.compliation.python.DrawTree;
import pers.lomesome.compliation.tool.finalattr.FinalAttribute;

import java.util.*;

public class SyntaxAnalysis {

    public static void init(){
        first_follow();
        makePredict();
    }

    private static void first_follow() {
        EliminateLR eliminateLR = new EliminateLR(FinalAttribute.getAllGrammer());
        eliminateLR.eliminate();
        EliminateBT eliminateBT = new EliminateBT(FinalAttribute.getAllGrammer());
        eliminateBT.eliminate();
        eliminateBT.eliminate();
        eliminateBT.eliminate();
        eliminateBT.eliminate();

        FinalAttribute.getAllGrammer().getGrammarMap().forEach((k, v)-> FinalAttribute.getAllVn().add(k));
        FinalAttribute.getAllGrammer().getGrammarMap().forEach((k, v)->{
            for (List<String> stringList:v){
                FinalAttribute.getAllVt().addAll(stringList);
            }
        });
        FinalAttribute.getAllVt().removeAll(FinalAttribute.getAllVn());
        FinalAttribute.getAllVt().remove("ε");

        FirstSet firstSet = new FirstSet();
        FinalAttribute.setFirstmap(firstSet.getFirstSet());

        SingleFirstSet singleFirstSet = new SingleFirstSet(FinalAttribute.getAllGrammer().getGrammarMap());
        FinalAttribute.setSelectMap(singleFirstSet.getSelectSet());

        FollowSet followSet = new FollowSet();
        FinalAttribute.setFollowmap(followSet.getFollowSet());

    }

    private static void makePredict(){
        Predict predict = new Predict(FinalAttribute.getAllGrammer(), FinalAttribute.getSelectMap(), FinalAttribute.getFollowmap());
        FinalAttribute.setPredictMap((LinkedHashMap<String, LinkedHashMap<String, List<MakeJson>>>) predict.predictTable().get(0));
        FinalAttribute.setSemPredictMap((LinkedHashMap<String, LinkedHashMap<String, List<String>>>) predict.predictTable().get(1));
    }

    public static List<List<String>> analysis(List<Word> list) {
        LinkedHashMap<String, LinkedHashMap<String, List<MakeJson>>> map = FinalAttribute.getPredictMap();
        Stack<MakeJson> makeJsonStack = new MyStack<>();
        boolean errorflag = true;
        List<List<String>> result = new ArrayList<>();
        List<String> analysisResult = new ArrayList<>();
        List<String> error = new ArrayList<>();
        result.add(analysisResult);
        result.add(error);
        makeJsonStack.push(new MakeJson("#", new ArrayList<>()));
        makeJsonStack.push(new MakeJson((String) FinalAttribute.getAllGrammer().getGrammarMap().keySet().toArray()[0], new ArrayList<>()));

        int IP = 0;
        Word a = list.get(IP);
        MakeJson X = makeJsonStack.pop();
        List<MakeJson> makeJsonList = new ArrayList<>();

        while (!X.getName().equals("#")){
            if (X.getName().equals(a.getName())){
                IP++;
                a = list.get(IP);
            } else if (FinalAttribute.getAllVt().contains(X.getName())){
                error.add("error: " +" position : (" + list.get(IP- 1).getRow() + "," + list.get(IP- 1).getCol() + ") 缺少 '" + X.getName()+"'\n");
                System.out.println("error: " +" position : (" + list.get(IP- 1).getRow() + "," + list.get(IP- 1).getCol() + ") 缺少 '" + X.getName()+"'\n");
                errorflag = false;
            } else if (FinalAttribute.getAllVn().contains(X.getName())){
                if (map.get(X.getName()).get(a.getName()).size() == 0){
                    System.out.println("遇到SYNCH，从栈顶弹出非终结符" + X);
//                    error.add("error: position line : " + a.getCol() + "\n");
                    errorflag = false;
                    X = makeJsonStack.pop();
                    continue;
                }else if (map.get(X.getName()).get(a.getName()).get(0).getName().equals("synch")) {
                    System.out.println("遇到SYNCH，从栈顶弹出非终结符" + X);
                    errorflag = false;
                    X = makeJsonStack.pop();
                    continue;
                } else {
                    List<MakeJson> stringList = map.get(X.getName()).get(a.getName());
                    List<MakeJson> makeJsons = new ArrayList<>(stringList.subList(0, stringList.size()));
                    if (FinalAttribute.getAllVn().contains(X.getName()) && makeJsons.size() == 0){
                        makeJsons.add(new MakeJson("ε", new ArrayList<>()));
                    }
                    X.setChildren(stringList);
                    makeJsonList.add(X);
                    Collections.reverse(makeJsons);
                    for (MakeJson s : makeJsons){
                        if (!s.getName().equals("ε")) {
                            if (!s.getName().equals("synch")) {
                                makeJsonStack.push(s);
                            }
                        }
                    }
                }
            }
            analysisResult.add(makeJsonStack + " " + list.subList(IP, list.size()) + "\n");
            System.out.println(makeJsonStack + " " + list.subList(IP, list.size()));
            X = makeJsonStack.pop();
        }
        if (list.size() - 1 != IP  || !errorflag){
            System.out.println("该语句不符合该文法");
            analysisResult.add("该语句不符合该文法\n");
        }else {
            System.out.println("该语句符合该文法");
            String str = StringEscapeUtils.unescapeJava(makeJsonList.get(0).toString());
            str = str.replace("}\",\"","},").replace("[\"{\"","[{\"").replace("}\"],","}],");
            DrawTree.draw(str);
            analysisResult.add("该语句符合该文法\n");
        }
        return result;
    }
}
