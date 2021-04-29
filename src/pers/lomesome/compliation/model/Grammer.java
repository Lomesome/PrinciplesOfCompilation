package pers.lomesome.compliation.model;

import java.util.List;

public class Grammer {
    private String vn;
    private List<String> replaceList;

    public Grammer(String vn, List<String> replaceList){
        this.vn = vn;
        this.replaceList = replaceList;
    }

    public String getVn() {
        return vn;
    }

    public void setVn(String vn) {
        this.vn = vn;
    }

    public List<String> getReplaceList() {
        return replaceList;
    }

    public void setReplaceList(List<String> replaceList) {
        this.replaceList = replaceList;
    }

    @Override
    public String toString(){
        return vn + "->" + replaceList;
    }
}
