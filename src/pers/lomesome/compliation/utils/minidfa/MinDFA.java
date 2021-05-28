package pers.lomesome.compliation.utils.minidfa;

import pers.lomesome.compliation.model.DFA;
import pers.lomesome.compliation.model.Edge;
import pers.lomesome.compliation.model.Group;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 *【DFA的最小化】
 * 核心思路：
 *  1. 定义一个Group类，作为「分组」。
 *     Group有两个属性：groupID作为唯一标识；StateSet为该分组包含的状态集
 *  2. separate()方法的作用是，根据某个字母(letter)对分组集合(groupSet)进行彻底分裂
 *  3. 对于字母表中的每个字母，进行separate分裂
 *
 * 下面的论述有利于理解这个算法：
 *  1. HashMap做映射，是该算法的一个关键点。
 *     对于某个字母，一个分组(group)的所有状态(state)根据这个字母，用HashMap记录它们分别会被映射到哪个分组里，据此分裂。
 *     举个例子：{group1,[0, 1]}, {group2,[2, 3]}    (key是组，value是转化后指向该组的所有状态)
 *     ⬆ 0,1状态会转化到1组，2,3状态会转化到2组，据此，旧组分裂为了两个新组，然后删掉旧组，新组入队BFS
 *        如果这个哈希表的size==1，说明所有状态只能转化到一个组中，那么它们是等价的，不用删掉旧组，该组直接进入finalGroupSet最终分组
 *   2. groupID的作用是什么？为什么还要专门维护它？
 *      唯一标识。从1中看出，过程中不断进行着"删掉旧组，生成新组"的行为。维护这个ID主要是为了HashMap做映射
 *
 */
public class MinDFA {

    private int cnt = 0;		// 维护Group的唯一ID
    private DFA dfa;
    private List<Edge> edgeList;
    public MinDFA(DFA dfa, List<Edge> edgeList){
        this.dfa = dfa;
        this.edgeList = edgeList;
    }

    public Set<Group> getMinDFA() {
        List<Integer> K = dfa.getK();
        List<Integer> Z = dfa.getZ();
        String[][] f = dfa.getF();
        char[] letters = dfa.getLetters();

        K.removeAll(Z);         // 全部状态集K - 终态集Z = 非终态集
        Group groupx = new Group(cnt++, new LinkedHashSet<>(K));
        Group groupy = new Group(cnt++, new LinkedHashSet<>(Z));
        Set<Group> finalGroupSet = new LinkedHashSet<>();             // 最终分组
        Set<Group> curGroupSet;               // 此时的分组
        finalGroupSet.add(groupx);
        finalGroupSet.add(groupy);

        for (char letter : letters) {                                 // 对于每个字母
            curGroupSet = finalGroupSet;                            // 【最终分组】不断沦为【此时分组】
            finalGroupSet = separate(curGroupSet, letter, f);       // 【此时分组】又分裂成新的【最终分组】
        }                                                           // 所有字母都用了一次后，成为名副其实的【最终分组】

        return finalGroupSet;
    }

    private Set<Group> separate(Set<Group> groupSet, char letter, String[][] f){
        Set<Group> finalGroupSet = new LinkedHashSet<>();
        Queue<Group> queue = new LinkedList<>(groupSet);

        while (!queue.isEmpty()){
            Group oldGroup = queue.poll();
            Map<Group, List<Integer>> map = new LinkedHashMap<>();  //根据指向的组，对状态Integer进行分类
            for(Integer state : oldGroup.stateSet){
                Group stateNextBelong = beLong(state, letter, f, groupSet);
                if(!map.containsKey(stateNextBelong)){
                    map.put(stateNextBelong, new ArrayList<>());
                }
                map.get(stateNextBelong).add(state);
            }
            if (map.size() == 1){   // 如果这些状态映射到了一个状态集(Group)中，则为最终分组
                finalGroupSet.add(oldGroup);
            }else{                  // 如果这些状态映射到了多个状态集(Group)中，则删除原先分组，创建多个新分组，并将新分组入队
                groupSet.remove(oldGroup);
                for(List<Integer> list : map.values()){
                    Group newGroup = new Group(cnt++, new LinkedHashSet<>(list));
                    groupSet.add(newGroup);
                    queue.add(newGroup);
                }
            }
        }
        return finalGroupSet;
    }

    /**
     * move方法: 返回唯一后继状态（-1表示没有后继状态）
     */
    private int move(int state, char letter, String[][] f){
        for(int nextState = 0; nextState < f.length; nextState++){
            for(char c : f[state][nextState].toCharArray()){
                if(c == letter){
                    return nextState;
                }
            }
        }
        return -1;
    }

    /**
     * beLong方法: 某状态(state)经过字母(letter)一次转化(move)后，所属于的当前分组(group)
     */
    private Group beLong(int state, char letter, String[][] f, Set<Group> groupSet){
        int newState = move(state, letter, f);
        for(Group group : groupSet){
            if(group.stateSet.contains(newState)){
                return group;
            }
        }
        return null;
    }

    public List<String> getResult(){
        Set<String> dfaList = new LinkedHashSet<>();
        Set<Group> set = getMinDFA();
        for (Group group : set){
            System.out.println(group);
            for (Integer integer : group.stateSet){
                for (Edge nfaEdge : edgeList){
                    if (nfaEdge.getBegin().equals(String.valueOf(integer))){
                        dfaList.add(group.groupID  + "->" + find(set, (String) nfaEdge.getEnd()) + "[label=\"" + nfaEdge.getLabel() + "\"];");
                    }
                }
            }
        }
        return new ArrayList<>(dfaList);
    }

    private String find(Set<Group> set, String s){
        for (Group group : set){
            if (group.stateSet.contains(Integer.valueOf(s))){
                return String.valueOf(group.groupID);
            }
        }
        return null;
    }
}
