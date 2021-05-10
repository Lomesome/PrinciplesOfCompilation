package pers.lomesome.compliation.utils.grammatical;

import org.apache.commons.lang3.StringEscapeUtils;
import pers.lomesome.compliation.model.MakeJson;
import pers.lomesome.compliation.model.MyStack;
import pers.lomesome.compliation.model.Word;
import pers.lomesome.compliation.python.DrawTree;
import pers.lomesome.compliation.tool.filehandling.ReadAndWriteFile;
import pers.lomesome.compliation.tool.finalattr.FinalAttribute;

import java.util.*;

public class GrammaticalAnalysis {
    private static AllGrammer allGrammer;

    public static void init(){
        first_follow();
        makePredict();
    }

    private static void first_follow() {
        String content = ReadAndWriteFile.readFileContent("/Users/leiyunhong/IdeaProjects/PrinciplesOfCompilation/src/resources/grammar/MyGrammer.txt"); //C语言官方文法
        GrammaticalHandle grammaticalHandle = new GrammaticalHandle(content);
        allGrammer = new AllGrammer(grammaticalHandle.grammaticlHandel());
        EliminateLR eliminateLR = new EliminateLR(allGrammer);
        eliminateLR.eliminate();
        EliminateBT eliminateBT = new EliminateBT(allGrammer);
        eliminateBT.eliminate();

        allGrammer.getGrammarMap().forEach((k, v)-> FinalAttribute.getAllVn().add(k));
        allGrammer.getGrammarMap().forEach((k, v)->{
            for (List<String> stringList:v){
                FinalAttribute.getAllVt().addAll(stringList);
            }
        });
        FinalAttribute.getAllVt().removeAll(FinalAttribute.getAllVn());
        FinalAttribute.getAllVt().remove("ε");

        FirstSet firstSet = new FirstSet(allGrammer.getGrammarMap());

        FinalAttribute.setFirstmap(firstSet.getFirstSet());

        SelectSet selectSet = new SelectSet(allGrammer.getGrammarMap());
        FinalAttribute.setSelectMap(selectSet.getSelectSet());

        FollowSet followSet = new FollowSet(allGrammer.getGrammarMap(), FinalAttribute.getFirstmap());
        FinalAttribute.setFollowmap(followSet.getFollowSet());

    }

    private static void makePredict(){
        Predict predict = new Predict(allGrammer, FinalAttribute.getSelectMap(), FinalAttribute.getFollowmap());
        FinalAttribute.setPredictMap(predict.predictTable());
    }

    public static List<List<String>> analysis(List<Word> list){
        LinkedHashMap<String, LinkedHashMap<String, List<MakeJson>>> map = FinalAttribute.getPredictMap();
        Stack<MakeJson> makeJsonStack = new MyStack<>();
        boolean errorflag = false;
        List<List<String>> result = new ArrayList<>();
        List<String> analysisResult = new ArrayList<>();
        List<String> error = new ArrayList<>();
        result.add(analysisResult);
        result.add(error);
        makeJsonStack.push(new MakeJson("#", new ArrayList<>()));
        makeJsonStack.push(new MakeJson((String) allGrammer.getGrammarMap().keySet().toArray()[0], new ArrayList<>()));
        list.add(new Word("#", "end", -1,-1));
        list.get(list.size() - 1).setName("#");
        int IP = 0;
        Word a = list.get(IP);
        MakeJson X = makeJsonStack.pop();
        List<MakeJson> makeJsonList = new ArrayList<>();

        while (!X.getName().equals("#")){
            if (X.getName().equals(a.getName())){
                IP++;
                a = list.get(IP);
            } else if (FinalAttribute.getAllVt().contains(X.getName())){
                error.add("error: " +" position line : " + 2 + " 缺少 '" + X.getName()+"'\n");
                errorflag = true;
            } else if (FinalAttribute.getAllVn().contains(X.getName())){
                if (map.get(X.getName()).get(a.getName()).size() == 0){
                    error.add("error: position line : " + a.getCol() + "\n");
                    errorflag = true;
                    X = makeJsonStack.pop();
                    continue;
                }else if (map.get(X.getName()).get(a.getName()).get(0).getName().equals("synch")) {
                    System.out.println("遇到SYNCH，从栈顶弹出非终结符" + X);
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
                        if (!s.getName().equals("ε") || !s.getName().equals("synch")) {
                            makeJsonStack.push(s);
                        }
                    }
                }
            }

            analysisResult.add(makeJsonStack + " " + list.subList(IP, list.size()) + "\n");
            System.out.println(makeJsonStack + " " + list.subList(IP, list.size()));
            X = makeJsonStack.pop();
        }
        if (list.size() - 1 != IP  || errorflag){
            analysisResult.add("该语句不符合该文法\n");
        }else {
            analysisResult.add("该语句符合该文法\n");
        }
        String str = StringEscapeUtils.unescapeJava(makeJsonList.get(0).toString());
        str = str.replace("}\",\"","},").replace("[\"{\"","[{\"").replace("}\"],","}],");
        DrawTree.draw(str);
        return result;
    }
}
