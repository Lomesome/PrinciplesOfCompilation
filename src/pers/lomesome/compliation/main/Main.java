package pers.lomesome.compliation.main;

import javafx.application.Application;
import javafx.stage.Stage;
import pers.lomesome.compliation.tool.finalattr.FinalAttribute;
import pers.lomesome.compliation.controller.ManageMainInterface;
import pers.lomesome.compliation.view.MainInterface;

public class Main extends Application {

    @Override
    public void init() {
        FinalAttribute.init(); //加载关键字、运算符、界符的token值
    }

    @Override
    public void start(Stage primaryStage) {
        ManageMainInterface.setMainGui(new MainInterface());
    }


    public static void main(String[] args) {
        launch(args);
    }
}
