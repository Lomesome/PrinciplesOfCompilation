package pers.lomesome.compliation.model;

import java.io.Serializable;

/*
    对工程的定义
    通过Serializable使该类型能被实例化保存
 */

public class MyProject implements Serializable {

    private static final long serialVersionUID = 5160728458299623666L;
    private String name;  //工程名称
    private String path;  //工程路径（在本电脑的路径）

    public MyProject(String name, String path){
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
