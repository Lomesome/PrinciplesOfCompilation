package pers.lomesome.compliation.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import pers.lomesome.compliation.model.MyProject;
import pers.lomesome.compliation.controller.ManageMainInterface;
import pers.lomesome.compliation.controller.ManageProjects;
import pers.lomesome.compliation.tool.filehandling.ReadAndWriteFile;
import java.io.File;

public class NewProject {

    public NewProject() {
        Stage rootStage = new Stage();
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10, 20, 20, 20));
        Label name = new Label("Project name:");
        name.setMinWidth(90);
        TextField nameField = new TextField();
        nameField.setPrefWidth(1100);
        HBox nameBox = new HBox(10, name, nameField);
        nameBox.setAlignment(Pos.CENTER_LEFT);

        Label location = new Label("Project location:");
        location.setMinWidth(90);
        TextField locationField = new TextField();
        locationField.setPrefWidth(1100);

        Label modulename = new Label("Module name:");
        modulename.setMinWidth(90);
        TextField modulenameField = new TextField();
        modulenameField.setPrefWidth(1100);
        HBox modulenameBox = new HBox(10, modulename, modulenameField);
        modulenameBox.setAlignment(Pos.CENTER_LEFT);

        Label contentroot = new Label("Content root:");
        contentroot.setMinWidth(90);
        TextField contentrootField = new TextField();
        contentrootField.setPrefWidth(1100);
        HBox contentrootBox = new HBox(10, contentroot, contentrootField);
        contentrootBox.setAlignment(Pos.CENTER_LEFT);
        modulenameBox.setPadding(new Insets(0, 0, 0, 15));
        contentrootBox.setPadding(new Insets(0, 0, 0, 15));

        VBox vBox = new VBox(10, modulenameBox, contentrootBox);

        TitledPane titledPane = new TitledPane("More Setting", vBox);
        titledPane.setExpanded(false);

        nameField.textProperty().addListener((arg, o, n) -> modulenameField.setText(n));

        modulenameField.textProperty().addListener((arg, o, n) -> nameField.setText(n));

        locationField.textProperty().addListener((arg, o, n) -> contentrootField.setText(n));

        contentrootField.textProperty().addListener((arg, o, n) -> locationField.setText(n));

        Button pathButton = new Button("…");

        pathButton.setOnMouseClicked(event -> {
            DirectoryChooser file = new DirectoryChooser();
            File newFolder = file.showDialog(rootStage);
            try {
                String foldpath = newFolder.getPath();
                locationField.setText(foldpath);
                contentrootField.setText(foldpath);
                modulenameField.setText(foldpath.split("/")[foldpath.split("/").length - 1]);
                nameField.setText(foldpath.split("/")[foldpath.split("/").length - 1]);
            } catch (Exception ignore) {
            }
        });
        HBox locationBox = new HBox(10, location, locationField, pathButton);
        locationBox.setAlignment(Pos.CENTER_LEFT);

        VBox attr = new VBox(10, nameBox, locationBox);

        Button help = new Button("?");

        help.getStyleClass().addAll("button-help", "button-foc");
        Button cancel = new Button("Cancel");
        cancel.getStyleClass().addAll("button-cancel", "button-foc");
        cancel.setMinWidth(70);
        cancel.setMaxHeight(30);
        cancel.setOnMouseClicked(event -> rootStage.close());
        Button finish = new Button("Finish");
        finish.setOnMouseClicked(event -> {
            String path = locationField.getText() + "/" + nameField.getText(); //所创建文件目录
            File f = new File(path);
            if (!f.exists()) {
                f.mkdirs(); //创建目录
            }
            MyProject myProject = new MyProject(nameField.getText(), locationField.getText());
            ManageMainInterface.getMainGui().addObList(myProject);
            ManageProjects.getProjectsMap().put(locationField.getText() + nameField.getText(), myProject);
            ReadAndWriteFile.save(ManageProjects.getProjectsMap());
            rootStage.close();
        });
        finish.getStyleClass().addAll("button-finish", "button-foc");
        finish.setMinWidth(70);
        finish.setMaxHeight(30);

        Label none = new Label("");
        none.setPrefWidth(1100);
        HBox buttonBox = new HBox(10, help, cancel, none, finish);
        buttonBox.setMinHeight(40);
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        borderPane.setTop(attr);
        borderPane.setCenter(titledPane);
        BorderPane.setAlignment(titledPane, Pos.BOTTOM_LEFT);
        borderPane.setBottom(buttonBox);
        Scene scene = new Scene(borderPane, 1200, 700);
        scene.getStylesheets().add("/resources/css/style.css");
        rootStage.setScene(scene);
        rootStage.setTitle("New Project");
        rootStage.show();
    }
}