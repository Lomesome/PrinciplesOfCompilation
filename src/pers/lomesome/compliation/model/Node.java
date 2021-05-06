package pers.lomesome.compliation.model;

import java.util.*;

public class Node {
    String symbol_name;
    Node father;
    public List<Node> sons;
    public Map<String, String> attribute;

    public Node(String symbolName, Node father) {
        super();
        symbol_name = symbolName;
        this.father = father;
        sons = new ArrayList<>();
        attribute = new LinkedHashMap<>();
    }

    public void setSons(List<Node> sons) {
        this.sons = sons;
    }

    public String getSymbol_name() {
        return symbol_name;
    }

    public Node getFather() {
        return father;
    }
}
