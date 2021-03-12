package pers.lomesome.compliation.view.mywidgets;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.List;

public class NewCodeArea extends ScrollPane {
    private final List<HBox> rowBoxList = new ArrayList<>();
    private final VBox codeBox = new VBox();
    private int line = 0;
    public NewCodeArea(){
        this.setStyle("-fx-background-color: transparent");
        this.setContent(codeBox);
        HBox row = makeRowBox();
        codeBox.getChildren().add(row);

    }

    public NewCodeArea(String content){

    }

    public void setText(String text){

    }

    private HBox makeRowBox(){
        line += 1;
        Label lineLabel = new Label(String.valueOf(line));
        lineLabel.setStyle("-fx-background-color: #bea6a6");
        lineLabel.setPrefWidth(70);
        lineLabel.setPadding(new Insets(0,0,0,10));
        TextField textField = new TextField();
        textField.textProperty().addListener((a, o, n) -> {
            if(n.length() > 0)
            textField.setPrefWidth(n.length()*textField.getFont().getSize()/ 2);
        });
        textField.setStyle("-fx-background-color: transparent;-fx-padding: 0");
        textField.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                HBox row = makeRowBox();
                codeBox.getChildren().add(row);
                row.getChildren().get(1).requestFocus();
            }else if(event.getCode() == KeyCode.BACK_SPACE){
                if(line > 1) {
                    int lastline = Integer.parseInt(lineLabel.getText()) - 1;
                    line -= 1;
                    rowBoxList.remove(lastline);
                    rowBoxList.get(lastline - 1).getChildren().get(1).requestFocus();
                    codeBox.getChildren().remove(lastline);
                }
            }
        });
        HBox row = new HBox(lineLabel, textField);
        row.setOnMouseClicked(event -> textField.requestFocus());
        row.setStyle("-fx-padding: 0");
        row.setAlignment(Pos.CENTER_LEFT);
        rowBoxList.add(row);
        return row;
    }

    public String getText(){
        StringBuilder alltext = new StringBuilder();
        for(HBox hBox:rowBoxList){
            alltext.append(((TextField) hBox.getChildren().get(1)).getText()).append("\n");
        }
        return alltext.toString();
    }
}
