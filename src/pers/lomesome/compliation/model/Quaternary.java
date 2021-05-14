package pers.lomesome.compliation.model;

public class Quaternary {
    private Integer level;
    private Word First;
    private Word Second;
    private Word Third;
    private Word Fourth;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Word getFirst() {
        return First;
    }

    public void setFirst(Word first) {
        First = first;
    }

    public Word getSecond() {
        return Second;
    }

    public void setSecond(Word second) {
        Second = second;
    }

    public Word getThird() {
        return Third;
    }

    public void setThird(Word third) {
        Third = third;
    }

    public Word getFourth() {
        return Fourth;
    }

    public void setFourth(Word fourth) {
        Fourth = fourth;
    }

    @Override
    public String toString(){
        return " (" + First + "," + Second + "," + Third + "," + Fourth + ")";
    }
}