package pers.lomesome.compliation.controller;

import javafx.scene.layout.BorderPane;

public class ManageQtTable {
    private static BorderPane tableBorderPane = null;

    public static BorderPane getTableBorderPane() {
        return tableBorderPane;
    }

    public static void setTableBorderPane(BorderPane tableBorderPane) {
        ManageQtTable.tableBorderPane = tableBorderPane;
    }

    public static void delTableBorderPane(){
        tableBorderPane = null;
    }
}
