package pers.lomesome.compliation.model;

public class Quaternary {
    private int id = 0;
    private int level;
    private Word operator;
    private Word arg1;
    private Word arg2;
    private Word result;

    public Quaternary(){}

    public Quaternary(Word operator, Word arg1, Word arg2, Word result){
        this.operator = operator;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.result = result;
    }

    public Quaternary(int id, Word operator, Word arg1, Word arg2, Word result){
        this.id = id;
        this.operator = operator;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.result = result;
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

    public Word getOperator() {
        return operator;
    }

    public void setOperator(Word operator) {
        this.operator = operator;
    }

    public Word getArg1() {
        return arg1;
    }

    public void setArg1(Word arg1) {
        this.arg1 = arg1;
    }

    public Word getArg2() {
        return arg2;
    }

    public void setArg2(Word arg2) {
        this.arg2 = arg2;
    }

    public Word getResult() {
        return result;
    }

    public void setResult(Word result) {
        this.result = result;
    }

    @Override
    public String toString(){
        return  id + " (" + operator + "," + arg1 + "," + arg2 + "," + result + ")";
    }
}