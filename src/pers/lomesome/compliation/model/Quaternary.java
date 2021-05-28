package pers.lomesome.compliation.model;

public class Quaternary {
    private int id = 0;
    private int level;
    private Word First;
    private Word Second;
    private Word Third;
    private Word Fourth;

    public Quaternary(){}

    public Quaternary(Word First, Word Second, Word Third, Word Fourth){
        this.First = First;
        this.Second = Second;
        this.Third = Third;
        this.Fourth = Fourth;
    }

    public Quaternary(int id, Word First, Word Second, Word Third, Word Fourth){
        this.id = id;
        this.First = First;
        this.Second = Second;
        this.Third = Third;
        this.Fourth = Fourth;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
        return  id + " (" + First + "," + Second + "," + Third + "," + Fourth + ")";
    }
}