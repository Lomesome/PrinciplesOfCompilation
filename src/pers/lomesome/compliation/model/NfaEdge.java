package pers.lomesome.compliation.model;

public class NfaEdge {
    private State begin;
    private State end;
    private String label;

    public NfaEdge(State begin, String label, State end){
        this.begin = begin;
        this.label = label;
        this.end = end;
    }

    public State getBegin() {
        return begin;
    }

    public void setBegin(State begin) {
        this.begin = begin;
    }

    public State getEnd() {
        return end;
    }

    public void setEnd(State end) {
        this.end = end;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        label = label;
    }

    @Override
    public String toString() {
        return begin+"->"+end+"[label=\"" + label + "\"];";
    }
}
