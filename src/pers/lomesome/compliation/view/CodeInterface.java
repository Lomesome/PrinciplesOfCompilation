package pers.lomesome.compliation.view;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.util.Callback;
import org.fxmisc.flowless.VirtualizedScrollPane;
import pers.lomesome.compliation.model.PropertyWord;
import pers.lomesome.compliation.model.Word;
import pers.lomesome.compliation.controller.*;
import pers.lomesome.compliation.tool.filehandling.FileUtil;
import pers.lomesome.compliation.tool.filehandling.ReadAndWriteFile;
import pers.lomesome.compliation.tool.finalattr.FinalAttribute;
import pers.lomesome.compliation.utils.semantic.Analysis;
import pers.lomesome.compliation.utils.syntax.SyntaxAnalysis;
import pers.lomesome.compliation.utils.lexer.Lexer;
//import pers.lomesome.compliation.utils.lexer.LexicalAnalyzer;
import pers.lomesome.compliation.utils.semantic.SymbolTable;
import pers.lomesome.compliation.view.mywidgets.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CodeInterface {
    private final BorderPane rootBorderPane;
    private final Stage rootStage;
    private final TabPane tabPane = new TabPane();
    private final SplitPane centerSplitPane = new SplitPane();
    private final SplitPane leftRightSplitPane = new SplitPane();
    private double[] lr = {0.2}; //左右SplitPane默认分割比例
    private double[] c = {0.75}; //上下SplitPane默认分割比例
    private TreeView<File> treeView;
    private ContextMenu contextMenu;
    private BorderPane outPane = new BorderPane();
    private final ObservableList<String> logList = FXCollections.observableArrayList();  //记录运行log
    private final ObservableList<PropertyWord> wordList = FXCollections.observableArrayList();
    private final Map<String, TextArea> textAreaMap = new HashMap<>();
    private final ObservableList<Word> obList = FXCollections.observableArrayList();
    private final ListView<Word> error = new ListView<>(obList);

    private void init() {
        for (String s : FileUtil.findAll()) {
            addTab(new File(s));
        }
    }

    public CodeInterface() {
        if (OpenProject.getMyProject()!=null)
            init();
        rootStage = new Stage();
        rootStage.setMaximized(true);
        rootStage.setTitle(OpenProject.getMyProject().getName());
        rootBorderPane = new BorderPane();//主面板

        makeTools();
        leftMenu();
        rightMenu();
        center();
        bottomMenu();

        rootStage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, arg0 -> {
            ManageMainInterface.setMainGui(new MainInterface());
            ManageTreeView.delTreeView();
            ManageTable.delTableBorderPane();
            rootStage.close();
        });

        Scene scene = new Scene(rootBorderPane);
        scene.getStylesheets().add("/resources/css/style.css");
        rootStage.setScene(scene);
        rootStage.show();
    }

    private MenuBar setMenuBar() {
        //创建MenuBar
        MenuBar menuBar = new MenuBar();
        //创建Menu
        Menu idea = new Menu("Lomesome IDEA");
        Menu file = new Menu("File");
        Menu edit = new Menu("Edit");
        Menu view = new Menu("View");
        Menu tools = new Menu("Tools");
        //Menu键入到MenuBar
        menuBar.getMenus().addAll(idea, file, edit, view, tools);
        //创建MenuItem类
        //还可以对MenuItem设置图标
        Menu newFile = new Menu("New");
        MenuItem projectItem = new MenuItem("Project...");
        MenuItem existprojectItem = new MenuItem("Project from Existing Sources");
        final ImageView classImageview = new ImageView(this.getClass().getResource("/resources/images/class.png").toString());
        classImageview.setFitWidth(16);
        classImageview.setFitHeight(16);
        MenuItem classItem = new MenuItem("Lome Class", classImageview);
        newFile.getItems().addAll(projectItem, existprojectItem, new SeparatorMenuItem(), classItem);
        final ImageView openImageview = new ImageView(this.getClass().getResource("/resources/images/open.png").toString());
        openImageview.setFitWidth(16);
        openImageview.setFitHeight(16);
        MenuItem open = new MenuItem("Open...", openImageview);
        Menu openRecent = new Menu("Open Recent");
        MenuItem closeProject = new MenuItem("Close Project");
        MenuItem save = new MenuItem("Save All");
        //设置menuItem的快捷键
        save.setAccelerator(KeyCombination.valueOf("Ctrl+S"));
        MenuItem reload = new MenuItem("Reload All from Disk");

        //将MenuItem放在对应的Menu上
        file.getItems().addAll(newFile, open, openRecent, closeProject, new SeparatorMenuItem(), save, reload);//将分割线加进来

        MenuItem lexicalAnalysis = new MenuItem("Lexical Analysis");
        lexicalAnalysis.setAccelerator(KeyCombination.valueOf("Ctrl+L"));
        tools.getItems().addAll(lexicalAnalysis);

        menuBar.setPadding(new Insets(0, 10, 0, 10));

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");

        projectItem.setOnAction(event -> new NewProject());

        closeProject.setOnAction(event -> {
            ManageMainInterface.setMainGui(new MainInterface());
            ManageTreeView.delTreeView();
            rootStage.close();
        });

        open.setOnAction(event -> {
            File myFile = fileChooser.showOpenDialog(rootStage);
            if (myFile != null) {
                addTab(myFile);
            }
        });

        save.setOnAction(event -> {
            MyCodeArea codeArea = (MyCodeArea) ((VirtualizedScrollPane)tabPane.getSelectionModel().getSelectedItem().getContent()).getContent();
            File myFile = (File) codeArea.getUserData();
            try {
                ReadAndWriteFile.write(myFile.getPath(), codeArea.getText());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        classItem.setOnAction(event -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setContentText("Please enter the name:");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                File myFile = new File(OpenProject.getMyProject().getPath() + "/" + OpenProject.getMyProject().getName() + "/" + result.get() + ".txt");
                addTab(myFile);
            }
        });

        Service<Integer> lexicalAnalyzerService = new Service<Integer>() {
            @Override
            protected Task<Integer> createTask() {
                return new Task<Integer>() {
                    @Override
                    protected Integer call() {
                        TextArea textArea = textAreaMap.get("Run");
                        boolean areaflag = false;
                        if (textArea != null) {
                            textArea.setText("");
                            areaflag = true;
                            textArea.setText("Lexical Analyzer Start!!!\n");
                        }

                        File openFile = (File) tabPane.getSelectionModel().getSelectedItem().getUserData();
//                        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(openFile.getPath());
//                        lexicalAnalyzer.runAnalyzer();

                        wordList.clear();
                        Lexer lexer = null;
                        try {
                            lexer = new Lexer(new FileReader(openFile.getPath()));
                            lexer.next_token();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        assert lexer != null;
                        for (Word word : lexer.getWords()) {
                            Platform.runLater(() -> wordList.addAll(new PropertyWord(word.getType(), String.valueOf(word.getTypenum()), word.getWord())));
                            System.out.println(FinalAttribute.findString(word.getTypenum(), word.getWord()));
                        }

                        if (((BorderPane) rootBorderPane.getRight()).getLeft() != null) {
                            Platform.runLater(obList::clear);
                            for (Word word : lexer.getErrorMsgList()) {
                                Platform.runLater(() -> obList.add(word));
                            }
                        }


                        if (areaflag) {
                            textArea.appendText("Lexical Analyzer finished!!!");
                        }
                        return null;
                    }
                };
            }
        };

        lexicalAnalysis.setOnAction(event -> lexicalAnalyzerService.restart());
        return menuBar;
    }

    private void makeTools() {
        BorderPane toolsBar = new BorderPane();

        MyButton start = new MyButton("/resources/images/start.png", "toolBt");
        MyButton debug = new MyButton("/resources/images/debug.png", "toolBt");
        MyButton stop = new MyButton("/resources/images/stop.png", "toolBt");
        MyButton find = new MyButton("/resources/images/find.png", "toolBt");

        start.setTooltip(new Tooltip("Run"));
        debug.setTooltip(new Tooltip("Debug"));
        stop.setTooltip(new Tooltip("Stop"));
        find.setTooltip(new Tooltip("Search"));
        Service<Integer> runService = new Service<Integer>() {
            @Override
            protected Task<Integer> createTask() {
                return new Task<Integer>() {
                    @Override
                    protected Integer call() throws IOException {
                        long startTime = System.currentTimeMillis();

                        if (textAreaMap.get("Run") != null) {
                            File openFile = (File) tabPane.getSelectionModel().getSelectedItem().getUserData();
                            TextArea textArea = textAreaMap.get("Run");
//                            Platform.runLater(() -> {
//                                textArea.setText("");
//                                textArea.appendText(openFile.getName() + " is running!\n");
//                            });
                            List<Word> list = new LinkedList<>();
                            Lexer lexer = null;
                            try {
                                lexer = new Lexer(new FileReader(openFile.getPath()));
                                lexer.next_token();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            assert lexer != null;
                            for (Word word : lexer.getWords()) {
                                word.setName(FinalAttribute.findString(word.getTypenum(), word.getWord()));
                                list.add(word);
                            }
                            list.add(new Word("#", "end", -1,-1));
                            list.get(list.size() - 1).setName("#");

                            SymbolTable Table = new SymbolTable();
                            Table.getTable(list);
                            List<List<String>> listList = SyntaxAnalysis.analysis(list, Table);

                            for (String s : listList.get(0)){
                                Platform.runLater(() -> textArea.appendText(s));
                            }
                            for (String s : listList.get(1)){
                                Platform.runLater(() -> textArea.appendText(s));
                            }
                            String code = "0";
                            if (listList.get(1).size()>0) {
                                code = "-1";
                            }
                            if (code.equals("0")){
                                Table.printtable();
                                Analysis.analysis(list, Table);
                            }
                            String finalCode = code;
                            Platform.runLater(() -> textArea.appendText("\nProcess finished with exit code " + finalCode +"\n"));

                            long endTime = System.currentTimeMillis();
                            long useTime = endTime - startTime;

                            Platform.runLater(() -> {
                                start.setImageView("/resources/images/start.png");
                                debug.setImageView("/resources/images/debug.png");
                                stop.setImageView("/resources/images/stop.png");
                                logList.add(openFile.getName() + " run successfully in " + useTime / 1000 + " s " + useTime % 1000 + " ms ");
                            });
                        }
                        return null;
                    }
                };
            }
        };

        start.setOnMouseClicked(event -> {
            start.setImageView("/resources/images/restart.png");
            stop.setImageView("/resources/images/stopping.png");
            runService.restart();
        });

        debug.setOnMouseClicked(event -> {
            debug.setImageView("/resources/images/redebug.png");
            stop.setImageView("/resources/images/stopping.png");
        });

        stop.setOnMouseClicked(event -> {
            start.setImageView("/resources/images/start.png");
            debug.setImageView("/resources/images/debug.png");
            stop.setImageView("/resources/images/stop.png");
        });
        Text name = new Text();
        if (OpenProject.getMyProject()!=null){
            name.setText(OpenProject.getMyProject().getName());
        }
        name.setStyle("-fx-font-weight: bold");
        BorderPane.setMargin(name, new Insets(0, 0, 0, 15));
        toolsBar.setStyle("-fx-background-color: #eaeaea");
        BorderPane.setAlignment(name, Pos.CENTER_LEFT);
        Separator separator = new Separator();
        separator.setOrientation(Orientation.VERTICAL);

        HBox tools = new HBox(8, start, debug, stop, separator, find);
        tools.setAlignment(Pos.CENTER);
        tools.setPadding(new Insets(0, 10, 0, 0));
        tools.setMinSize(25, 25);
        toolsBar.setRight(tools);
        toolsBar.setLeft(name);
        toolsBar.setTop(setMenuBar());
        rootBorderPane.setTop(toolsBar);
    }

    private void leftMenu() {
        VerticalLabel projectLabel = new VerticalLabel(VerticalDirection.UP);
        projectLabel.setPadding(new Insets(10, 10, 10, 10));
        projectLabel.setUserData("nochoose");
        projectLabel.setText("1:Project");
        VBox projectBox = new VBox(projectLabel);
        projectBox.setPadding(new Insets(10, 2, 10, 2));

        projectBox.setOnMouseClicked(event -> {
            if (projectLabel.getUserData().equals("nochoose")) {
                projectLabel.setUserData("choose");
                projectBox.setStyle("-fx-background-color: gray");
                if (ManageTreeView.getTreeView() != null) {
                    leftRightSplitPane.setDividerPositions(lr);
                    leftRightSplitPane.getItems().add(0, ManageTreeView.getTreeView());
                } else {
                    initTreeView();
                    ManageTreeView.setTreeView(treeView);
                    leftRightSplitPane.getItems().add(0, treeView);
                }
            } else {
                projectLabel.setUserData("nochoose");
                projectBox.setStyle("-fx-background-color: transparent");
                lr = leftRightSplitPane.getDividerPositions();
                leftRightSplitPane.getItems().remove(0);
            }
        });

        VBox vBox = new VBox(projectBox);
        vBox.setMinWidth(25);
        vBox.setStyle("-fx-background-color: #eaeaea");
        rootBorderPane.setLeft(vBox);
    }

    private void rightMenu() {
        VerticalLabel tableLabel = new VerticalLabel(VerticalDirection.DOWN);
        tableLabel.setPadding(new Insets(10, 10, 10, 10));
        tableLabel.setUserData("nochoose");
        tableLabel.setText("Show Table");
        VBox tableBox = new VBox(tableLabel);
        tableBox.setPadding(new Insets(10, 2, 10, 2));
        BorderPane borderPane = new BorderPane();

        tableBox.setOnMouseClicked(event -> {
            if (tableLabel.getUserData().equals("nochoose")) {
                BorderPane tableBorderPane;
                if (ManageTable.getTableBorderPane() == null) {
                    tableBorderPane = new BorderPane();
                    tableBorderPane.setCenter(new LaxicalAnalyzerTableView(new String[]{"token", "word"}, wordList));
                    error.setPrefSize(300, 300);
                    error.setStyle("-fx-background-color:white;-fx-border-width: 1 0 1 0;-fx-border-color: lightgray");
                    tableBorderPane.setBottom(new VBox(new Label("Error:"), error));
                    ManageTable.setTableBorderPane(tableBorderPane);
                } else {
                    tableBorderPane = ManageTable.getTableBorderPane();
                }
                borderPane.setLeft(tableBorderPane);
                tableLabel.setUserData("choose");
                tableBox.setStyle("-fx-background-color: gray");
            } else {
                borderPane.setLeft(null);
                tableLabel.setUserData("nochoose");
                tableBox.setStyle("-fx-background-color: transparent");
            }
        });
        error.getStyleClass().add("codelist");
        error.setCellFactory(new Callback<ListView<Word>, ListCell<Word>>() {
                                 public ListCell<Word> call(ListView<Word> headerListView) {
                                     return new ListCell<Word>() {
                                         @Override
                                         protected void updateItem(Word item, boolean empty) {
                                             super.updateItem(item, empty);
                                             if (item != null) {
                                                 this.setOnMouseClicked(event -> {
                                                     if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                                                         MyCodeArea codeArea = (MyCodeArea) ((VirtualizedScrollPane)tabPane.getSelectionModel().getSelectedItem().getContent()).getContent();
                                                         Platform.runLater(() -> codeArea.moveToSelect(item.getRow(), item.getCol()));
                                                     } else if (event.getButton() == MouseButton.SECONDARY) {
                                                         MyContextMenu.getInstance().show(this, Side.BOTTOM, 0, 0);
                                                     }
                                                 });
                                                 Label label = new Label(item.getType() + " " + "第" + item.getRow() + "行,第" + (item.getCol() - item.getWord().length()) + "列: " + item.getWord());
                                                 label.setTooltip(new Tooltip("没有建议"));
                                                 setGraphic(label);
                                             } else {
                                                 setText(null);
                                                 setGraphic(null);
                                             }
                                         }
                                     };
                                 }
                             }
        );

        VBox vBox = new VBox(tableBox);
        vBox.setMinWidth(25);
        borderPane.setRight(vBox);
        vBox.setStyle("-fx-background-color: #eaeaea;-fx-border-width: 0 0 0 1;-fx-border-color: lightgray");
        rootBorderPane.setRight(borderPane);
    }

    private void center() {
        leftRightSplitPane.setDividerPositions(0.2f);
        leftRightSplitPane.getItems().addAll(tabPane);
        centerSplitPane.setOrientation(Orientation.VERTICAL);
        centerSplitPane.getItems().addAll(leftRightSplitPane);
        centerSplitPane.setDividerPositions(0.75f);
        rootBorderPane.setCenter(centerSplitPane);
    }

    private void bottomMenu() {
        final ToggleGroup group = new ToggleGroup();
        final MyButton delete = new MyButton("/resources/images/delete_.png", "toolBt2");
        delete.setUserData("notext");
        outPane = new BorderPane();
        delete.setOnMouseClicked(event -> {
            ((TextArea) outPane.getCenter()).setText("");
            delete.setUserData("notext");
            delete.setImageView("/resources/images/delete_.png");
            delete.setStyle("-fx-background-color: transparent");
        });

        delete.setOnMouseEntered(event -> {
            if (delete.getUserData().equals("text")) {
                delete.setStyle("-fx-background-color: #dddddd");
            }
        });

        delete.setOnMouseExited(event -> delete.setStyle("-fx-background-color: transparent"));

        group.selectedToggleProperty().addListener((a, o, n) -> {
            if (n == null) {
                c = centerSplitPane.getDividerPositions();
                centerSplitPane.getItems().remove(1);
            } else {
                if (centerSplitPane.getDividerPositions().length > 0)
                    c = centerSplitPane.getDividerPositions();
                String choosename = n.getToggleGroup().getSelectedToggle().getUserData().toString();
                HBox hBox = new HBox(new Label(choosename + ":"));
                hBox.setPadding(new Insets(5, 10, 5, 10));
                hBox.setStyle("-fx-border-color: lightgray;-fx-border-width: 0 0 1 0");
                hBox.setAlignment(Pos.CENTER_LEFT);
                outPane.setTop(hBox);
                TextArea outArea;
                if (textAreaMap.get(choosename) == null) {
                    outArea = new TextArea();
                    outArea.setEditable(false);
                    outArea.setStyle("-fx-padding: 5 0 5 10");
                    outArea.textProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue.length() == 0) {
                            delete.setUserData("notext");
                            Platform.runLater(() -> delete.setImageView("/resources/images/delete_.png"));
                        } else {
                            delete.setUserData("text");
                            Platform.runLater(() -> delete.setImageView("/resources/images/delete.png"));
                        }
                    });
                    textAreaMap.put(choosename, outArea);
                } else {
                    outArea = textAreaMap.get(choosename);
                }

                if (delete.getWidth() != 0) {
                    if (outArea.getText().length() == 0) {
                        delete.setUserData("notext");
                        delete.setImageView("/resources/images/delete_.png");
                    } else {
                        delete.setUserData("text");
                        delete.setImageView("/resources/images/delete.png");
                    }
                }

                VBox toolBar = new VBox(8, delete);
                toolBar.setStyle("-fx-border-color: lightgray;-fx-border-width: 0 1 0 0");
                toolBar.setPadding(new Insets(10, 3, 10, 3));
                toolBar.setAlignment(Pos.TOP_CENTER);
                outPane.setLeft(toolBar);
                outPane.setCenter(outArea);
                try {
                    centerSplitPane.setDividerPositions(c);
                    centerSplitPane.getItems().set(1, outPane);
                } catch (Exception e) {
                    centerSplitPane.setDividerPositions(c);
                    centerSplitPane.getItems().add(outPane);
                }
            }
        });

        ToggleButton run = new ToggleButton("4:Run");
        run.setUserData("Run");
        run.setStyle("-fx-focus-color:transparent;-fx-faint-focus-color: transparent;-fx-background-insets: 1");
        run.setToggleGroup(group);
        run.setSelected(true);

        ToggleButton problems = new ToggleButton("6:Problems");
        problems.setUserData("Problems");
        problems.setStyle("-fx-focus-color:transparent;-fx-faint-focus-color: transparent;-fx-background-insets: 1");
        problems.setToggleGroup(group);

        ToggleButton build = new ToggleButton("Build");
        build.setUserData("Build");
        build.setStyle("-fx-focus-color:transparent;-fx-faint-focus-color: transparent;-fx-background-insets: 1");
        build.setToggleGroup(group);

        HBox hbox = new HBox(run, problems, build);
        hbox.setStyle("-fx-border-width: 0 0 1 0;-fx-border-color: lightgray");
        Label tips = new Label();
        tips.setPadding(new Insets(1, 0, 1, 30));
        logList.addListener((ListChangeListener<? super String>) o -> tips.setText(o.getList().get(o.getList().size() - 1)));
        VBox vBox = new VBox(hbox, tips);
        HBox.setMargin(run, new Insets(0, 0, 0, 25));
        rootBorderPane.setBottom(vBox);
    }

    private void initTreeView() {
        contextMenu = new ContextMenu();
        // 菜单项
        Menu newBg = new Menu("New");
        // 二级菜单项
        MenuItem delBg = new MenuItem("Delete...");
        delBg.setOnAction(event -> {
            treeView.getFocusModel().getFocusedItem().getParent().getChildren().remove(treeView.getFocusModel().getFocusedItem());
            FileUtil.deleteDir((File) contextMenu.getUserData());
        });
        // 二级菜单项
        final ImageView classImageview = new ImageView(this.getClass().getResource("/resources/images/class.png").toString());
        classImageview.setFitWidth(16);
        classImageview.setFitHeight(16);
        MenuItem classBg = new MenuItem("Lome Class", classImageview);

        classBg.setOnAction(event -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setContentText("Please enter the name:");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                File theFile = treeView.getFocusModel().getFocusedItem().getValue();

                File myFile;
                if (theFile.isDirectory()) {
                    myFile = new File(theFile.getPath() + "/" + result.get() + ".lome");
                    addNode(myFile, treeView.getFocusModel().getFocusedItem());
                } else {
                    myFile = new File(theFile.getParentFile().getPath() + "/" + result.get() + ".lome");
                    addNode(myFile, treeView.getFocusModel().getFocusedItem().getParent());
                }
                addTab(myFile);
            }
        });

        final ImageView packageImageview = new ImageView(this.getClass().getResource("/resources/images/package.png").toString());
        packageImageview.setFitWidth(16);
        packageImageview.setFitHeight(16);

        MenuItem packageBg = new MenuItem("Package", packageImageview);
        packageBg.setOnAction(event -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setContentText("Please enter the name:");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                File theFile = treeView.getFocusModel().getFocusedItem().getValue();
                File myFile;
                if (theFile.isDirectory()) {
                    myFile = new File(theFile.getPath() + "/" + result.get());
                } else {
                    myFile = new File(theFile.getParentFile().getPath() + "/" + result.get());
                }
                if (!myFile.exists()) {
                    if (myFile.mkdirs()) {
                        if (treeView.getFocusModel().getFocusedItem().getParent() != null) {
                            addNode(myFile, treeView.getFocusModel().getFocusedItem().getParent());
                        } else {
                            addNode(myFile, treeView.getFocusModel().getFocusedItem());
                        }
                    }
                } else {
                    TipStage.informationTip("新建package错误", 5, rootStage);
                }
            }
        });

        newBg.getItems().addAll(classBg, packageBg);

        // 将菜单项添加进右键菜单
        contextMenu.getItems().addAll(newBg, new SeparatorMenuItem(), delBg);

        ImageView treeImageview = new ImageView(this.getClass().getResource("/resources/images/project.png").toString());
        TreeItem<File> rootNode;
        if(OpenProject.getMyProject() != null) {
            rootNode = new TreeItem<>(new File(OpenProject.getMyProject().getPath() + "/" + OpenProject.getMyProject().getName()), treeImageview);
        }else {
            rootNode = new TreeItem<>();
        }
        treeImageview.setFitWidth(18);
        treeImageview.setFitHeight(18);
        rootNode.setExpanded(true);
        treeView = new TreeView<>(rootNode);
        treeView.setPrefWidth(200);
        treeView.setCellFactory((TreeView<File> p) -> new TextFieldTreeCellImpl());
        treeView.setStyle("-fx-background-color: transparent;" + "-fx-border-width: 0 0 0 0;" + "-fx-border-style: solid inside;");
        if (OpenProject.getMyProject() != null){
            findFileAndFolderList(new File(OpenProject.getMyProject().getPath() + "/" + OpenProject.getMyProject().getName()), rootNode);
        }
    }

    private void findFileAndFolderList(File dir, TreeItem<File> pNode) {
        if (!dir.exists() || !dir.isDirectory()) {// 判断是否存在目录
            return;
        }
        String[] files = dir.list();// 读取目录下的所有目录文件信息
        assert files != null;
        for (String s : files) {// 循环，添加文件名或回调自身
            File file = new File(dir, s);
            if (file.isFile()) {// 如果是文件
                if (!file.getName().equals(".DS_Store")) {
                    addNode(file, pNode);
                }
            } else {// 如果是目录
                TreeItem<File> newNode = addNode(file, pNode);
                findFileAndFolderList(file, newNode);// 回调自身继续查询
            }
        }
    }

    private TreeItem<File> addNode(File newFile, TreeItem<File> pNode) {
        ImageView treeImageview;
        if (newFile.isDirectory()) {
            treeImageview = new ImageView(this.getClass().getResource("/resources/images/package.png").toString());
        } else {
            treeImageview = new ImageView(this.getClass().getResource("/resources/images/class.png").toString());
        }
        treeImageview.setFitWidth(18);
        treeImageview.setFitHeight(18);
        TreeItem<File> newNode = new TreeItem<>(new File(newFile.getPath()), treeImageview);
        newNode.setExpanded(true);
        pNode.getChildren().add(newNode);
        return newNode;
    }

    private void addTab(File myFile) {
        MyTab code = new MyTab(myFile.getName());
        code.setNode(myFile);
        tabPane.getTabs().add(code);
        tabPane.getSelectionModel().select(code);
    }

    public void setRunText(String content){
        textAreaMap.get("Run").appendText(content + "\n");
    }

    final class TextFieldTreeCellImpl extends TreeCell<File> {

        private TextFieldTreeCellImpl() {

            this.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY) {
                    contextMenu.hide();
                    contextMenu.setUserData(getTreeItem().getValue());
                    contextMenu.show(rootStage, event.getScreenX(), event.getScreenY());
                }
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && getTreeItem().getValue().isFile()) {
                    if (getTreeItem() != null) {
                        MyTab code = new MyTab(getTreeItem().getValue().getName());
                        boolean flag = true;
                        for (Tab tab : tabPane.getTabs()) {
                            if (getTreeItem().getValue().equals(tab.getUserData())) {
                                tabPane.getSelectionModel().select(tab);
                                flag = false;
                            }
                        }
                        if (flag) {
                            code.setUserData(getTreeItem().getValue());
                            code.setNode(getTreeItem().getValue());
                            tabPane.getTabs().add(code);
                            tabPane.getSelectionModel().select(code);
                        }
                        rootStage.setTitle(OpenProject.getMyProject().getName()  + " - " + getTreeItem().getValue().getName());
                    }
                }
            });
        }

        @Override
        public void updateItem(File item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                setText(getString());
                setGraphic(getTreeItem().getGraphic());
            }
        }

        private String getString() {
            return getItem() == null ? "" : getItem().getName();
        }
    }
}