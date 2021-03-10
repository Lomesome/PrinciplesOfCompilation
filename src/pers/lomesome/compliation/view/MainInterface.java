package pers.lomesome.compliation.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import pers.lomesome.compliation.model.MyProject;
import pers.lomesome.compliation.controller.ManageProjects;
import pers.lomesome.compliation.controller.OpenProject;
import pers.lomesome.compliation.controller.ReadAndWriteFile;
import java.io.File;
import java.util.Map;
import java.util.Set;

public class MainInterface {

    private final ObservableList<MyProject> obList = FXCollections.observableArrayList();
    private final Stage rootStage = new Stage();

    public void init() {
        ReadAndWriteFile.readObj();
        Set<Map.Entry<String, MyProject>> sets = ManageProjects.getProjectsMap().entrySet();        //获取HashMap键值对
        for (Map.Entry<String, MyProject> set : sets) {                //遍历HashMap键值对
            obList.add(0, set.getValue());
        }
    }

    public MainInterface() {
        init();
        BorderPane borderPane = new BorderPane();
        ListView listView = new ListView(obList);

        listView.setPrefWidth(300);
        listView.setStyle("-fx-padding: 0;-fx-background-color: white;");
        listView.setCellFactory(new Callback<ListView<MyProject>, ListCell<MyProject>>() {
                                    public ListCell<MyProject> call(ListView<MyProject> headerListView) {

                                        return new ListCell<MyProject>() {

                                            @Override
                                            protected void updateItem(MyProject item, boolean empty) {
                                                super.updateItem(item, empty);

                                                if (item != null) {
                                                    this.setOnMouseClicked(event -> {
                                                        if (event.getClickCount() == 2) {
                                                            OpenProject.setMyProject(item);
                                                            ManageProjects.getProjectsMap().get(item.getPath() + item.getName());
                                                            rootStage.close();
                                                            new CodeInterface();
                                                            ManageProjects.getProjectsMap().remove(item.getPath() + item.getName());
                                                            ManageProjects.getProjectsMap().put(item.getPath() + item.getName(), item);
                                                            ReadAndWriteFile.save(ManageProjects.getProjectsMap());
                                                        }
                                                    });
                                                    BorderPane root = new BorderPane();
                                                    root.setPrefHeight(60);
                                                    root.setStyle("-fx-background-color:transparent;");
                                                    Label name = new Label(item.getName());
                                                    name.setStyle("-fx-text-fill: black");
                                                    name.setPadding(new Insets(0, 0, 0, 10));

                                                    Label path = new Label(item.getPath());
                                                    path.setStyle("-fx-text-fill: gray");
                                                    Circle a = new Circle(2);
                                                    ImageView buttonImageView = new ImageView(this.getClass().getResource("/resources/images/close.png").toString());
                                                    buttonImageView.setFitHeight(15);
                                                    buttonImageView.setFitWidth(15);

                                                    Button close = new Button();
                                                    close.setOnMouseClicked(event -> {
                                                        obList.remove(item);
                                                        listView.getSelectionModel().select(null);
                                                        ManageProjects.delProject(item);
                                                        ReadAndWriteFile.save(ManageProjects.getProjectsMap());
                                                    });
                                                    HBox.setMargin(close, new Insets(0, 0, 0, -5));
                                                    close.getStyleClass().add("hclose");
                                                    close.setGraphic(buttonImageView);
                                                    close.setShape(a);
                                                    close.setVisible(false);
                                                    VBox project = new VBox(5, name, path);
                                                    BorderPane.setMargin(project, new Insets(0, 0, 0, 20));
                                                    project.setAlignment(Pos.CENTER_LEFT);
                                                    root.setRight(close);
                                                    root.setCenter(project);
                                                    BorderPane.setAlignment(close, Pos.CENTER);
                                                    BorderPane.setAlignment(project, Pos.CENTER_LEFT);
                                                    root.setOnMouseEntered(event -> close.setVisible(true));
                                                    root.setOnMouseExited(event -> close.setVisible(false));
                                                    root.setCursor(Cursor.HAND);
                                                    setGraphic(root);
                                                } else {
                                                    setGraphic(null);
                                                }
                                            }
                                        };
                                    }
                                }
        );
        borderPane.setLeft(listView);
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setStyle("-fx-background-color: #f5f5f5;-fx-border-color: #d0cfcf;-fx-border-width: 0 0 0 1px");
        ImageView icoImage = new ImageView(this.getClass().getResource("/resources/images/icon.png").toString());
        VBox.setMargin(icoImage, new Insets(40, 0, 0, 0));

        Label nameLabel = new Label("LYH IDEA");
        nameLabel.setStyle("-fx-font-size: 40");

        Label versionLabel = new Label("Version 2021.3.1");
        versionLabel.setStyle("-fx-text-fill: gray;-fx-font-size: 18px");

        HBox newProject = makeLabelBox("/resources/images/new.png", "New Project");
        HBox open = makeLabelBox("/resources/images/open.png", "Open or Import");
        HBox get = makeLabelBox("/resources/images/get.png", "Get from Version Control");

        vBox.getChildren().addAll(icoImage, nameLabel, versionLabel, newProject, open, get);
        borderPane.setCenter(vBox);
        Scene scene = new Scene(borderPane, 750, 450);
        scene.getStylesheets().add("/resources/css/style.css");
        rootStage.setScene(scene);
        rootStage.setTitle("Welcome to Lomesome IDEA");
        rootStage.show();
    }

    public HBox makeLabelBox(String imagePath, String name) {
        HBox hBox = new HBox(5);
        VBox.setMargin(hBox, new Insets(25, 0, 0, 150));
        ImageView imageView = new ImageView(this.getClass().getResource(imagePath).toString());
        imageView.setFitHeight(16);
        imageView.setFitWidth(16);
        Label nameLabel = new Label(name);
        nameLabel.setOnMouseEntered(event -> {
            nameLabel.setStyle("-fx-text-fill: royalblue");
        });
        nameLabel.setOnMouseExited(event -> {
            nameLabel.setStyle("-fx-text-fill: black");
        });

        nameLabel.setOnMouseClicked(event -> {
            switch (name) {
                case "New Project":
                    new NewProject();
                    break;
                case "Open or Import":
                    DirectoryChooser file = new DirectoryChooser();
                    File newFolder = file.showDialog(rootStage);
                    try {
                        String foldpath = newFolder.getPath();
                        StringBuilder path = new StringBuilder();
                        String[] strings = foldpath.split("/");
                        for (int i = 1; i < strings.length - 1; i++) {
                            path.append("/").append(strings[i]);
                        }
                        MyProject myProject;
                        if (ManageProjects.getProjectsMap().get(path + strings[strings.length - 1]) == null) {
                            myProject = new MyProject(strings[strings.length - 1], path.toString());
                            ManageProjects.getProjectsMap().put(path + strings[strings.length - 1], myProject);
                            addObList(myProject);
                            ReadAndWriteFile.save(ManageProjects.getProjectsMap());
                        } else {
                            myProject = ManageProjects.getProjectsMap().get(path + strings[strings.length - 1]);
                            obList.remove(myProject);
                            obList.add(0, myProject);
                            ReadAndWriteFile.save(ManageProjects.getProjectsMap());
                        }
                        OpenProject.setMyProject(myProject);
                        new CodeInterface();
                        rootStage.close();

                    } catch (Exception ignore) { }
                    break;
            }
        });

        nameLabel.setCursor(Cursor.HAND);
        hBox.getChildren().addAll(imageView, nameLabel);
        return hBox;
    }

    public void addObList(MyProject myProject) {
        obList.add(0, myProject);
    }
}
