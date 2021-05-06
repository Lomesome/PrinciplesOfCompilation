package pers.lomesome.compliation.utils.grammatical;

import pers.lomesome.compliation.tool.filehandling.ReadAndWriteFile;
import java.util.*;

public class NewAnalysis {

    private final Stack<String> makeJsonStack = new Stack<>();
    private LinkedHashMap<String, LinkedHashMap<String, List<String>>> map;
    private final Set<String> allVn = new LinkedHashSet<>();
    private final Set<String> allVt = new LinkedHashSet<>();
    private AllGrammer allGrammer;
    private Map<String, Set<String>> singlefirstmap;
    private Map<String, Set<String>> followmap;
    private boolean errorflag = false;

    public void first_follow() {
        String content = ReadAndWriteFile.readFileContent("/Users/leiyunhong/IdeaProjects/PrinciplesOfCompilation/src/resources/grammar/test2.txt"); //C语言官方文法
        GrammaticalHandle grammaticalHandle = new GrammaticalHandle(content);
        allGrammer = new AllGrammer(grammaticalHandle.grammaticlHandel());

        EliminateLR eliminateLR = new EliminateLR(allGrammer);
        eliminateLR.eliminate();
        EliminateBT eliminateBT = new EliminateBT(allGrammer);
        eliminateBT.eliminate();
        eliminateBT.eliminate();
        allGrammer.getGrammarMap().forEach((k, v) -> {
            System.out.println(k + "->" + v);
        });

        allGrammer.getGrammarMap().forEach((k, v) -> {
            allVn.add(k);
        });
        allGrammer.getGrammarMap().forEach((k, v) -> {
            for (List<String> stringList : v) {
                allVt.addAll(stringList);
            }
        });
        allVt.removeAll(allVn);
        allVt.remove("ε");

        FirstSet firstSet = new FirstSet(allGrammer.getGrammarMap());

        Map<String, Set<String>> firstmap = firstSet.getFirstSet();
        firstmap.forEach((k, v) -> {
            System.out.println("First(" + k + ")=" + v);
        });

        SingleFirst singleFirst = new SingleFirst(allGrammer.getGrammarMap());
        singlefirstmap = singleFirst.getFirstSet();
        singlefirstmap.forEach((k, v) -> {
            System.out.println("First(" + k + ")=" + v);
        });
        FollowSet followSet = new FollowSet(allGrammer.getGrammarMap(), firstmap);
        followmap = followSet.getFollowSet();
        followmap.forEach((k, v) -> {
            System.out.println("Follow(" + k + ")=" + v);
        });
    }

    public void makePredict() {
        NewPredict predict = new NewPredict(allGrammer, singlefirstmap, followmap);
        map = predict.predictTable();
        map.forEach((k, v) -> {
            System.out.println(k + " " + v);
        });
    }

    public void analysis(List<String> list) {
        makeJsonStack.push("#");
        makeJsonStack.push((String) allGrammer.getGrammarMap().keySet().toArray()[0]);
        System.out.println(makeJsonStack);
        list.add("#");
        int IP = 0;
        String a = list.get(IP);
        String X = makeJsonStack.pop();
        while (!X.equals("#")) {
            if (X.equals(a)) {
                IP++;
                a = list.get(IP);
            } else if (allVt.contains(X)) {
                errorflag = true;
                System.out.println("error");
            } else if (allVn.contains(X)) {
                if (map.get(X).get(a).size() == 0) {
                    System.out.println("error");
                    errorflag = true;
                    IP++;
                    a = list.get(IP);
                    continue;
                } else if (map.get(X).get(a).get(0).equals("synch")) {
                    System.out.println("error");
                    errorflag = true;
                    IP++;
                    a = list.get(IP);
                    continue;
                } else {
                    List<String> stringList = map.get(X).get(a);
                    List<String> newStringList = new ArrayList<>(stringList.subList(0, stringList.size()));
                    Collections.reverse(newStringList);
                    for (String s : newStringList) {
                        if (!s.equals("ε")) {
                            makeJsonStack.push(s);
                        }
                    }
                }
            }
            System.out.println(makeJsonStack + " " + list.subList(IP, list.size()));
            X = makeJsonStack.pop();
        }
        if (list.size() - 1 != IP || errorflag) {
            System.out.println("该语句不符合该文法");
        } else {
            System.out.println("该语句符合该文法");
        }

    }
}
