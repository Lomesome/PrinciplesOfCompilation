package pers.lomesome.compliation.main;

import javafx.application.Application;
import javafx.stage.Stage;
import pers.lomesome.compliation.tool.filehandling.ReadAndWriteFile;
import pers.lomesome.compliation.tool.finalattr.FinalAttribute;
import pers.lomesome.compliation.controller.ManageMainInterface;
import pers.lomesome.compliation.utils.semantic.Analysis;
import pers.lomesome.compliation.utils.syntax.AllGrammer;
import pers.lomesome.compliation.utils.syntax.GrammaticalHandle;
import pers.lomesome.compliation.utils.syntax.SyntaxAnalysis;
import pers.lomesome.compliation.view.MainInterface;

public class Main extends Application {

    @Override
    public void init() {
        String content = ReadAndWriteFile.readFileContent("/Users/leiyunhong/IdeaProjects/PrinciplesOfCompilation/src/resources/grammar/MyGrammer.txt"); //C语言官方文法
        GrammaticalHandle grammaticalHandle = new GrammaticalHandle(content);
        FinalAttribute.setAllGrammer(new AllGrammer(grammaticalHandle.grammaticlHandel()));
        String newG = ReadAndWriteFile.readFileContent("/Users/leiyunhong/IdeaProjects/PrinciplesOfCompilation/src/resources/grammar/MyGrammerWithAction.txt"); //C语言官方文法
        GrammaticalHandle newGrammaticalHandle = new GrammaticalHandle(newG);
        FinalAttribute.setAllGrammerWithAction(new AllGrammer(newGrammaticalHandle.grammaticlHandel()));
        FinalAttribute.init(); //加载关键字、运算符、界符的token值
        SyntaxAnalysis.init(); //预先生成预测表
        Analysis.init();
    }

    @Override
    public void start(Stage primaryStage) {
        ManageMainInterface.setMainGui(new MainInterface());
    }


    public static void main(String[] args) {
        launch(args);
    }
}
