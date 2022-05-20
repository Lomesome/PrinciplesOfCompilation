package pers.lomesome.compliation.view.mystage;

import com.lomesome.util.BytesImage;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import pers.lomesome.compliation.utils.nfadfamfa.*;
import java.io.ByteArrayInputStream;

public class Reg2Dfa2Mfa {
    private ImageView imageView = new ImageView();
    DataTransfer dataTransfer;

    public Reg2Dfa2Mfa() {

        Stage primaryStage = new Stage();
        BorderPane borderPane = new BorderPane();
        Label label = new Label("正规式");
        label.setStyle("-fx-padding: 8px 0 0 10px");
        TextField textField = new TextField();
        textField.setText("(a|b)*");
        Button reg2nfa = new Button("reg to nfa");
        Button nfa2dfa = new Button("nfa to dfa");
        Button mindfa = new Button("min dfa");
        reg2nfa.setDisable(false);
        nfa2dfa.setDisable(true);
        mindfa.setDisable(true);
        HBox hBox = new HBox(20, label, textField, reg2nfa, nfa2dfa, mindfa);
        hBox.setPadding(new Insets(10,10,10,20));
        borderPane.setTop(hBox);

        imageView.setFitWidth(900);
        borderPane.setCenter(imageView);

        Service<Integer> reg2nfaService = new Service<Integer>() {
            @Override
            protected Task<Integer> createTask() {
                return new Task<Integer>() {
                    @Override
                    protected Integer call() throws Exception {
                        dataTransfer = new DataTransfer();
                        imageView = new ImageView(new Image(new ByteArrayInputStream(new BytesImage().makeBytesImage(new RegToNfa(dataTransfer).getNFAs(textField.getText())))));
                        imageView.setFitWidth(900);
                        imageView.setFitHeight(500);
                        Platform.runLater(() -> {
                            borderPane.setCenter(imageView);
                        });
                        return null;
                    }
                };
            }
        };

        Service<Integer> nfa2dfaService = new Service<Integer>() {
            @Override
            protected Task<Integer> createTask() {
                return new Task<Integer>() {
                    @Override
                    protected Integer call() throws Exception {
                        imageView = new ImageView(new Image(new ByteArrayInputStream(new BytesImage().makeBytesImage(new NfaToDfa(dataTransfer).getDFAs()))));
                        imageView.setFitWidth(900);
                        imageView.setFitHeight(500);
                        Platform.runLater(() -> {
                            borderPane.setCenter(imageView);
                        });
                        return null;
                    }
                };
            }
        };

        Service<Integer> mindfaService = new Service<Integer>() {
            @Override
            protected Task<Integer> createTask() {
                return new Task<Integer>() {
                    @Override
                    protected Integer call() throws Exception {
                        imageView = new ImageView(new Image(new ByteArrayInputStream(new BytesImage().makeBytesImage(new DfaToMfa(dataTransfer).getMFAS()))));
                        imageView.setFitWidth(900);
                        imageView.setFitHeight(500);
                        Platform.runLater(() -> {
                            borderPane.setCenter(imageView);
                        });
                        return null;
                    }
                };
            }
        };

        reg2nfa.setOnMouseClicked(event -> {
            reg2nfaService.restart();
            reg2nfa.setDisable(true);
            nfa2dfa.setDisable(false);
            mindfa.setDisable(true);
        });

        nfa2dfa.setOnMouseClicked(event -> {
            nfa2dfaService.restart();
            reg2nfa.setDisable(true);
            nfa2dfa.setDisable(true);
            mindfa.setDisable(false);
        });

        mindfa.setOnMouseClicked(event -> {
            mindfaService.restart();
            reg2nfa.setDisable(false);
            nfa2dfa.setDisable(true);
            mindfa.setDisable(true);
        });

        Scene scene = new Scene(borderPane, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}