package pers.lomesome.compliation.model;

import java.util.ArrayList;
import java.util.List;

public class NFA {

    /**
     * 该块NFA包含的边
     */
    private List<NfaEdge> nfaEdgeList = new ArrayList<>();
    /**
     * 开始状态
     */
    private State beginState;
    /**
     * 终结状态
     */
    private State endState;

    public List<NfaEdge> getEdgeList() {
        return nfaEdgeList;
    }

    public void setEdgeList(List<NfaEdge> nfaEdgeList) {
        this.nfaEdgeList = nfaEdgeList;
    }

    public State getBeginState() {
        return beginState;
    }

    public void setBeginState(State beginState) {
        this.beginState = beginState;
    }

    public State getEndState() {
        return endState;
    }

    public void setEndState(State endState) {
        this.endState = endState;
    }
}
