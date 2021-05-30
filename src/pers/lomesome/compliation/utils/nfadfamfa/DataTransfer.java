package pers.lomesome.compliation.utils.nfadfamfa;

import pers.lomesome.compliation.model.Edge;
import pers.lomesome.compliation.model.NFA;
import pers.lomesome.compliation.model.State;
import java.util.ArrayList;
import java.util.List;

public class DataTransfer {
    /**
     * 状态计数器
     */
    int count = 0;

    /**
     * 正则式包含的所有字符
     */
    List<Character> words = new ArrayList<>();

    /**
     * 用来保存NFA所有边的信息
     */
    List<Edge> nfaEdges = new ArrayList<>();

    /**
     * 用来保存DFA所有边的信息
     */
    List<Edge> dfaEdges = new ArrayList<>();

    /**
     * 用来保存MFA所有边的信息
     */
    List<Edge> mfaEdges = new ArrayList<>();

    /**
     * DFAI中的状态所包含的NFA中的状态
     */
    List<List<Integer>> DFAI = new ArrayList<>();

    /**
     * 用来保存NFA中所有的状态
     */
    List<State> statesOfNFA = new ArrayList<>();

    /**
     * 用来保存DFA中所有的状态
     */
    List<State> statesOfDFA = new ArrayList<>();

    /**
     * 用来保存MFA中所有的状态
     */
    List<State> statesOfMFA = new ArrayList<>();

    /**
     * DFA的开始状态集合
     */
    List<Integer> DFAstart = new ArrayList<>();

    /**
     * DFA的结束状态集合
     */
    List<Integer> DFAend = new ArrayList<>();

    /**
     * 构建的NFA
     */
    NFA nfa;

}
