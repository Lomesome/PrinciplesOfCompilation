package pers.lomesome.compliation.model;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

/**
 * 该类是JSON类对象用于语法分析定义父子节点
 */
public class MakeJson {
    /**
     * 节点名称
     */
    private String name;
    /**
     * 子节点链表
     */
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
