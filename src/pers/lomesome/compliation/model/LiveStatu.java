package pers.lomesome.compliation.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class LiveStatu {
    private List<Quaternary> qt = new LinkedList<>();
    private Stack<Pair<Word, Integer>> SEM = new Stack<>(); // 语义栈
    private Stack<String[]> SIGNHEAD = new Stack<>();
    private Stack<Pair<Word, Integer>> argsStack= new Stack<>();
    private LinkedList<Quaternary> ActiveLable = new LinkedList<Quaternary>(); // 活跃信息表，和四元式一一对应

    public List<Quaternary> getQt() {
        return qt;
    }

    public void setQt(List<Quaternary> qt) {
        this.qt = qt;
    }

    public Stack<Pair<Word, Integer>> getSEM() {
        return SEM;
    }

    public void setSEM(Stack<Pair<Word, Integer>> SEM) {
        this.SEM = SEM;
    }

    public Stack<String[]> getSIGNHEAD() {
        return SIGNHEAD;
    }

    public void setSIGNHEAD(Stack<String[]> SIGNHEAD) {
        this.SIGNHEAD = SIGNHEAD;
    }

    public Stack<Pair<Word, Integer>> getArgsStack() {
        return argsStack;
    }

    public void setArgsStack(Stack<Pair<Word, Integer>> argsStack) {
        this.argsStack = argsStack;
    }

    public LinkedList<Quaternary> getActiveLable() {
        return ActiveLable;
    }

    public void setActiveLable(LinkedList<Quaternary> activeLable) {
        ActiveLable = activeLable;
    }
}
