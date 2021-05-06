package pers.lomesome.compliation.utils.grammatical;

import org.apache.commons.lang3.StringEscapeUtils;
import pers.lomesome.compliation.model.MakeJson;
import pers.lomesome.compliation.model.MyStack;
import pers.lomesome.compliation.python.DrawTree;
import pers.lomesome.compliation.tool.filehandling.ReadAndWriteFile;
import java.util.*;

public class GrammaticalAnalysis {

    private final Stack<MakeJson> makeJsonStack = new MyStack<>();
    private LinkedHashMap<String, LinkedHashMap<String, List<MakeJson>>> map;
    private final Set<String> allVn = new LinkedHashSet<>();
    private final Set<String> allVt = new LinkedHashSet<>();
    private AllGrammer allGrammer;
    private Map<String, Set<String>> singlefirstmap;
    private Map<String, Set<String>> followmap;

    public void first_follow() {
        String content = ReadAndWriteFile.readFileContent("/Users/leiyunhong/IdeaProjects/PrinciplesOfCompilation/src/resources/grammar/test2.txt"); //C语言官方文法
        GrammaticalHandle grammaticalHandle = new GrammaticalHandle(content);
        allGrammer = new AllGrammer(grammaticalHandle.grammaticlHandel());

        EliminateLR eliminateLR = new EliminateLR(allGrammer);
        eliminateLR.eliminate();
        EliminateBT eliminateBT = new EliminateBT(allGrammer);
        eliminateBT.eliminate();
        eliminateBT.eliminate();
        allGrammer.getGrammarMap().forEach((k, v)->{
            System.out.println(k +"->"+v);
        });

        allGrammer.getGrammarMap().forEach((k, v)->{
            allVn.add(k);
        });
        allGrammer.getGrammarMap().forEach((k, v)->{
            for (List<String> stringList:v){
                allVt.addAll(stringList);
            }
        });
        allVt.removeAll(allVn);
        allVt.remove("ε");

        FirstSet firstSet = new FirstSet(allGrammer.getGrammarMap());

        Map<String, Set<String>> firstmap = firstSet.getFirstSet();
        firstmap.forEach((k,v)->{
            System.out.println("First(" + k +")="+v);
        });

        SingleFirst singleFirst = new SingleFirst(allGrammer.getGrammarMap());
        singlefirstmap = singleFirst.getFirstSet();
        singlefirstmap.forEach((k,v)->{
            System.out.println("First(" + k +")="+v);
        });
        FollowSet followSet = new FollowSet(allGrammer.getGrammarMap(), firstmap);
        followmap = followSet.getFollowSet();
        followmap.forEach((k,v)->{
            System.out.println("Follow(" + k +")="+v);
        });
    }

    public void makePredict(){
        Predict predict = new Predict(allGrammer, singlefirstmap, followmap);
        map = predict.predictTable();
        map.forEach((k, v)->{
            System.out.println(k + " " + v);
        });
    }

    public void analysis(List<String> list){
        makeJsonStack.push(new MakeJson("#", new ArrayList<>()));
        makeJsonStack.push(new MakeJson((String) allGrammer.getGrammarMap().keySet().toArray()[0], new ArrayList<>()));
        list.add("#");
        int IP = 0;
        String a = list.get(IP);
        MakeJson X = makeJsonStack.pop();
        List<MakeJson> makeJsonList = new ArrayList<>();

        while (!X.getName().equals("#")){
            if (X.getName().equals(a)){
                IP++;
                a = list.get(IP);
            } else if (allVt.contains(X.getName())){
                System.out.println("error");
            } else if (allVn.contains(X.getName())){
                if (map.get(X.getName()).get(a) == null){
                    System.out.println("error");
                }else {
                    List<MakeJson> stringList = map.get(X.getName()).get(a);
                    List<MakeJson> makeJsons = new ArrayList<>(stringList.subList(0, stringList.size()));
                    List<MakeJson> makeJsons2 = new ArrayList<>(stringList.subList(0, stringList.size()));
                    if (allVn.contains(X.getName()) && makeJsons2.size() == 0){
                        makeJsons2.add(new MakeJson("ε", new ArrayList<>()));
                    }
                    X.setChildren(makeJsons2);
                    makeJsonList.add(X);
                    Collections.reverse(makeJsons);
                    for (MakeJson s : makeJsons){
                        if (!s.getName().equals("ε")) {
                            makeJsonStack.push(s);
                        }
                    }
                }
            }
            System.out.println(makeJsonStack + " " + list.subList(IP, list.size()));
            X = makeJsonStack.pop();
        }
        if (list.size() - 1 != IP){
            System.out.println("该语句不符合该文法");
        }else {
            System.out.println("该语句符合该文法");
        }

        String str = StringEscapeUtils.unescapeJava(makeJsonList.get(0).toString());
        str = str.replace("}\",\"","},").replace("[\"{\"","[{\"").replace("}\"],","}],");
        DrawTree.draw(str);
    }
}
