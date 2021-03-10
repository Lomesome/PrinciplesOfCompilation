package pers.lomesome.compliation.controller;

import pers.lomesome.compliation.model.MyProject;

public class OpenProject {

    private static MyProject myProject;

    public static MyProject getMyProject() {
        return myProject;
    }

    public static void setMyProject(MyProject myProject) {
        OpenProject.myProject = myProject;
    }
}
