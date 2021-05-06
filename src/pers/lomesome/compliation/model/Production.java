package pers.lomesome.compliation.model;

import java.util.List;

public class Production {

    public int no;
    public String left;
    public String[] right;
    public List<String> select;

    public String getError() {
        return null;
    }

    public String getSolution() {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder(left + " ->");

        if (right.length == 0)
            ret.append(" ε");

        for (String s : right) {
            ret.append(" ").append(s);
        }

        return ret.toString();
    }

    public List<String> getSelect() {
        return select;
    }

    public void setSelect(List<String> select) {
        this.select = select;
    }

    public int getNo() {
        return no;
    }

    public String getLeft() {
        return left;
    }

    public String[] getRight() {
        return right;
    }

    public Production() {
    }

    public Production(int no, String left) {
        this.no = no;
        this.left = left;
        this.right = new String[0];
    }

    public Production(int no, String left, String[] symbols) {
        this.no = no;
        this.left = left;
        this.right = symbols;
    }
}
