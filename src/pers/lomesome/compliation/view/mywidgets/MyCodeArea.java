package pers.lomesome.compliation.view.mywidgets;

import javafx.application.Platform;
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
        Platform.runLater(()->{
            this.setLineHighlighterFill(Color.LIGHTGOLDENRODYELLOW);
            this.setLineHighlighterOn(true);
        });
        this.setUserData(file);
        IntFunction<Node> numberFactory = LineNumberFactory.get(this);
        IntFunction<Node> graphicFactory = line -> {
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

    public void moveToSelect(int row, int col){
        this.moveTo(row - 1, col - 1);
        this.requestFocus();
        this.showParagraphAtTop(row - 10);
    }
}
