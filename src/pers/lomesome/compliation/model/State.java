package pers.lomesome.compliation.model;

import java.util.Objects;

public class State {
    private String stateName;
    public static int ID = 0;

    public State(){
        this.stateName = String.valueOf(ID++);
    }

    public State(String stateName){
        this.stateName = stateName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return Objects.equals(stateName, state.stateName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stateName);
    }

    @Override
    public String toString() {
        return stateName;
    }
}
