package pers.lomesome.compliation.controller;

import javafx.scene.control.TreeView;
import java.io.File;

public class ManageTreeView {

    private static TreeView<File> treeView = null;

    public static TreeView<File> getTreeView() {
        return treeView;
    }

    public static void setTreeView(TreeView<File> treeView) {
        ManageTreeView.treeView = treeView;
    }

    public static void delTreeView(){
        treeView = null;
    }
}
