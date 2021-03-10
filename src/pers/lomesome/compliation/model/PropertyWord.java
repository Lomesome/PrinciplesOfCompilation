package pers.lomesome.compliation.model;

import javafx.beans.property.SimpleStringProperty;

public class PropertyWord {

    private final SimpleStringProperty classify;
    private final SimpleStringProperty token;
    private final SimpleStringProperty word;

    public PropertyWord(String classify, String token, String word) {
        this.classify = new SimpleStringProperty(classify);
        this.token = new SimpleStringProperty(token);
        this.word = new SimpleStringProperty(word);
    }

    public String getClassify() {
        return classify.get();
    }

    public void setClassify(String c) {
        classify.set(c);
    }

    public String getToken() {
        return token.get();
    }

    public void setToken(String t) {
        token.set(t);
    }

    public String getWord() {
        return word.get();
    }

    public void setWord(String w) {
        word.set(w);
    }
}