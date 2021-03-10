package pers.lomesome.compliation.main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Test extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane borderPane = new BorderPane();
        Code textArea = new Code("dasfasfas\ndasdasdsa");
        textArea.setOnKeyPressed(event -> {
            System.out.println(event.getCode());
        });


//
//        VBox vBox = new VBox();
//        vBox.getChildren().add(new Label("1"));
//        textArea.textProperty().addListener((a, o, n)->{
//            String regEx="\n"; //要匹配的子串，用正则表达式
//            Pattern p = Pattern.compile(regEx);
//            Matcher m=p.matcher(n);
//            int i = 1;
//            while(m.find()) {
//                i++;
//            }
//            if(i > vBox.getChildren().size()){
//                for(int j = vBox.getChildren().size(); j < i; j++){
//                    vBox.getChildren().add(new Label(String.valueOf(j + 1)));
//                }
//            }else if(i < vBox.getChildren().size()){
//                for(int j = vBox.getChildren().size(); j > i; j--){
//                    vBox.getChildren().remove(vBox.getChildren().size()-1);
//                }
//            }
//        });
//
//        vBox.setPrefWidth(70);
//        borderPane.setLeft(vBox);
//        ScrollPane scrollPane = new ScrollPane(borderPane);
//        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
//        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        borderPane.setCenter(textArea);
        Scene scene = new Scene(borderPane);
        scene.getStylesheets().add("/resources/css/style.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
