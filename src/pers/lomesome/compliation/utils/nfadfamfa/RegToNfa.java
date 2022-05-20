package pers.lomesome.compliation.utils.nfadfamfa;

import pers.lomesome.compliation.model.Edge;
import pers.lomesome.compliation.model.NFA;
import pers.lomesome.compliation.model.State;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class RegToNfa {

    DataTransfer dataTransfer;

    public RegToNfa(DataTransfer dataTransfer){
        this.dataTransfer = dataTransfer;
    }

    public List<String> getNFAs(String s) {
        Rpn rpn = new Rpn();
        s = rpn.reverse2Rpn(s);
        dataTransfer.words = rpn.getSymbol(s);
        dataTransfer.nfa = makeNFA(s);
        getNFAInfor(dataTransfer.nfa.getStart());
        List<String> results = new ArrayList<>();
        dataTransfer.nfaEdges.forEach(edge -> {
            results.add(edge.toString());
        });
        return results;
    }

    //Reg->NFA
    private NFA makeNFA(String s) {
        Stack<pers.lomesome.compliation.model.NFA> nfa = new Stack<>();//nfa的栈
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '|') {
                State start = new State(dataTransfer.count++); //开始状态
                State end = new State(dataTransfer.count++);  //终结状态
                NFA now = new NFA(nfa.pop(), nfa.pop(), start, end);  //弹出两个nfa
                nfa.add(now);  //加入栈
                dataTransfer.nfaStates.add(now.getStart());
                dataTransfer.nfaStates.add(now.getEnd());
            } else if (s.charAt(i) == '*') {//取闭包
                State start = new State(dataTransfer.count++); //开始状态
                State end = new State(dataTransfer.count++);  //终结状态
                NFA now = new NFA(nfa.pop(), start, end);
                nfa.add(now);//加入栈
                dataTransfer.nfaStates.add(now.getStart());
                dataTransfer.nfaStates.add(now.getEnd());
            } else if (s.charAt(i) == '.') {//取链接
                NFA now = new NFA(nfa.pop(), nfa.pop());
                nfa.add(now);
            } else {//此时单边
                State start = new State(dataTransfer.count++); //开始状态
                State end = new State(dataTransfer.count++); //终结状态
                NFA now = new NFA(start, end, s.charAt(i));
                nfa.add(now);//创造一个nfa加入栈
                dataTransfer.nfaStates.add(now.getStart());
                dataTransfer.nfaStates.add(now.getEnd());
            }
        }
        return nfa.pop();
    }

    /**
     * 得到nfa的信息
     * @param start
     */
    private void getNFAInfor(State start) {
        //输出当前节点的所有后继
        List<Character> subChar = start.getSubChar();
        List<State> subState = start.getNext();
        start.setVisit();//设置为已访问
        for (int i = 0; i < subChar.size(); i++) {
            dataTransfer.nfaEdges.add(new Edge("" + start.getNum(), "" + subChar.get(i), "" + subState.get(i).getNum()));//当前状态+接受符号+下状态编号
            if (subState.get(i).getVisit() == 0) {//下一节点未被访问过，递归访问
                getNFAInfor(subState.get(i));
            }
        }
    }
}
