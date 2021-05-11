package pers.lomesome.compliation.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class LiveStatu {
    private List<Quaternary> qt = new LinkedList<>();
    private List<Quaternary> ActiveLable = new LinkedList<>(); // 活跃信息表，和四元式一一对应
    private Stack<Pair<String, Integer>> SEM = new Stack<>(); // 语义栈

    public List<Quaternary> getQt() {
        return qt;
    }

    public void setQt(List<Quaternary> qt) {
        this.qt = qt;
    }

    public List<Quaternary> getActiveLable() {
        return ActiveLable;
    }

    public void setActiveLable(List<Quaternary> activeLable) {
        ActiveLable = activeLable;
    }

    public Stack<Pair<String, Integer>> getSEM() {
        return SEM;
    }

    public void setSEM(Stack<Pair<String, Integer>> SEM) {
        this.SEM = SEM;
    }
}
