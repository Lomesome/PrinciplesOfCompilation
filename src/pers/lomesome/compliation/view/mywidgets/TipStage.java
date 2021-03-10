package pers.lomesome.compliation.view.mywidgets;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class TipStage {

    public static void informationTip(String header, String message, Stage rootStage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("信息");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.initOwner(rootStage);
        alert.show();
    }

    public static void informationTip(String header, int messageNum, Stage rootStage) {
        String[] message = {"", "", "", "", "添加失败", "该路存在重复的文件/文件夹名", "文件/文件夹名不能为空或包含特殊字符"};
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("信息");
        alert.setHeaderText(header);
        alert.setContentText(message[messageNum]);
        alert.initOwner(rootStage);
        alert.show();
    }

    public static Alert sureTip(String p_header, String p_message, Stage rootStage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("信息");
        alert.setHeaderText(p_header);
        alert.setContentText(p_message);
        alert.initOwner(rootStage);
        return alert;
    }
}
