package pers.lomesome.compliation.view.mystage;

import com.lomesome.util.BytesImage;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import pers.lomesome.compliation.model.DFA;
import pers.lomesome.compliation.model.Edge;
import pers.lomesome.compliation.model.NFA;
import pers.lomesome.compliation.utils.minidfa.MinDFA;
import pers.lomesome.compliation.utils.reg2nfa.*;
import pers.lomesome.compliation.utils.nfa2dfa.NfaToDfa;
import java.io.ByteArrayInputStream;
import java.util.*;

public class RegToNfaStage {
    private final Reg2Nfa reg2Nfa = new Reg2Nfa();
    private List<Edge> edgeList ;
    private NFA nfa;
    private boolean flag = true;
    private String[] strings = new String[]{};
    private ImageView imageView = new ImageView();
    private DFA dfa = new DFA();

    public RegToNfaStage() {
        Stage primaryStage = new Stage();
        BorderPane borderPane = new BorderPane();
        Label label = new Label("正规式");
        TextField textField = new TextField();
        Button reg2nfa = new Button("reg to nfa");
        Button nfa2dfa = new Button("nfa to dfa");
        Button mindfa = new Button("min dfa");
        HBox hBox = new HBox(20, label, textField, reg2nfa, nfa2dfa, mindfa);
        borderPane.setTop(hBox);

        imageView.setFitWidth(900);
        borderPane.setCenter(imageView);

        Service<Integer> reg2nfaService = new Service<Integer>() {
            @Override
            protected Task<Integer> createTask() {
                return new Task<Integer>() {
                    @Override
                    protected Integer call() throws Exception {
                        Rpn rpn = new Rpn();
                        String _regex = rpn.reverse2Rpn(textField.getText());
                        strings = rpn.getSymbol(_regex);
                        nfa = reg2Nfa.express_2_NFA(_regex);
                        imageView = new BytesImage().pureMakeImageViewHorizontal(reg2Nfa.getResults(nfa), 2000);
                        imageView.setFitWidth(900);
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
                        NfaToDfa nfaToDfa = new NfaToDfa(nfa.getEdgeList(), Integer.parseInt(nfa.getBeginState().getStateName()), strings);
                        imageView = new ImageView(new Image(new ByteArrayInputStream(new BytesImage().makeBytesImage(nfaToDfa.getResults(nfa.getEndState())))));
                        edgeList = nfaToDfa.getEdgeList();
                        dfa.setK(nfaToDfa.getMap());
                        dfa.setS(nfaToDfa.getStart());
                        char[] chars = new char[strings.length];
                        for (int i = 0; i < strings.length; i++){
                            chars[i] = strings[i].charAt(0);
                        }
                        dfa.setLetters(chars);

                        dfa.setZ(new ArrayList<>(nfaToDfa.getVt()));
                        imageView.setFitWidth(900);
                        Platform.runLater(() -> {
                            borderPane.setCenter(imageView);
                        });
                        System.out.println(nfaToDfa.getStateSize());
                        enterF(nfaToDfa.getStateSize());
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
                        MinDFA minDFA = new MinDFA(dfa, edgeList);

                        imageView = new ImageView(new Image(new ByteArrayInputStream(new BytesImage().makeBytesImage(minDFA.getResult()))));
                        imageView.setFitWidth(900);
                        Platform.runLater(() -> {
                            borderPane.setCenter(imageView);
                        });
                        return null;
                    }
                };
            }
        };

        final ChangeListener<Number> listener = new ChangeListener<Number>() {
            final Timer timer = new Timer();
            TimerTask task = null;
            final long delayTime = 5;

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, final Number newValue) {
                if (task != null) {
                    task.cancel();
                }
                task = new TimerTask() {
                    @Override
                    public void run() {
                        flag = true;
                        if (imageView.getFitHeight() + 10 != primaryStage.getHeight()) {
                            imageView.setFitHeight(primaryStage.getHeight() - 40);
                        }
                        if (imageView.getFitWidth() + 10 != primaryStage.getWidth()) {
                            imageView.setFitWidth(primaryStage.getWidth());
                        }
                    }
                };
                timer.schedule(task, delayTime);
            }
        };

        primaryStage.widthProperty().addListener(listener);
        primaryStage.heightProperty().addListener(listener);

        reg2nfa.setOnMouseClicked(event -> {
            Rpn rpn = new Rpn();
            String _regex = rpn.reverse2Rpn(textField.getText());
            strings = rpn.getSymbol(_regex);
            nfa = reg2Nfa.express_2_NFA(_regex);
            imageView = new ImageView(new Image(new ByteArrayInputStream(new BytesImage().makeBytesImage(reg2Nfa.getResults(nfa)))));
            imageView.setFitWidth(900);
            Platform.runLater(() -> {
                borderPane.setCenter(imageView);
            });
        });

        nfa2dfa.setOnMouseClicked(event -> {
            nfa2dfaService.restart();
        });

        mindfa.setOnMouseClicked(event -> {
            MinDFA minDFA = new MinDFA(dfa, edgeList);

            imageView = new ImageView(new Image(new ByteArrayInputStream(new BytesImage().makeBytesImage(minDFA.getResult()))));
            imageView.setFitWidth(900);
            Platform.runLater(() -> {
                borderPane.setCenter(imageView);
            });
//            mindfaService.restart();
        });

        Scene scene = new Scene(borderPane, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void enterF(int state) {

        String[][] f = new String[state][state];
        for(String[] arr : f){
            Arrays.fill(arr, "");
        }
        for(int i = 0; i < f.length; i++){
            f[i][i] = "ε";
        }
        for (Edge edge :edgeList){
            f[Integer.parseInt(edge.getBegin().toString())][Integer.parseInt(edge.getEnd().toString())] += edge.getLabel();
        }

        dfa.setF(f);

    }


}