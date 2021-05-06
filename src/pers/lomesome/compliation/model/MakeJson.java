package pers.lomesome.compliation.model;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

public class MakeJson {
    private String name;
    private List<MakeJson> children;

    public MakeJson(){}

    public MakeJson(String name, List<MakeJson> children){
        this.name = name;
        this.children = children;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setChildren(List<MakeJson> children) {
        this.children = children;
    }

    public List<MakeJson> getChildren() {
        return children;
    }

    @Override
    public String toString(){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", name);
            jsonObject.put("children", children);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return StringEscapeUtils.unescapeJava(jsonObject.toString());
    }
}
