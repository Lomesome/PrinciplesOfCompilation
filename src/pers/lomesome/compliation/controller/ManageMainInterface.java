package pers.lomesome.compliation.controller;

import pers.lomesome.compliation.view.MainInterface;

public class ManageMainInterface {

    private static MainInterface mainGui;

    public static MainInterface getMainGui() {
        return mainGui;
    }

    public static void setMainGui(MainInterface mainGui) {
        ManageMainInterface.mainGui = mainGui;
    }

}
