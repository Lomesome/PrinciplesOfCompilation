package pers.lomesome.compliation.model;

public class Quaternary {
    private Integer level;
    private String First;
    private String Second;
    private String Third;
    private String Fourth;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getFirst() {
        return First;
    }

    public void setFirst(String first) {
        First = first;
    }

    public String getSecond() {
        return Second;
    }

    public void setSecond(String second) {
        Second = second;
    }

    public String getThird() {
        return Third;
    }

    public void setThird(String third) {
        Third = third;
    }

    public String getFourth() {
        return Fourth;
    }

    public void setFourth(String fourth) {
        Fourth = fourth;
    }

    @Override
    public String toString(){
        return " (" + First + "," + Second + "," + Third + "," + Fourth + ")";
    }
}