package pers.lomesome.compliation.model;

import pers.lomesome.compliation.tool.finalattr.FinalAttribute;
import java.io.Serializable;

public class Word implements Serializable {
    private int token;    //种别码
    private String word;    //扫描得到的词
    private String type;    //类别
    private String name;
    public String value;
    private int row;
    private int col;

    public Word() { }

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

    public Word(String name, String source) {
        this.name = name;
        this.word = source;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

    @Override
    public String toString() {
        return word;
    }
}