package pers.lomesome.compliation.view.mywidgets;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class MyContextMenu extends ContextMenu {
    //单例
    private static MyContextMenu INSTANCE = null;

    // 私有构造函数
    private MyContextMenu() {
        MenuItem suggestionMenuItem = new MenuItem("Suggestions");
        getItems().add(suggestionMenuItem);
    }

    // 获取实例 *
    // @return MyContextMenu
    public static MyContextMenu getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MyContextMenu();
        }
        return INSTANCE;
    }
}