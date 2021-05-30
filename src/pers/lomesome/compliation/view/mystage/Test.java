package pers.lomesome.compliation.view.mystage;

import javafx.application.Application;
import javafx.stage.Stage;

public class Test extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        new Reg2Dfa2Mfa();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
