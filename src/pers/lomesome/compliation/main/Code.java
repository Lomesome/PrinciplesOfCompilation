package pers.lomesome.compliation.main;


import javafx.scene.control.TextArea;

public class Code extends TextArea {

    public Code(String s){
        this.setText(s);

        System.out.println(this.getContent());
    }
}
