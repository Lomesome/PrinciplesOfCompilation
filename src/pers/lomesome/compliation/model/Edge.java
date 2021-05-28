package pers.lomesome.compliation.model;

public class Edge {
    /**
     * 开始状态
     */
    private Object begin;
    /**
     * 终结状态
     */
    private Object end;
    /**
     * 接收字符
     */
    private String label;

    public Edge(Object begin, String label, Object end){
        this.begin = begin;
        this.label = label;
        this.end = end;
    }

    public Object getBegin() {
        return begin;
    }

    public void setBegin(Object begin) {
        this.begin = begin;
    }

    public Object getEnd() {
        return end;
    }

    public void setEnd(Object end) {
        this.end = end;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return begin+"->"+end+"[label=\"" + label + "\"];";
    }
}
