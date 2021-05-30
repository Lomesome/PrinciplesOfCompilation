package pers.lomesome.compliation.model;

import java.util.ArrayList;
import java.util.List;

public class State {
    /**
     * 当前状态编号
     */
    private int num;

    /**
     * 后继状态的编号
     */
    private List<Integer> SubState = new ArrayList<>();

    /**
     * 读到某个字符转换状态
     */
    private List<Character> SubChar = new ArrayList<>();

    /**
     * NFA图中的后序状态
     */
    private List<State> sub = new ArrayList<>();

    /**
     * 代表当前未被访问过
     */
    private int visit = 0;

    /**
     * 初始化不是终态//用于DFA
     */
    private boolean isEnd = false;

    /**
     * 是不是开始状态
     */
    private boolean isStart = false;

    public State(int num) {
        this.num = num;
    }

    /**
     * 图中链接下一个
     * @param s
     * @param c
     */
    public void setNext(State s, char c) {
        SubChar.add(c);
        sub.add(s);
    }

    public State getNext(char c) {
        for (int i = 0; i < SubChar.size(); i++) {
            if (SubChar.get(i) == c)
                return sub.get(i);
        }
        return new State(-1);
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    public boolean getIsEnd(){
        return isEnd;
    }

    public void setStart() {
        isStart = true;
    }

    public boolean IsStart() {
        return isStart;
    }

    public List<Character> getSubChar() {
        return this.SubChar;
    }

    public List<Integer> getSubState() {
        return this.SubState;
    }

    public List<State> getNext() {
        return sub;
    }

    public void setVisit() {
        visit = 1;
    }

    public int getVisit() {
        return visit;
    }

    public int getNum() {
        return this.num;
    }

}
