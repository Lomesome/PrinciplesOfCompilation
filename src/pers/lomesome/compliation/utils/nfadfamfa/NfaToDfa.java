package pers.lomesome.compliation.utils.nfadfamfa;

import pers.lomesome.compliation.model.Edge;
import pers.lomesome.compliation.model.State;
import java.util.*;

public class NfaToDfa {

    DataTransfer dataTransfer;

    public NfaToDfa(DataTransfer dataTransfer) {
        this.dataTransfer = dataTransfer;
    }

    public List<String> getDFAs() {
        makeDFA();
        List<String> results = new ArrayList<>();
        dataTransfer.dfaEdges.forEach(edge -> results.add(edge.toString()));
        return results;
    }

    //NFA->DFA
    private void makeDFA() {
        Queue<List<Integer>> Ique = new LinkedList<>();  //定义队列
        List<List<Integer>> table = new ArrayList<>();  //表
        State start = new State(dataTransfer.count++);  //给NFA加上新的开始状态和结束状态
        State end = new State(dataTransfer.count++);
        start.setNext(dataTransfer.nfa.getStart(), 'ε');
        dataTransfer.nfa.getEnd().setNext(end, 'ε'); //连接
        dataTransfer.statesOfNFA.add(start);
        dataTransfer.statesOfNFA.add(end);  //加入现有状态集合
        List<Integer> s = getEncloure(start);  //求开始状态的闭包
        dataTransfer.DFAI.add(s); //加入已有NFA
        Ique.add(s);  //加入队列
        while (!Ique.isEmpty()) {  //开始遍历,直到没有新状态
            s = Ique.poll();//出一个状态
            table.add(s);//表格第一列
            for (Character word : dataTransfer.words) {
                List<Integer> now = getHuDiversion(s, word);  //弧转换
                table.add(now);
                if (isNewDFAState(now) && !now.isEmpty()) {  //如果是一个新状态
                    dataTransfer.DFAI.add(now); //加入状态集
                    Ique.add(now); //加入队列
                }
            }
        }
        //得到状态集
        for (int i = 0; i < dataTransfer.DFAI.size(); i++) { //访问定义列的第一个
            State state = new State(i); //新建状态
            if (i == 0) { //那么当前状态为dfa的开始状态
                dataTransfer.DFAstart.add(state.getNum());
                state.setStart();
            }
            dataTransfer.statesOfDFA.add(state);
        }
        //链接状态
        //判断第一个状态是不是终态
        List<Integer> startInfor = table.get(0);  //得到开始状态
        State startState = getNextState(startInfor, table);
        if (startInfor.contains(dataTransfer.nfa.getEnd().getNum()) && !startState.getIsEnd()) {//当前状态包含nfa的终态，并且还未被设置成终态过
            dataTransfer.DFAend.add(startState.getNum());
            startState.setEnd(true);  //设为DFA的终态
        }
        for (int i = 0; i < dataTransfer.statesOfDFA.size(); i++) {
            for (int j = 0; j < dataTransfer.words.size(); j++) {
                char subchar = dataTransfer.words.get(j);
                List<Integer> nextStateInfor = table.get(i * (dataTransfer.words.size() + 1) + j + 1);  //得到状态信息
                if (nextStateInfor.size() == 0)
                    continue;
                State nextState = getNextState(nextStateInfor, table);
                if (nextStateInfor.contains(dataTransfer.nfa.getEnd().getNum()) && !nextState.getIsEnd()) { //当前状态包含nfa的终态，并且还未被设置成终态过
                    dataTransfer.DFAend.add(nextState.getNum());
                    nextState.setEnd(true); //设为DFA的终态
                }
                dataTransfer.statesOfDFA.get(i).setNext(nextState, subchar);
                dataTransfer.dfaEdges.add(new Edge("" + dataTransfer.statesOfDFA.get(i).getNum(), "" + subchar, "" + nextState.getNum()));
            }
        }
    }

    //得到DNF下一个状态
    public State getNextState(List<Integer> state, List<List<Integer>> table) {
        for (int i = 0; i < dataTransfer.statesOfDFA.size(); i++) {
            int count = 0;
            List<Integer> s = table.get(i * (dataTransfer.words.size() + 1));
            if (s.size() != state.size())
                continue;
            for (int j = 0; j < s.size(); j++)
                if (s.contains(state.get(j)))
                    count++;
            if (count == s.size())
                return dataTransfer.statesOfDFA.get(i);
        }
        return new State(-1);
    }

    /**
     * 判断当前状态是否是新状态在dfai中
     *
     * @param state 当前状态
     * @return boolean
     */
    public boolean isNewDFAState(List<Integer> state) {
        for (List<Integer> s : dataTransfer.DFAI) {
            int count = 0;
            if (s.size() != state.size())
                continue;
            for (Integer integer : state) {
                if (s.contains(integer))
                    count++;
            }
            if (count == state.size())
                return false;
        }
        return true;
    }

    /**
     * 得到弧转换
     *
     * @param states 状态
     * @param c      接收字符
     * @return List<Integer>
     */
    private List<Integer> getHuDiversion(List<Integer> states, char c) {
        List<Integer> r = new ArrayList<>();
        for (Integer item : states) { //对states中的每一个状态
            State state = getStateFromNum(item); //求这个状态
            List<Integer> move_C = getMove(state, c); //再得到Move的闭包
            for (Integer value : move_C) {
                State s = getStateFromNum(value); //对应状态
                List<Integer> enclose = getEncloure(s);
                for (Integer integer : enclose) {
                    if (!r.contains(integer))
                        r.add(integer);
                }
            }
        }
        return r;
    }

    //Move
    private List<Integer> getMove(State state, char c) {
        List<Integer> r = new ArrayList<>();
        Stack<State> stack = new Stack<>();
        stack.add(state);
        while (!stack.isEmpty()) {
            state = stack.pop();
            List<Character> subChar = state.getSubChar(); //下一个状态
            List<State> subState = state.getNext(); //子状态集
            for (int i = 0; i < subChar.size(); i++) {
                if (subChar.get(i) == c && !r.contains(subState.get(i).getNum())) {//当前经过'c'就能到达下一状态，且r集合里没有包括过
                    r.add(subState.get(i).getNum());
                }
            }
        }
        return r;
    }

    /**
     * 得到当前编号在NFA中对应状态
     *
     * @param num 编号
     * @return 在NFA中对应状态
     */
    private State getStateFromNum(int num) {
        State r = new State(0);
        for (State state : dataTransfer.statesOfNFA) {
            r = state;
            if (r.getNum() == num)
                return r;
        }
        return r;
    }

    /**
     * 求当前状态的闭包
     *
     * @param s 当前状态
     * @return 闭包列表
     */
    private List<Integer> getEncloure(State s) {
        List<Integer> r = new ArrayList<>();
        r.add(s.getNum());
        Stack<State> states = new Stack<>();
        states.add(s);
        while (!states.isEmpty()) {
            s = states.pop();
            List<Character> subChar = s.getSubChar();  //下一个状态
            List<State> subState = s.getNext();  //子状态集
            for (int i = 0; i < subChar.size(); i++) {
                if (subChar.get(i) == 'ε' && !r.contains(subState.get(i).getNum())) {  //当前经过ε就能到达下一状态，且r集合里没有包括过
                    r.add(subState.get(i).getNum());
                    states.add(subState.get(i));
                }
            }
        }
        return r;
    }
}
