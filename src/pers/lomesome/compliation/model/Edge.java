package pers.lomesome.compliation.model;

public class Edge {

    private String begin; //开始
    private String label; //接收字符
    private String end; //终结

    public Edge(String begin, String label, String end) {//NFADFAMFA
        this.begin = begin;
        this.label = label;
        this.end = end;
    }

    public String toString() {
        return begin + "->" + end + "[label=\"" + label + "\"];";
    }

    public String getBegin() {
        return begin;
    }

    public String getLabel() {
        return label;
    }

    public String getEnd() {
        return end;
    }

}
