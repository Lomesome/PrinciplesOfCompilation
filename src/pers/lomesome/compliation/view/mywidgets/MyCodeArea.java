package pers.lomesome.compliation.view.mywidgets;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import pers.lomesome.compliation.controller.ReadAndWriteFile;
import java.io.File;
import java.util.function.IntFunction;

public class MyCodeArea extends CodeArea {
    public MyCodeArea(File file){
        this.appendText(ReadAndWriteFile.readFileContent(file.getPath()));  //读取文本添加到CodeArea面板
        this.setLineHighlighterFill(new Color(0.95f,0.9f,0.1f,0.1));
        this.setLineHighlighterOn(true);
        IntFunction<Node> numberFactory = LineNumberFactory.get(this);

        this.setUserData(file);
        IntFunction<Node> graphicFactory = line -> {
            System.out.println(line);
            Circle circle = new Circle(4);
            circle.setFill(Color.RED);
            circle.setStroke(Color.RED);
            circle.setVisible(false);
            HBox hbox = new HBox(25, numberFactory.apply(line), circle);
            hbox.setStyle("-fx-background-color: #dedede");
            hbox.setPrefWidth(80);
            hbox.setAlignment(Pos.CENTER_LEFT);
            hbox.setCursor(Cursor.DEFAULT);
            hbox.setOnMouseClicked(event -> circle.setVisible(!circle.isVisible()));
            return hbox;
        };
        this.setParagraphGraphicFactory(graphicFactory);
    }
}
