package pers.lomesome.compliation.controller;

import pers.lomesome.compliation.view.MainInterface;

public class ManageMainInterface {

    private static MainInterface mainGui;  //保存当前程序运行的主界面的状态

    public static MainInterface getMainGui() {
        return mainGui;
    }

    public static void setMainGui(MainInterface mainGui) {
        ManageMainInterface.mainGui = mainGui;
    }

}
