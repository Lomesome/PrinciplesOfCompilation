package pers.lomesome.compliation.model;

import pers.lomesome.compliation.tool.finalattr.FinalAttribute;
import java.io.Serializable;

public class Word implements Serializable {
    private int token;    //种别码
    private String word;    //扫描得到的词
    private String type;    //类别
    private String name;    //终结符名称
    private String semSymbol = "";    //语义符号
    private int row;
    private int col;

    public Word() { }

    public Word(String word) {
        this.word = word;
        this.name = "";
    }

    public Word(String word, String name) {
        this.word = word;
        this.name = name;
    }

    public Word(String word, String type, int row, int col) {
        this.token = FinalAttribute.findToken(word);
        this.word = word;
        this.type = type;
        this.row = row;
        this.col = col;
    }

    public Word(int token, String word, String type, int row, int col) {
        this.token = token;
        this.word = word;
        this.type = type;
        this.row = row;
        this.col = col;
    }

    public int getTypenum() {
        return token;
    }

    public void setTypenum(int token) {
        this.token = token;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setSemSymbol(String semSymbol) {
        this.semSymbol = semSymbol;
    }

    public String getSemSymbol() {
        return semSymbol;
    }

    @Override
    public String toString() {
        return word;
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof  Word){
            Word word = (Word) o;
            return ((this.word.equals(word.word)));
        }
        return false;
    }
}