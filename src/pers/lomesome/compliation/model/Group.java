package pers.lomesome.compliation.model;

import java.util.Set;

public class Group {
    /**
     * 该组的唯一ID
     */
    public int groupID;
    /**
     * 该组所包含的状态集
     */
    public Set<Integer> stateSet;

    public Group(int groupID, Set<Integer> stateSet) {
        this.groupID = groupID;
        this.stateSet = stateSet;
    }

    @Override
    public String toString() {
        return "Group{" +
                "groupID=" + groupID +
                ", stateSet=" + stateSet +
                '}';
    }
}