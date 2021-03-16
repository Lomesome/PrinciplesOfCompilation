package pers.lomesome.compliation.view.mywidgets;

import javafx.scene.control.Tab;
import org.fxmisc.flowless.VirtualizedScrollPane;
import java.io.File;

public class MyTab extends Tab {

    public MyTab(String name){
        this.setText(name);
    }

    public void setNode(File myFile){
        this.setUserData(myFile);
        this.setContent(new VirtualizedScrollPane(new MyCodeArea(myFile)));
    }
}
