package pers.lomesome.compliation.model;

import java.util.Set;

public class DfaEdge {

    /**
     * 开始状态集
     */
    private final Set<String> begin;
    /**
     * 终结状态集
     */
    private final Set<String> end;
    /**
     * 接收字符
     */
    private final String label;

    public DfaEdge(Set<String> begin, String label, Set<String> end) {
        this.begin = begin;
        this.label = label;
        this.end = end;
    }

    public Set<String> getBegin() {
        return begin;
    }

    public Set<String> getEnd() {
        return end;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return begin.toString()+" -"+ label +"->  "+ end.toString();
    }
}
