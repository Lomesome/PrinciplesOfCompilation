package pers.lomesome.compliation.model;

import java.io.Serializable;

public class MyProject  implements Serializable {

    private static final long serialVersionUID = 5160728458299623666L;
    private String name;
    private String path;


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
