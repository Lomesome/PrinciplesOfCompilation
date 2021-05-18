package pers.lomesome.compliation.utils.semantic;

import pers.lomesome.compliation.model.LiveStatu;
import pers.lomesome.compliation.model.Pair;
import pers.lomesome.compliation.model.Word;
import java.util.ArrayList;
import java.util.List;

public class SymbolTable {

    public static class Var {
        public String name; // 变量名
        public List<Integer> scope = new ArrayList<>(); //作用域
        public String type; // 变量类型，int,char,float,string
        public String value; // 变量值
        public int tp; // 若数组则为数组单元个数
        public int offset; // 偏移量
        @Override
        public String toString(){
            return name + "\t\t" + type + "\t\t" + value + "\t\t" + tp + "\t\t" + offset + "\t\t\t" + scope;
        }
    }

    public static class Cons{
        public String name; // 常量名
        public List<Integer> scope = new ArrayList<>(); //作用域
        public String type; // 常量类型，int,char,float,string
        public String value; // 常量值
        public int tp; // 若数组则为数组单元个数
        public int offset; // 偏移量
        @Override
        public String toString(){
            return name + "\t" + type + "\t" + tp + "\t" + offset;
        }
    }

    public static class Fun {
        public String name; // 函数名
        public String type; // 返回值类型，int,char,float，void
        public List<Pair<String, String>> args = new ArrayList<>();

        @Override
        public String toString(){
            return name + "\t\t" + type + "\t\t\t" + args;
        }

    }

    public List<Var> Synbl = new ArrayList<>(); //变量表
    public List<Var> Temp = new ArrayList<>(); //中间变量表
    public List<Fun> Func = new ArrayList<>(); //函数表
    public List<Cons> Const = new ArrayList<>(); //常量表
    public LiveStatu liveStatu;


    private int state = 1;
    private String stype;
    private String sname;
    private int off = 0;
    private int offs = 0;
    private boolean isArr = false;
    private int len = 0;
    public final String funcname;
    public final String functype;
    public List<Pair<String, String>> argsList= new ArrayList<>();

    public SymbolTable(String funcname, String functype){
        this.funcname = funcname;
        this.functype = functype;
        liveStatu = new LiveStatu();
    }

    boolean SearchSynbl(String s) {
        int count ;
        for (Var v : Synbl) {
            if (v.name.equals(s) ){
                count = 0;
                for (int i = 0; i < v.scope.size(); i++){
                    if (v.scope.get(i).equals(Analysis.actionscope.get(i))){
                        count++;
                    }
                }
                if (count == Analysis.actionscope.size() && Analysis.actionscope.size() == v.scope.size()){
                    return true;
                }
            }
        }
        return false;
    }

    void addSynbl() {
        Var v = new Var();
        v.name = sname;
        v.type = stype;
        int size = 0;
        switch (stype) {
            case "int":
                size = 4;
                break;
            case "char":
                size = 1;
                break;
            case "float":
                size = 8;
                break;
            case "String":
                size = 50;
                break;
        }
        if (isArr) {
            v.tp = len;
            v.type = stype + "[]";
        }
        if (!SearchSynbl(sname)) {
            off = offs;
            v.offset = off;
            offs = off + size * len;//加入该变量后末尾地址等于原偏移地址加上该变量的长度
            v.scope = new ArrayList<>(Analysis.actionscope);
            Synbl.add(v);
        } else {
            Analysis.flag = false;
            Analysis.errorMsg.add(sname + "重定义");
            System.out.println(sname + "重定义");
        }
    }

    public void setTable(Word word){
        String s = word.getWord();
        state = changeState(s, state);//自动机状态变换
    }

    int changeState(String s, int state) {
        switch (state) {
            case 1:
                if (s.equals("int") || s.equals("char") || s.equals("String") || s.equals("float")) {
                    stype = s;
                    state = 2;
                } else
                    state = 1;
                break;
            case 2:
                sname = s;
                state = 3;
                break;
            case 3:
                if (s.equals(",")) {
                    isArr = false;
                    len = 1;
                    addSynbl();
                    state = 2;
                } else if (s.equals("[")) {
                    state = 4;
                    isArr = true;
                } else if (!(s.equals(";") || s.equals("(") || s.equals(")"))) {
                    state = 3;
                }
                else {
                    isArr = false;
                    len = 1;
                    addSynbl();
                    state = 1;
                }
                break;
            case 4:
                len = Integer.parseInt(s);
                state = 5;
                break;
            case 5:
                if (s.equals("]")) {
                    addSynbl();
                    state = 6;
                }
                break;
            case 6:
                if (s.equals(",")) {
                    state = 2;
                } else {
                    state = 1;
                }
                break;
        }
        return state;
    }

    public List<List<Object>> printTable() {
        List<List<Object>> results = new ArrayList<>();
        System.out.println("符号表：");
        List<Object> synblList = new ArrayList<>();
        for (Var v : Synbl) {
            synblList.add(v);
            System.out.printf("%10s", v.name);
            System.out.printf("%10s", v.type);
            System.out.printf("%10s", v.value);
            System.out.printf("%10s", v.offset);
            System.out.printf("%10s", v.tp);
            System.out.printf("\t\t\t%-10s", v.scope);
            System.out.println();
        }
        System.out.println("函数表：");
        List<Object> funclList = new ArrayList<>();
        for (Fun f : Func) {
            funclList.add(f);
            System.out.printf("%10s", f.name);
            System.out.printf("%10s", f.type);
            System.out.printf("\t\t\t%-10s", f.args);
            System.out.println();
        }
        results.add(synblList);
        results.add(funclList);
        return results;
    }

    public LiveStatu getLiveStatu() {
        return liveStatu;
    }

    public void setLiveStatu(LiveStatu liveStatu) {
        this.liveStatu = liveStatu;
    }
}