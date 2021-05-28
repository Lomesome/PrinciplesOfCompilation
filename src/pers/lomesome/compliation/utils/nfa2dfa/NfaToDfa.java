package pers.lomesome.compliation.utils.nfa2dfa;

import pers.lomesome.compliation.model.DfaEdge;
import pers.lomesome.compliation.model.Edge;
import pers.lomesome.compliation.model.NfaEdge;
import pers.lomesome.compliation.model.State;
import java.util.*;

public class NfaToDfa {
    private final List<DfaEdge> DFA = new ArrayList<>();
    private List<Edge> edgeList = new ArrayList<>();
    private final NFAUtil nfaUtil ;
    private final int start;
    private final String[] strings;
    private Map<String, Integer> map = new LinkedHashMap<>();
    private Set<String> vt;

    public NfaToDfa(List<NfaEdge> nfaNfaEdgeList, int start, String[] strings){
        this.nfaUtil = new NFAUtil(nfaNfaEdgeList);
        this.start = start;
        this.strings = strings;
    }

    public void getNewStatus() {
        Set<Set<String>> res = new HashSet<>();
        Set<String> startPointEpsilonClosure = nfaUtil.getEpsilonClosure(String.valueOf(start));   //起始点的 ε 闭包
        res.add(startPointEpsilonClosure);  //将起始点的 ε 闭包加到结果中
        getNewStatus(res, startPointEpsilonClosure);  //指定路径
    }

    private void getNewStatus(Set<Set<String>> res,Set<String>sourcePoints) {

        for (String path : strings) {  //如果当前获得的路径，在之前没有出现过
            Set<String> onePathEpsilonSet = new HashSet<>();   //定义某一路径上的 ε 闭包
            Set<String> move = nfaUtil.getMove(sourcePoints, path);  //指定路径上的可达点
            for (String s : move) {
                Set<String> tmpSet = nfaUtil.getEpsilonClosure(s);  //获取当前可达点的 ε 闭包
                onePathEpsilonSet.addAll(tmpSet);  //将当前可达点的 ε 闭包,放在指定路径的集合里
            }

            DFA.add(new DfaEdge(sourcePoints, path, onePathEpsilonSet));

            if (!res.contains(onePathEpsilonSet) && onePathEpsilonSet.size() > 0) { //如果当前获得的路径，在之前没有出现过
                res.add(onePathEpsilonSet);  //添加当前集合
                getNewStatus(res, onePathEpsilonSet); //递归
            }
        }
    }

    public List<String> getResults(State endState){
        int ID = 0;
        vt = new LinkedHashSet<>();
        getNewStatus();
        Set<String> list = new LinkedHashSet<>();
        edgeList = new ArrayList<>();
        for (DfaEdge dfaEdge : DFA) {
            String start = dfaEdge.getBegin().toString();
            String end = dfaEdge.getEnd().toString();
            String label = dfaEdge.getLabel();
            if (end.length()  == 2)
                continue;
            if (map.get(start) == null){
                map.put(start, ID++);
                start = String.valueOf(ID - 1);
            }else {
                start = String.valueOf(map.get(start));
            }
            if (dfaEdge.getBegin().contains(endState.getStateName())){
                vt.add(start);
            }
            if (map.get(end)==null){
                map.put(end, ID++);
                end = String.valueOf(ID - 1);
            }else {
                end = String.valueOf(map.get(end));
            }
            if (dfaEdge.getEnd().contains(endState.getStateName())){
                vt.add(end);
            }
            edgeList.add(new Edge(start, label, end));
            list.add(new Edge(start, label, end).toString());
            System.out.println(dfaEdge);
        }
        vt.forEach(s -> list.add(s + " [shape=doublecircle];"));
        return new ArrayList<>(list);
    }

    public List<String> getStateSet(){
        return new ArrayList<>(map.keySet());
    }

    public List<Edge> getEdgeList() {
        return edgeList;
    }

    public List<Integer> getMap() {
        return new ArrayList<>(map.values());
    }

    public int getStart() {
        return 4;
    }

    public List<Integer> getVt() {
        List<Integer> list = new ArrayList<>();
        vt.forEach(s -> {
            list.add(Integer.parseInt(s));
        });
        return list;
    }

    public int getStateSize(){
        return map.keySet().size();
    }

}
