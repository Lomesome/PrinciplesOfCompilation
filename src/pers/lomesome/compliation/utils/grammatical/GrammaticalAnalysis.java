package pers.lomesome.compliation.utils.grammatical;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pers.lomesome.compliation.python.DrawTree;
import pers.lomesome.compliation.tool.filehandling.ReadAndWriteFile;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GrammaticalAnalysis {

    Stack<String> stack = new Stack<>();
    Stack<MakeJson> makeJsonStack = new Stack<>();
    LinkedHashMap<String, LinkedHashMap<String, List<MakeJson>>> map;
    Set<String> allVn = new LinkedHashSet<>();
    Set<String> allVt = new LinkedHashSet<>();

    public void first_follow() throws JSONException {
        String content = ReadAndWriteFile.readFileContent("/Users/leiyunhong/IdeaProjects/PrinciplesOfCompilation/src/resources/grammar/test6.txt"); //C语言官方文法
        GrammaticalHandle grammaticalHandle = new GrammaticalHandle(content);
        AllGrammer allGrammer = new AllGrammer(grammaticalHandle.grammaticlHandel());
        EliminateLR eliminateLR = new EliminateLR(allGrammer);
        eliminateLR.eliminate();
        EliminateBT eliminateBT = new EliminateBT(allGrammer);
        eliminateBT.eliminate();
        allGrammer.getGrammarMap().forEach((k, v)->{
            System.out.println(k +"->"+v);
        });
        FirstSet firstSet = new FirstSet(allGrammer.getGrammarMap());

        Map<String, Set<String>> firstmap = firstSet.getFirstSet();
        firstmap.forEach((k,v)->{
            System.out.println("First(" + k +")="+v);
        });

        SingleFirst singleFirst = new SingleFirst(allGrammer.getGrammarMap());
        Map<String, Set<String>> singlefirstmap = singleFirst.getFirstSet();
        singlefirstmap.forEach((k,v)->{
            System.out.println("First(" + k +")="+v);
        });
        FollowSet followSet = new FollowSet(allGrammer.getGrammarMap(), firstmap);
        Map<String, Set<String>> followmap = followSet.getFollowSet();
        followmap.forEach((k,v)->{
            System.out.println("Follow(" + k +")="+v);
        });
        NewPredict predict = new NewPredict(allGrammer, singlefirstmap, followmap);
        map = predict.predictTable();
        map.forEach((k, v)->{
//            codeInterface.setRunText(k + " " + v);
            System.out.println(k + " " + v);
        });
//

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
        makeJsonStack.push(new MakeJson("#", new ArrayList<>()));
        makeJsonStack.push(new MakeJson((String) allGrammer.getGrammarMap().keySet().toArray()[0], new ArrayList<>()));
        List<String> list = new LinkedList<>();
        list.add("b");
        list.add("i");
        list.add("c");
        list.add("t");
        list.add("a");
        list.add("e");
        list.add("a");
        list.add("d");
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
                    X.setChildren(stringList);
                    makeJsonList.add(X);
                    Collections.reverse(stringList);
                    for (MakeJson s : stringList){
                        if (!s.getName().equals("ε")) {
                            makeJsonStack.push(s);
                        }
                    }
                }
            }
//            System.out.println(stack + "\t\t\t\t\t\t\t\t\t\t" + list.subList(IP, list.size()));
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
